package com.roryharford.user;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.roryharford.event.Event;

//Starting application starts embedded database apache derby
@Service
public class UserService {

	// links it with the customerRepository
	@Autowired
	private UserRepository customerRepository;

	private ArrayList<Event> list = new ArrayList<Event>();

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
			double price = -1;
			String minPrice="0";
			String maxPrice="0";
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
							
							price = Double.parseDouble(formatter.format((Double.parseDouble(minPrice) + Double.parseDouble(maxPrice)) / 2));
						}
						else
						{
							price = Double.parseDouble(formatter.format(Double.parseDouble(venueObject.get("max").toString())));
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

					// System.out.println("THE NEW NAME "+newName);
					if (name.equals("Eagles")) {
						System.out.println("\nEagles Image: " + imageUrl);
					}
					Event event = new Event(id, arena, name, price, date, time, imageUrl);
					list.add(event);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;

	}

	public List<Event> getEventList() {
		return list;
	}

	public Event getOneEvent(String id) {
		System.out.println("SIZE OF LIST " + list.size());
		System.out.println("SIZE OF ID " + id);
		for (int i = 0; i < list.size(); i++) {
			System.out.println("COMPARE ID " + id + " list ID " + list.get(i).getId());
			if (list.get(i).getId().equals(id)) {
				return list.get(i);
			}
		}
		return null;
	}

	public List<User> getAllUsers() {
		// connects to the database and runs a query
		List<User> users = new ArrayList<>();
		// adds each User into the array
		customerRepository.findAll().forEach(users::add);
		return users;
	}

	public User getUser(String id) {
		// return Users.stream().filter(t -> t.getId().equals(id)).findFirst().get();
		// It knows the id is a String because we set it in the User class
		return customerRepository.getOne(id);
	}

	public void addUser(User user) {
		customerRepository.save(user);
	}

	public void updateUser(String id, User user) {
		// A save can update and add a User because the User has information about what
		// it is an a repository can check if it already exists or not.
		customerRepository.save(user);
	}

	public void deleteUser(String id) {
		customerRepository.deleteById(id);
	}

	public User loginCustomer(String email, String password) {
		List<User> Customers = this.getAllUsers();
		User customer = new User();
		for (int i = 0; i < Customers.size(); i++) {
			customer = Customers.get(i);
			if (customer != null && customer.getPassword().equals(password) && customer.getEmail().equals(email)) {
				return customer;
			}
		}
		return null;
	}

	public User createCustomer(User customer) {

		this.addUser(customer);
		return customer;

	}
}
