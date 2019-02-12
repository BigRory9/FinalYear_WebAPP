package com.roryharford.user;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.Request;
import org.apache.jasper.tagplugins.jstl.core.Url;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.roryharford.event.Event;
import com.roryharford.ticket.Ticket;
import com.roryharford.ticket.TicketController;
import com.roryharford.ticket.TicketService;

//will eventually be mapped to Customer
@Controller
public class UserController {

	@Autowired
	private UserService userService;

	

//	@Autowired
//	private TicketService ticketService;

	@RequestMapping(value = "/")
	public String index() {
		userService.createEventArray();
		return "homepage";
	}

	@RequestMapping(value = "/loginPage")
	public String redirect() {
		return "login";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/upload")
	public String handleFIleUpload(@RequestParam("file") MultipartFile file) {
		BasicAWSCredentials creds = new BasicAWSCredentials("AKIAITAEL7BGCI2WOZMA",
				"czY/LBxMNNgabRanQdt1pNm7jbM+Fl2iDKOFdjup");
		AmazonS3 s3Client = AmazonS3Client.builder().withRegion("eu-west-1")
				.withCredentials(new AWSStaticCredentialsProvider(creds)).build();

		InputStream is;
		try {
			is = file.getInputStream();

			// save on s3 wont allow me to save with public read access
			s3Client.putObject(new PutObjectRequest("tickets-fare-images", "newFile", is, new ObjectMetadata())
					.withCannedAcl(CannedAccessControlList.PublicRead));

			// Get a refernce to the image Object
			S3Object s3object = s3Client.getObject(new GetObjectRequest("tickets-fare-images", "newFile"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "homepage";

	}

	@RequestMapping("/users")
	public List<User> getAllUsers() {
		System.out.println("ENTERED " + userService.getAllUsers().toString());
		return userService.getAllUsers();
	}

	@RequestMapping("/users/{id}")
	public User getUser(@PathVariable String id) {
		return userService.getUser(id);
	}

	@PostMapping(value = "/users")
	public void addUser(@RequestBody User User) {
		userService.addUser(User);
	}

	@PutMapping(value = "/users/{id}")
	public void updateUser(@RequestBody User User, @PathVariable String id) {
		userService.updateUser(id, User);
	}

	@DeleteMapping(value = "/users/{id}")
	public void deleteUser(@PathVariable String id) {
		userService.deleteUser(id);
	}

	@RequestMapping(value = "/Customer", method = RequestMethod.GET)
	public String Customer() {
		return "Customer";

	}

	@PostMapping("/login")
	public String verifyCustomer(@ModelAttribute UserLoginDetails CustomerDetails, HttpSession session,
			RedirectAttributes attr, final BindingResult binding,Model model) {
		// Your code here
		System.out.println(CustomerDetails.getInputEmail() + " " + CustomerDetails.getInputPassword());
		User user = userService.loginCustomer(CustomerDetails.getInputEmail(),
				CustomerDetails.getInputPassword());
		if (user == null) {
			attr.addFlashAttribute("org.springframework.validation.BindingResult.register", binding);
			attr.addFlashAttribute("msg", "Wrong Details");
			return "redirect:/";
		}
		else
		{
		session.setAttribute("user", user);
		for (int i = 0; i < userService.getEventList().size(); i++) {
			System.out.println(userService.getEventList().get(i).toString());
		}

		if(userService.getEventList().size()<0)
		{
			System.out.println("NO TICKETS");
		}
		model.addAttribute("lists", userService.getEventList());
		return "success";
		}
	}

	@RequestMapping("/logout")
	public String logoutCustomer(HttpServletRequest request) {

		HttpSession session = request.getSession();
		session.invalidate();
		return "homepage";

	}

	@PostMapping("/register")
	public String registerCustomer(Model model, @ModelAttribute User customer, HttpSession session,
			@RequestParam("file") MultipartFile file) {
		BasicAWSCredentials creds = new BasicAWSCredentials("AKIAITAEL7BGCI2WOZMA",
				"czY/LBxMNNgabRanQdt1pNm7jbM+Fl2iDKOFdjup");
		AmazonS3 s3Client = AmazonS3Client.builder().withRegion("eu-west-1")
				.withCredentials(new AWSStaticCredentialsProvider(creds)).build();

		InputStream is;
		try {
			is = file.getInputStream();

			// save on s3 wont allow me to save with public read access
			s3Client.putObject(new PutObjectRequest("tickets-fare-images", "newFile.jpg", is, new ObjectMetadata())
					.withCannedAcl(CannedAccessControlList.PublicRead));

			// Get a refernce to the image Object
			S3Object s3object = s3Client.getObject(new GetObjectRequest("tickets-fare-images", "newFile.jpg"));
			User customertype = userService.createCustomer(customer);
			if (customertype == null) {
				return "redirect:/";
			}
			session.setAttribute("customer", customer);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "success";

	}

	@RequestMapping("/registerPage")
	public String showRegister() {
		return "register";
	}

//	@RequestMapping("/{userId}/tickets")
//	public TicketController getCommentResource() {
//		return new TicketController();
//	}

}
