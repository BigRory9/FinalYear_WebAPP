package com.roryharford.ticket;

import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
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

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
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

	//deleted model user attribute
	@RequestMapping(value = "/purchase-tickets", method = RequestMethod.GET)
	public String purchase(@RequestParam("id") String id, HttpServletResponse httpServletResponse, HttpSession session, Model model) {
	//	System.out.println(id);
		model.addAttribute("id", id);
		return "creditCard";
	}

	//deleted model user attribute
	@RequestMapping(value = "/purchase-tickets/process", method = RequestMethod.POST)
	public String process(@RequestParam("id") String id, HttpServletResponse httpServletResponse, HttpSession session, Model model, HttpServletResponse response) throws AuthenticationException,
			InvalidRequestException, APIConnectionException, CardException, APIException, WriterException, IOException {
		System.out.println(id);
		System.out.println("HELLO");
		Event event = userService.getOneEvent(id);
		BasicAWSCredentials creds = new BasicAWSCredentials("AKIAI5BANVNXM3EHHWMQ",
				"vVsj1Kd+iQ0LKyOgSuS5PVM8vJ00fdGMll1jCc6r");
		AmazonS3 s3Client = AmazonS3Client.builder().withRegion("eu-west-1")
				.withCredentials(new AWSStaticCredentialsProvider(creds)).build();
		
		User user = (User) session.getAttribute("user");
		String imageName = "Image Number " + user.getId();
		

		// Get a refernce to the image Object
		S3Object s3object = s3Client.getObject(new GetObjectRequest("tickets-images-fare", imageName));

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
		
		System.out.println(imageName);
		Ticket ticket = new Ticket(event.getDisplayName(), event.getArena(), event.getDate(), event.getPrice(),
				event.getTime(),base64Encoded.toString());
		
		System.out.println(user.getName());
		ticketService.createTicket(ticket);
		user.addTicket(ticket);
		userService.updateUser(id, user);
		String name = ticket.getId()+".png";
		System.out.println("IMAGES NAME: "+name);
		generateQRCodeImage(input, 300, 300,name);
		

		model.addAttribute("name", user.getName());
		model.addAttribute("ticket_id", ticket.getId());
		model.addAttribute("eventName", event.getDisplayName());
		Stripe.apiKey = "sk_test_UIcZ6w9lltQi6Vn5VlDCtRk5";
		Map<String, Object> chargeParams = new HashMap<String, Object>();
		double price = (event.getPrice()*100);
		int eventPrice =(int) Math.round(price);
		chargeParams.put("amount", eventPrice );
		chargeParams.put("currency", "eur");
		chargeParams.put("description", event.getDisplayName());
		chargeParams.put("source", "tok_amex");
		Charge.create(chargeParams);
		

		return "display_ticket";
	}

	@RequestMapping(value = "/download-PDF/{ticket-id}", method = RequestMethod.GET)
	public void downloadPDF(@PathVariable("ticket-id") String id, HttpServletResponse response, HttpSession session) {
		User user = (User) session.getAttribute("user");
		Ticket ticket =ticketService.getTicket(Integer.parseInt(id));
		String imageName = "Image Number " + user.getId();
		BasicAWSCredentials creds = new BasicAWSCredentials("AKIAI5BANVNXM3EHHWMQ",
				"vVsj1Kd+iQ0LKyOgSuS5PVM8vJ00fdGMll1jCc6r");
		AmazonS3 s3Client = AmazonS3Client.builder().withRegion("eu-west-1")
				.withCredentials(new AWSStaticCredentialsProvider(creds)).build();
		// Get a refernce to the image Object
		S3Object s3object = s3Client.getObject(new GetObjectRequest("tickets-images-fare", imageName));
		ticketService.downloadPdf(s3object,ticket,user,response);
		
		
		
	}

	private byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

		ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
		byte[] pngData = pngOutputStream.toByteArray();
		return pngData;
	}

	private static void generateQRCodeImage(String text, int width, int height,String name) throws WriterException, IOException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

		Path path = FileSystems.getDefault().getPath(
				"C:\\Users\\roryh\\Documents\\workspace-sts-3.9.6.RELEASE\\login\\src\\main\\webapp\\static\\images\\"+name);
		MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
	}

	
}
