package com.roryharford.user;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.catalina.connector.Request;
import org.apache.jasper.tagplugins.jstl.core.Url;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.roryharford.event.Event;
import com.roryharford.ticket.Ticket;
import com.roryharford.ticket.TicketController;
import com.roryharford.ticket.TicketService;
import com.roryharford.user.security.BCrypt;

//will eventually be mapped to Customer
@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private TicketService ticketService;

//	@Autowired
//	private TicketService ticketService;

	@RequestMapping(value = "/")
	public String index() {
		return "homepage";
	}

	@RequestMapping(value = "/homepage")
	public String redirect(Model model) {
		ticketService.createEventArray(0);
		model.addAttribute("lists", ticketService.getEventList());
		return "success";
	}

//	@RequestMapping(method = RequestMethod.POST, value = "/upload")
//	public String handleFIleUpload(@RequestParam("file") MultipartFile file) {
//		BasicAWSCredentials creds = new BasicAWSCredentials("AKIAITAEL7BGCI2WOZMA",
//				"czY/LBxMNNgabRanQdt1pNm7jbM+Fl2iDKOFdjup");
//		AmazonS3 s3Client = AmazonS3Client.builder().withRegion("eu-west-1")
//				.withCredentials(new AWSStaticCredentialsProvider(creds)).build();
//
//		InputStream is;
//		try {
//			is = file.getInputStream();
//
//			// save on s3 wont allow me to save with public read access
//			s3Client.putObject(new PutObjectRequest("tickets-fare-images", "newFile", is, new ObjectMetadata())
//					.withCannedAcl(CannedAccessControlList.PublicRead));
//
//			// Get a refernce to the image Object
//			S3Object s3object = s3Client.getObject(new GetObjectRequest("tickets-fare-images", "newFile"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return "homepage";
//
//	}

	@RequestMapping("/users")
	public List<User> getAllUsers() {
		System.out.println("ENTERED " + userService.getAllUsers().toString());
		return userService.getAllUsers();
	}

//	@RequestMapping("/users/{id}")
//	public User getUser(@PathVariable String id) {
//		return userService.getUser(id);
//	}

	@PostMapping(value = "/users")
	public void addUser(@RequestBody User User) {
		userService.addUser(User);
	}

	@PutMapping(value = "/users/{id}")
	public void updateUser(@RequestBody User User, @PathVariable String id) {
		userService.updateUser(id, User);
	}

//	@DeleteMapping(value = "/users/{id}")
//	public void deleteUser(@PathVariable String id) {
//		userService.deleteUser(id);
//	}

	@RequestMapping(value = "/Customer", method = RequestMethod.GET)
	public String Customer() {
		return "Customer";

	}

	@RequestMapping(value = { "/logout" }, method = RequestMethod.GET)
	public String login(HttpSession session) {
		session.setAttribute("user", null);
		return "homepage";
	}

	@PostMapping("/login")
	public String verifyCustomer(@ModelAttribute("user") UserLoginDetails CustomerDetails, HttpSession session,
			RedirectAttributes attr, final BindingResult binding, Model model) {
		// Your code here
		System.out.println("VERY IMPORTANT" + CustomerDetails.getInputPassword());
		User user = userService.loginCustomer(CustomerDetails.getInputEmail(), CustomerDetails.getInputPassword());

		if (user == null) {
			attr.addFlashAttribute("org.springframework.validation.BindingResult.register", binding);
			attr.addFlashAttribute("msg", "Wrong Details");
			return "redirect:/";
		} else {
			ticketService.createEventArray(0);
			model.addAttribute("lists", ticketService.getEventList());
			session.setAttribute("user", user);
		}

		return "success";
	}

	@PostMapping("/register")
	public String createUser(Model model, @Valid @ModelAttribute("user") User user, BindingResult bindingResult,
			@RequestParam("file") MultipartFile file, HttpServletRequest request, final BindingResult binding,
			HttpSession session) {
		if (file.isEmpty()) {
			request.setAttribute("msg", "ERROR! You must upload a photo");
			return "register";
		}
		if (userService.getUserByEmail(user.getEmail()) == null) {

			if (userService.getUserByUsername(user.getName()) == null) {

				InputStream is;
				BasicAWSCredentials creds = new BasicAWSCredentials("AKIAJVL5I336SYABBB4A",
						"I7gmPoB7tY5bUky5GjLsDijZucjLG/8sngV/UZg6");
				AmazonS3 s3Client = AmazonS3Client.builder().withRegion("eu-west-1")
						.withCredentials(new AWSStaticCredentialsProvider(creds)).build();
				user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
				userService.createCustomer(user);
				try {
					is = file.getInputStream();
					// save on s3 wont allow me to save with public read access
//		tickets-images-fare
					String imageName = "Image Number " + user.getId();
					s3Client.putObject(new PutObjectRequest("gigzeaze", imageName, is, new ObjectMetadata())
							.withCannedAcl(CannedAccessControlList.PublicRead));

					// Get a refernce to the image Object
					S3Object s3object = s3Client.getObject(new GetObjectRequest("gigzeaze", imageName));

//		//add to a model
					model.addAttribute("picUrl", s3object.getObjectContent().getHttpRequest().getURI().toString());

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				model.addAttribute("msg", "ERROR! Username is taken");
				return "register";
			}
		} else {

			model.addAttribute("msg", "ERROR! Email already exists");
			return "register";
		}

		ticketService.createEventArray(0);
		model.addAttribute("lists", ticketService.getEventList());
		session.setAttribute("user", user);
		return "success";
//		}
//  return null;

	}

	@RequestMapping("/registerPage")
	public String showRegister() {
		return "register";
	}

}
