package com.roryharford.ticket;

import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.roryharford.event.Event;
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

//	@RequestMapping(value = "/tickets")
//	public String countsList(Model model) {
//	    model.addAttribute("tickets", ticketService.getAllTickets());
//	    return "tickets";
//	}

	@RequestMapping(value = "/purchase-tickets/{id}", method = RequestMethod.GET)
	public String purchase(@PathVariable("id") String id, HttpServletResponse httpServletResponse, HttpSession session,
			@ModelAttribute User customer) {
		return "creditCard";
	}

	@RequestMapping(value = "/purchase-tickets/{id}/process", method = RequestMethod.POST)
	public String process(@PathVariable("id") String id, HttpServletResponse httpServletResponse, HttpSession session,
			@ModelAttribute User customer, Model model) throws AuthenticationException, InvalidRequestException,
			APIConnectionException, CardException, APIException, WriterException, IOException {
		System.out.println(id);
		Event event = userService.getOneEvent(id);
		Ticket ticket = new Ticket(event.getDisplayName(), event.getArena(), event.getDate(), event.getPrice(),
				event.getTime());
		User user = (User) session.getAttribute("user");
		System.out.println(user.getName());
		ticketService.createTicket(ticket);
		user.addTicket(ticket);
		userService.updateUser(id, user);

		String imageName = "Image Number " + user.getId();

		BasicAWSCredentials creds = new BasicAWSCredentials("AKIAI5BANVNXM3EHHWMQ",
				"vVsj1Kd+iQ0LKyOgSuS5PVM8vJ00fdGMll1jCc6r");
		AmazonS3 s3Client = AmazonS3Client.builder().withRegion("eu-west-1")
				.withCredentials(new AWSStaticCredentialsProvider(creds)).build();

		// Get a refernce to the image Object
		S3Object s3object = s3Client.getObject(new GetObjectRequest("tickets-images-fare", imageName));

		
		// add to a model
		model.addAttribute("picUrl", s3object.getObjectContent().getHttpRequest().getURI().toString());
		//Image card = SwingFXUtils.toFXImage(tempCard, null );
		byte[] bytes = getQRCodeImage("Event Name: "+event.getDisplayName()+"\n Users Photo "
				+s3object.getObjectContent().getHttpRequest().getURI().toString(), 300, 300);
		byte[] encodeBase64 = Base64.encodeBase64(bytes);
		String base64Encoded = new String(encodeBase64, "UTF-8");
		// adding QR Code,
		model.addAttribute("QRcode", base64Encoded);
		System.out.println(imageName);

		model.addAttribute("name", user.getName());
		model.addAttribute("eventName", event.getDisplayName());
		Stripe.apiKey = "sk_test_UIcZ6w9lltQi6Vn5VlDCtRk5";
		Map<String, Object> chargeParams = new HashMap<String, Object>();
		chargeParams.put("amount", (event.getPrice() * 100));
		chargeParams.put("currency", "eur");
		chargeParams.put("description", event.getDisplayName());
		chargeParams.put("source", "tok_amex");
		Charge.create(chargeParams);
//		Transaction_ticket t = new Transaction_ticket(event.getPrice(), "eur");
//		ticketService.addTransaction();
		return "display_ticket";
	}

	private byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

		ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
		byte[] pngData = pngOutputStream.toByteArray();
		return pngData;
	}

}
