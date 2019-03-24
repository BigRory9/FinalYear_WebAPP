package com.roryharford.ticket;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.model.S3Object;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.roryharford.user.User;

//Starting application starts embedded database apache derby
@Service
public class TicketService {

	// links it with the customerRepository
	@Autowired
	private TicketRepository ticketRepository;

	public List<Ticket> getAllTickets() {
		// connects to the database and runs a query
		List<Ticket> tickets = new ArrayList<>();
		// adds each User into the array
		ticketRepository.findAll().forEach(tickets::add);
		return tickets;
	}

	public Ticket getTicket(int id) {
		// return Users.stream().filter(t -> t.getId().equals(id)).findFirst().get();
		// It knows the id is a String because we set it in the User class
		return ticketRepository.getOne(id);
	}

//	
//	public Customer getUser(String id) {
//	//	return Users.stream().filter(t -> t.getId().equals(id)).findFirst().get();
//		//It knows the id is a String because we set it in the User class
//		return customerRepository.getOne(id);
//	}
//	
//	public void addUser(Customer user) {
//		customerRepository.save(user);
//	}
//
//	public void updateUser(String id, Customer user) {
//	 //A save can update and add a User because the User has information about what it is an a repository can check if it already exists or not.	
//		customerRepository.save(user);
//	}
//
	public void deleteEvent(int id) {
		ticketRepository.deleteById(id);
	}
//	
//	public Customer loginCustomer(String email, String password)
//	{ 	
//		List<Customer> Customers = this.getAllUsers();
//		Customer customer = new Customer();
//		for(int i=0; i<Customers.size();i++)
//		{
//		 customer =Customers.get(i);
//		if(customer != null && customer.getPassword().equals(password) && customer.getEmail().equals(email)) {
//			return customer;
//		}
//		}
//		return null;
//	}

	public Ticket createTicket(Ticket ticket) {

		ticketRepository.save(ticket);
		return ticket;

	}

	public void downloadPdf(S3Object s3object,Ticket ticket,User user,HttpServletResponse response ) {
		com.itextpdf.text.Document document = new com.itextpdf.text.Document();
		try {
			String pdfName = ticket.getId()+"PDF.pdf";
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\roryh\\Documents\\workspace-sts-3.9.6.RELEASE\\login\\src\\main\\webapp\\static\\images\\"+pdfName));

			document.open();
			com.itextpdf.text.Image image = com.itextpdf.text.Image
					.getInstance("http://127.0.0.1:127/images/FYPBRAND.png");
			com.itextpdf.text.Image profile = com.itextpdf.text.Image
					.getInstance(s3object.getObjectContent().getHttpRequest().getURI().toString());
//			System.out.println(base64Encoded);
			com.itextpdf.text.Image qrCode = com.itextpdf.text.Image
					.getInstance("http://127.0.0.1:127/images/"+ticket.getId()+".png");
			image.scalePercent(50);
			image.setAlignment(image.ALIGN_MIDDLE);
			profile.scalePercent(50);
			qrCode.scalePercent(50);
			document.add(image);
			document.add(new Paragraph("Users Name: " + user.getName()));
			document.add(new Paragraph("Ticket : " + ticket.getName()));
			document.add(new Paragraph("       "));
			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100);
			table.setWidths(new int[] { 1,1 });
			table.addCell(createImageCell(profile));
			table.addCell(createImageCell(qrCode));
			// height, width
//			image.setAbsolutePosition(20, 20);  
			document.add(table);
//			document.add(profile);
//			document.add(qrCode);

			document.close();
//			File image = new File(base64Encoded);
//			document.add((Element) image);

			writer.close();

			DefaultResourceLoader loader = new DefaultResourceLoader();
			String name=ticket.getId()+"PDF.pdf";
			InputStream is = loader.getResource("http://127.0.0.1:127/images/"+name).getInputStream();
			IOUtils.copy(is, response.getOutputStream());
			// attachment if you want to download
			response.setHeader("Content-Disposition", "attachment; filename="+name);
			response.flushBuffer();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	public static PdfPCell createImageCell(com.itextpdf.text.Image img) throws DocumentException, IOException {
		PdfPCell cell = new PdfPCell(img, true);
		cell.setBorder(Rectangle.NO_BORDER);
		img.setPaddingTop(5);
		img.scaleAbsoluteHeight(300);
		img.scaleAbsoluteWidth(300);
		img.setScaleToFitHeight(true);
		return cell;
	}

	public static PdfPCell createTextCell(String text) throws DocumentException, IOException {
		PdfPCell cell = new PdfPCell();
		Paragraph p = new Paragraph(text);
		p.setAlignment(Element.ALIGN_RIGHT);
		cell.addElement(p);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
//		cell.setBorder(Rectangle.NO_BORDER);
		return cell;
	}
}
