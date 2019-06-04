package com.roryharford.ticket;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.util.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
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
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.roryharford.event.Event;
import com.roryharford.user.User;

//Starting application starts embedded database apache derby
@Service
public class TicketService {

	// links it with the customerRepository
	@Autowired
	private TicketRepository ticketRepository;

	private ArrayList<Event> list = new ArrayList<Event>();

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


	public void deleteEvent(int id) {
		ticketRepository.deleteById(id);
	}


	public List<String> serchKeyword(String keyword) {
		String id = "";
		list = new ArrayList<Event>();
		ArrayList<String> keywordList = new ArrayList<String>();
		try {

			String url = "https://app.ticketmaster.com/discovery/v2/attractions.json?apikey=thlzRyuDZ6IGtUirirhnDPinG0Sgk2Ay&keyword="
					+ keyword;
			String json = Jsoup.connect(url).ignoreContentType(true).execute().body();
			JSONObject obj = new JSONObject(json);
			JSONObject responseJson = obj.getJSONObject("_embedded");

			JSONArray entriesJSON = responseJson.getJSONArray("attractions");
			for (int i = 0; i < entriesJSON.length(); i++) {
				JsonElement jelement = new JsonParser().parse(entriesJSON.get(i).toString());
				JsonObject jobject = jelement.getAsJsonObject();
				if (!jobject.get("id").equals(null)) {

					id = jobject.get("id").toString();
					id = id.replace("\"", "");
					keywordList.add(id);
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return keywordList;

	}

	public List<Event> createEventArray(String keyword) {

		try {
			TimeUnit.SECONDS.sleep((long) 0.5);
			String url = "https://app.ticketmaster.com/discovery/v2/events.json?attractionId=" + keyword
					+ "&countryCode=IE&apikey=thlzRyuDZ6IGtUirirhnDPinG0Sgk2Ay";
			String json = Jsoup.connect(url).ignoreContentType(true).execute().body();
			JSONObject obj = new JSONObject(json);
			if (obj.toString().contains("_embedded")) {
				JSONObject responseJson = obj.getJSONObject("_embedded");
				JSONArray entriesJSON = responseJson.getJSONArray("events");
				String id = "", name = "", arena = "", date = null, time = "";
				String longitude = "", latitude = "";
				double price = -1;
				String minPrice = "0";
				String maxPrice = "0";
				String imageUrl = "";
				boolean found = false;
				for (int i = 0; i < entriesJSON.length(); i++) {
					JsonElement jelement = new JsonParser().parse(entriesJSON.get(i).toString());
					JsonObject jobject = jelement.getAsJsonObject();
					if (!jobject.get("name").equals(null)) {

						id = jobject.get("id").toString();
						name = jobject.get("name").toString();
						JsonArray imagesArray = jobject.getAsJsonArray("images");
						for (int j = 0; j < imagesArray.size() && found == false; j++) {
							JsonElement imagesElement = new JsonParser().parse(imagesArray.get(j).toString());
							JsonObject imagesObject = imagesElement.getAsJsonObject();
							imageUrl = imagesObject.get("url").toString();
							if (imageUrl.endsWith("RETINA_LANDSCAPE_16_9.jpg\"")) {
								found = true;
							}
							j++;

						}

						found = false;

						JsonObject datesObject = jobject.getAsJsonObject("dates");
						JsonObject object = datesObject.getAsJsonObject("start");
						date = object.get("localDate").toString();
						if (object.has("localTime")) {
							time = object.get("localTime").toString();
						}

						JsonArray priceArray = jobject.getAsJsonArray("priceRanges");
						if (priceArray != null) {

							JsonElement venueElement = new JsonParser().parse(priceArray.get(0).toString());
							JsonObject venueObject = venueElement.getAsJsonObject();
							NumberFormat formatter = new DecimalFormat("#0.00");
							if (venueObject.has("min")) {
								minPrice = venueObject.get("min").toString();
								maxPrice = venueObject.get("max").toString();

								price = Double.parseDouble(formatter
										.format((Double.parseDouble(minPrice) + Double.parseDouble(maxPrice)) / 2));
							} else {
								price = Double.parseDouble(
										formatter.format(Double.parseDouble(venueObject.get("max").toString())));
							}

						}

						JsonObject newobject = jobject.getAsJsonObject("_embedded");
						JsonArray arrayJSON = newobject.getAsJsonArray("venues");
						JsonElement venueElement = new JsonParser().parse(arrayJSON.get(0).toString());
						JsonObject venueObject = venueElement.getAsJsonObject();
						arena = venueObject.get("name").toString();

					}
					if (price != -1) {
						name = name.replace("\"", "");
						id = id.replace("\"", "");
						arena = arena.replace("\"", "");
						date = date.replace("\"", "");
						time = time.replace("\"", "");
						imageUrl = imageUrl.replace("\"", "");
						latitude = latitude.replace("\"", "");
						longitude = longitude.replace("\"", "");

						Event event = new Event(id, arena, name, price, date, time, imageUrl, latitude, longitude);
						list.add(event);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;

	}

	public List<Event> getEventList() {
		return list;
	}

	public Ticket createTicket(Ticket ticket) {

		ticketRepository.save(ticket);
		return ticket;

	}

	public List<Event> createEventArray(int pageNum) {
		try {
			list = new ArrayList<Event>();
			String url = "https://app.ticketmaster.com/discovery/v2/events.json?countryCode=IE&apikey=thlzRyuDZ6IGtUirirhnDPinG0Sgk2Ay&page="
					+ pageNum;
			String json = Jsoup.connect(url).ignoreContentType(true).execute().body();
			JSONObject obj = new JSONObject(json);
			JSONObject responseJson = obj.getJSONObject("_embedded");
//			("IMPORTANT "+responseJson.length());
//			JSONObject feedjson = responseJson.getJSONObject("events);
			JSONArray entriesJSON = responseJson.getJSONArray("events");
//			JSONArray entryJSON = responseJson.getJSONArray("priceRanges");
//			("HEUJDNMK!"+entryJSON.length());
			String id = "", name = "", arena = "", date = null, time = null;
			String longitude = "", latitude = "";
			double price = -1;
			String minPrice = "0";
			String maxPrice = "0";
			String imageUrl = "";
			boolean found = false;
			for (int i = 0; i < entriesJSON.length(); i++) {
//				 if(entriesJSON.get(i).equals("country")) {
//				 ("\n"+entriesJSON.get(i));
//				(i);

				// gson
				JsonElement jelement = new JsonParser().parse(entriesJSON.get(i).toString());

//				JsonElement element = new JsonParser().parse(entryJSON.get(i).toString());

//				("HAY0"+jelement);
				JsonObject jobject = jelement.getAsJsonObject();

				if (!jobject.get("name").equals(null)) {

					id = jobject.get("id").toString();
					name = jobject.get("name").toString();
					JsonArray imagesArray = jobject.getAsJsonArray("images");
					for (int j = 0; j < imagesArray.size() && found == false; j++) {
						JsonElement imagesElement = new JsonParser().parse(imagesArray.get(j).toString());
						JsonObject imagesObject = imagesElement.getAsJsonObject();
						imageUrl = imagesObject.get("url").toString();
						if (imageUrl.endsWith("RETINA_LANDSCAPE_16_9.jpg\"")) {
							found = true;
						}
						j++;

					}

					found = false;

					JsonObject datesObject = jobject.getAsJsonObject("dates");
					JsonObject object = datesObject.getAsJsonObject("start");
					date = object.get("localDate").toString();
					time = object.get("localTime").toString();
					// location

					JsonArray priceArray = jobject.getAsJsonArray("priceRanges");
					if (priceArray != null) {

//					("FOUND !!!"+priceArray.size());
						JsonElement venueElement = new JsonParser().parse(priceArray.get(0).toString());
						JsonObject venueObject = venueElement.getAsJsonObject();
						NumberFormat formatter = new DecimalFormat("#0.00");
//					("PRICE "+venueObject.get("min").toString());
						if (venueObject.has("min")) {
							minPrice = venueObject.get("min").toString();
							maxPrice = venueObject.get("max").toString();

							price = Double.parseDouble(formatter
									.format((Double.parseDouble(minPrice) + Double.parseDouble(maxPrice)) / 2));
						} else {
							price = Double.parseDouble(
									formatter.format(Double.parseDouble(venueObject.get("max").toString())));
						}

					}

					JsonObject newobject = jobject.getAsJsonObject("_embedded");
					JsonArray arrayJSON = newobject.getAsJsonArray("venues");
					JsonElement venueElement = new JsonParser().parse(arrayJSON.get(0).toString());
					JsonObject venueObject = venueElement.getAsJsonObject();
					arena = venueObject.get("name").toString();
					JsonObject latLong = venueObject.getAsJsonObject("location");
					longitude = latLong.get("longitude").toString();
					latitude = latLong.get("latitude").toString();
				}
				if (price != -1) {
					name = name.replace("\"", "");
					id = id.replace("\"", "");
					arena = arena.replace("\"", "");
					date = date.replace("\"", "");
					time = time.replace("\"", "");
					imageUrl = imageUrl.replace("\"", "");
					latitude = latitude.replace("\"", "");
					longitude = longitude.replace("\"", "");

					Event event = new Event(id, arena, name, price, date, time, imageUrl, latitude, longitude);
					list.add(event);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;

	}

	public Event getOneEvent(String id) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId().equals(id)) {
				return list.get(i);
			}
		}
		return null;
	}

	public void createPdf(S3Object s3object, Ticket ticket, User user, HttpServletResponse response) {
		com.itextpdf.text.Document document = new com.itextpdf.text.Document();
		try {
			String pdfName = ticket.getId() + "PDF.pdf";
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(
					"C:\\Users\\roryh\\Documents\\workspace-sts-3.9.6.RELEASE\\login\\src\\main\\webapp\\static\\images\\"
							+ pdfName));

			document.open();
			com.itextpdf.text.Image image = com.itextpdf.text.Image
					.getInstance("http://127.0.0.1:127/images/logo_2.png");
			com.itextpdf.text.Image profile = com.itextpdf.text.Image
					.getInstance(s3object.getObjectContent().getHttpRequest().getURI().toString());
			com.itextpdf.text.Image qrCode = com.itextpdf.text.Image
					.getInstance("http://127.0.0.1:127/images/" + ticket.getId() + ".png");
			image.scalePercent(50);
			image.setAlignment(image.ALIGN_MIDDLE);
			profile.scalePercent(50);
			qrCode.scalePercent(50);
			document.add(image);
			document.add(new Paragraph("Users Name: " + user.getName()));
			document.add(new Paragraph("Ticket : " + ticket.getName()));
			document.add(new Paragraph("Arena : " + ticket.getArena() + "  Date : " + ticket.getDate()));
			document.add(new Paragraph("Price : " + ticket.getPrice()));
			document.add(new Paragraph("       "));
			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100);
			table.setWidths(new int[] { 1, 1 });
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
			BasicAWSCredentials creds = new BasicAWSCredentials("AKIAJVL5I336SYABBB4A",
					"I7gmPoB7tY5bUky5GjLsDijZucjLG/8sngV/UZg6");
			AmazonS3 s3Client = AmazonS3Client.builder().withRegion("eu-west-1")
					.withCredentials(new AWSStaticCredentialsProvider(creds)).build();

			File file = new File(
					"C:\\Users\\roryh\\Documents\\workspace-sts-3.9.6.RELEASE\\login\\src\\main\\webapp\\static\\images\\"
							+ pdfName);

			FileInputStream input = new FileInputStream(file);
			MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain",
					IOUtils.toByteArray(input));
			InputStream is = multipartFile.getInputStream();

			s3Client.putObject(new PutObjectRequest("gigzeaze", pdfName, is, new ObjectMetadata())
					.withCannedAcl(CannedAccessControlList.PublicRead));

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

	public void showPDF(Ticket ticket, HttpServletResponse response) {
		try {
			DefaultResourceLoader loader = new DefaultResourceLoader();
			String name = ticket.getId() + "PDF.pdf";
			InputStream is;

			is = loader.getResource("http://127.0.0.1:127/images/" + name).getInputStream();

			IOUtils.copy(is, response.getOutputStream());
			// attachment if you want to download
			response.setHeader("Content-Disposition", "attachment; filename=" + name);
			response.flushBuffer();
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
