package com.roryharford.ticket;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.roryharford.event.Event;
import com.roryharford.group.Group;
import com.roryharford.group.GroupService;
import com.roryharford.user.User;
import com.roryharford.user.UserService;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;

@Controller
public class TicketController {

	private static final String QR_CODE_IMAGE_PATH = "./MyQRCode.png";

	@Autowired
	private TicketService ticketService;

	@Autowired
	private UserService userService;

	@Autowired
	private GroupService groupService;



	@RequestMapping(value = "/login/{pageNum}", method = RequestMethod.GET)
	public String next(@RequestParam("pageNum") String pageNum, Model model) {
		int number = Integer.parseInt(pageNum);
		model.addAttribute("lists", ticketService.createEventArray((number - 1)));
		return "success";
	}

	@RequestMapping(value = "/searchEvents", method = RequestMethod.GET)
	public String searchEvents(@RequestParam("keyword") String keyword, Model model, HttpSession session) {
		try {
			
			List<String> listOfIds = ticketService.serchKeyword(keyword);		
			for (int i = 0; i < listOfIds.size(); i++) {
				TimeUnit.SECONDS.sleep((long) 0.5);
				ticketService.createEventArray(listOfIds.get(i));
			}
			model.addAttribute("lists", ticketService.getEventList());
		} catch (InterruptedException e) {
		}
		return "success";

	}

	@RequestMapping(value = "/viewTickets", method = RequestMethod.GET)
	public String viewTickets(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		List<Ticket> tickets = user.getTickets();
		List<Ticket> userTickets = new ArrayList<>();
		Date todayDate = new Date();
		for (int i = 0; i < tickets.size(); i++) {
			try {
				SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
				String ticketDate = tickets.get(i).getDate();
				ticketDate = ticketDate.replace("\"", "");
				Date date = inputFormat.parse(ticketDate);
				todayDate = inputFormat.parse(inputFormat.format(new Date()));
				if (todayDate.before(date) || todayDate.equals(date)) {
					userTickets.add(tickets.get(i));
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		model.addAttribute("lists", userTickets);
		return "usersTickets";
	}

	// deleted model user attribute
	@RequestMapping(value = "/purchase-tickets/process", method = RequestMethod.POST)
	public String process(@RequestParam("id") String id, HttpServletResponse httpServletResponse, HttpSession session,
			Model model, HttpServletResponse response) throws AuthenticationException, InvalidRequestException,
			APIConnectionException, CardException, APIException, WriterException, IOException {
		Event event = ticketService.getOneEvent(id);
		BasicAWSCredentials creds = new BasicAWSCredentials("AKIAJVL5I336SYABBB4A",
				"I7gmPoB7tY5bUky5GjLsDijZucjLG/8sngV/UZg6");
		AmazonS3 s3Client = AmazonS3Client.builder().withRegion("eu-west-1")
				.withCredentials(new AWSStaticCredentialsProvider(creds)).build();

		User user = (User) session.getAttribute("user");
		String imageName = "Image Number " + user.getId();

		// Get a refernce to the image Object
		S3Object s3object = s3Client.getObject(new GetObjectRequest("gigzeaze", imageName));

		// add to a model
		model.addAttribute("picUrl", s3object.getObjectContent().getHttpRequest().getURI().toString());
		// Image card = SwingFXUtils.toFXImage(tempCard, null );
		String input = "Event Name: " + event.getDisplayName() + "\n Users Photo "
				+ s3object.getObjectContent().getHttpRequest().getURI().toString();
		byte[] bytes = getQRCodeImage(input, 300, 300);

		byte[] encodeBase64 = Base64.encodeBase64(bytes);
		String base64Encoded = new String(encodeBase64, "UTF-8");
		// adding QR Code,
		model.addAttribute("QRcode", base64Encoded);

		Ticket ticket = new Ticket(event.getDisplayName(), event.getArena(), event.getDate(), event.getPrice(),
				event.getTime(), event.getLongitude(), event.getLatitude());

		ticketService.createTicket(ticket);
		user.addTicket(ticket);
		userService.updateUser(id, user);
		String name = ticket.getId() + ".png";
		generateQRCodeImage(input, 300, 300, name);

		model.addAttribute("name", user.getName());
		model.addAttribute("ticket_id", ticket.getId());
		model.addAttribute("event", event);
		Stripe.apiKey = "sk_test_UIcZ6w9lltQi6Vn5VlDCtRk5";
		Map<String, Object> chargeParams = new HashMap<String, Object>();
		double price = (event.getPrice() * 100);
		int eventPrice = (int) Math.round(price);
		chargeParams.put("amount", eventPrice);
		chargeParams.put("currency", "eur");
		chargeParams.put("description", event.getDisplayName());
		chargeParams.put("source", "tok_amex");
		Charge.create(chargeParams);
		ticketService.createPdf(s3object, ticket, user, response);

		return "display_ticket";
	}

	@RequestMapping(value = "/purchase-tickets/process-group", method = RequestMethod.POST)
	public String processforgroup(@RequestParam("group") String groupid,@RequestParam("id") String id, HttpServletResponse httpServletResponse,
			HttpSession session, Model model, HttpServletResponse response) throws AuthenticationException,
			InvalidRequestException, APIConnectionException, CardException, APIException, WriterException, IOException {

		// Getting the event
		Event event = ticketService.getOneEvent(id);
		// getting User from session will have to get this users group and loop around
		User user = (User) session.getAttribute("user");

		// creating credentials for AWS
		BasicAWSCredentials creds = new BasicAWSCredentials("AKIAJVL5I336SYABBB4A",
				"I7gmPoB7tY5bUky5GjLsDijZucjLG/8sngV/UZg6");
		AmazonS3 s3Client = AmazonS3Client.builder().withRegion("eu-west-1")
				.withCredentials(new AWSStaticCredentialsProvider(creds)).build();

		// gets image name to get it off AWS
		String imageName = "Image Number " + user.getId();
		// Get a reference to the image Object
		S3Object s3object = s3Client.getObject(new GetObjectRequest("gigzeaze", imageName));
		// add to a model
		model.addAttribute("picUrl", s3object.getObjectContent().getHttpRequest().getURI().toString());

		// gets input for QR code
		String input = "Event Name: " + event.getDisplayName() + "\n Users Photo "
				+ s3object.getObjectContent().getHttpRequest().getURI().toString();

		// creates ticket
		Ticket ticket = new Ticket(event.getDisplayName(), event.getArena(), event.getDate(), event.getPrice(),
				event.getTime(), event.getLongitude(), event.getLatitude());
		// creates QR code
		byte[] bytes = getQRCodeImage(input, 300, 300);

		byte[] encodeBase64 = Base64.encodeBase64(bytes);
		String base64Encoded = new String(encodeBase64, "UTF-8");

		// adding QR Code,
		model.addAttribute("QRcode", base64Encoded);

		// creates ticket and adds it to the user
		ticketService.createTicket(ticket);
		user.addTicket(ticket);
		userService.updateUser(id, user);

		String name = ticket.getId() + ".png";
		generateQRCodeImage(input, 300, 300, name);

		model.addAttribute("name", user.getName());
		model.addAttribute("ticket_id", ticket.getId());
		model.addAttribute("event", event);

		// Creates PDF and uploads it to the S3 Bucket
		ticketService.createPdf(s3object, ticket, user, response);
		
		Group group = groupService.getGroup(Integer.parseInt(groupid));

		// charges the card to Spring
		Stripe.apiKey = "sk_test_UIcZ6w9lltQi6Vn5VlDCtRk5";
		Map<String, Object> chargeParams = new HashMap<String, Object>();
		double price = (event.getPrice() * 100);
		price = price * (group.getFriends().size()+1);
		int eventPrice = (int) Math.round(price);
		chargeParams.put("amount", eventPrice);
		chargeParams.put("currency", "eur");
		chargeParams.put("description", event.getDisplayName());
		chargeParams.put("source", "tok_amex");
		Charge.create(chargeParams);

		//// attempting to get tickets for the users in the group the group will be
		//// hardcoded for now

		
		for (int i = 0; i < group.getFriends().size(); i++) {
			// creates ticket
			Ticket new_ticket = new Ticket(event.getDisplayName(), event.getArena(), event.getDate(), event.getPrice(),
					event.getTime(), event.getLongitude(), event.getLatitude());
			User friend = group.getFriends().get(i);
			ticketService.createTicket(new_ticket);
			friend.addTicket(new_ticket);
			userService.updateUser(id, friend);

			// gets image name to get it off AWS
			imageName = "Image Number " + friend.getId();
			// Get a reference to the image Object
			s3object = s3Client.getObject(new GetObjectRequest("gigzeaze", imageName));

			// gets input for QR code
			 input = "Event Name: " + event.getDisplayName() + "\n Users Photo "
					+ s3object.getObjectContent().getHttpRequest().getURI().toString();
			 
			  name = new_ticket.getId() + ".png";
				generateQRCodeImage(input, 300, 300, name);
				// Creates PDF and uploads it to the S3 Bucket
				ticketService.createPdf(s3object, new_ticket, friend, response);

		}

		return "display_ticket";
	}

	@RequestMapping(value = "/purchase-tickets-for-group", method = RequestMethod.GET)
	public String leadToPurchasePageForGroup(@RequestParam("id") String id, HttpServletResponse httpServletResponse,
			HttpSession session, Model model, HttpServletResponse response) {

		Event event = ticketService.getOneEvent(id);
		User user = (User) session.getAttribute("user");
		List<Group> usersGroups = groupService.getAllUsersGroups(user.getId());


		model.addAttribute("groups", usersGroups);
		model.addAttribute("list", event);
		return "purchaseGroup";
	}

	@RequestMapping(value = "/download-PDF/{ticket-id}", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> downloadPDF(@PathVariable("ticket-id") String id,
			HttpServletResponse response, HttpSession session) {
		try { // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User user = (User) session.getAttribute("user");
			Ticket ticket = ticketService.getTicket(Integer.parseInt(id));
			String imageName = ticket.getId() + "PDF.pdf";
			BasicAWSCredentials creds = new BasicAWSCredentials("AKIAJVL5I336SYABBB4A",
					"I7gmPoB7tY5bUky5GjLsDijZucjLG/8sngV/UZg6");
			AmazonS3 s3Client = AmazonS3Client.builder().withRegion("eu-west-1")
					.withCredentials(new AWSStaticCredentialsProvider(creds)).build();
			// Get a refernce to the image Object
			S3Object s3object = s3Client.getObject(new GetObjectRequest("gigzeaze", imageName));
			response.setHeader("Content-Disposition", "attachment; filename=" + imageName);
			S3ObjectInputStream s3is = s3object.getObjectContent();

			response.flushBuffer();
			return ResponseEntity.ok().contentType(org.springframework.http.MediaType.APPLICATION_PDF)
					.cacheControl(CacheControl.noCache())
					.header("Content-Disposition", "attachment; filename=" + imageName)
					.body(new InputStreamResource(s3is));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ticketService.downloadPdf(s3object, ticket, user, response);
		return null;

	}

	private byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

		ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
		byte[] pngData = pngOutputStream.toByteArray();
		return pngData;
	}

	private static void generateQRCodeImage(String text, int width, int height, String name)
			throws WriterException, IOException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

		Path path = FileSystems.getDefault().getPath(
				"C:\\Users\\roryh\\Documents\\workspace-sts-3.9.6.RELEASE\\login\\src\\main\\webapp\\static\\images\\"
						+ name);
		MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
	}

}
