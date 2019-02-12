package com.roryharford.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.roryharford.event.Event;

//Starting application starts embedded database apache derby
@Service
public class UserService {
	
	//links it with the customerRepository
	@Autowired
	private UserRepository customerRepository;
	
	private ArrayList<Event> list = new ArrayList<Event>();
	
	public List<Event> createEventArray () {
		try {
			System.out.println("Concerts at the Olympia");
			String url = "https://api.songkick.com/api/3.0/venues/2761953/calendar.json?apikey=4S0KyVbZMAu9uQfq";
			String json = Jsoup.connect(url).ignoreContentType(true).execute().body();
			JSONObject obj = new JSONObject(json);
			JSONObject responseJson = obj.getJSONObject("resultsPage");
			JSONObject feedjson = responseJson.getJSONObject("results");
			JSONArray entriesJSON = feedjson.getJSONArray("event");
			String id, displayName, arena, date, time;
			for (int i = 0; i < entriesJSON.length(); i++) {
//				 if(entriesJSON.get(i).equals("country")) {
//				 System.out.println("\n"+entriesJSON.get(i));

				// gson
				JsonElement jelement = new JsonParser().parse(entriesJSON.get(i).toString());
//				System.out.println("HAY0"+jelement);
				JsonObject jobject = jelement.getAsJsonObject();
				
				if (!jobject.get("displayName").equals(null)) {
					id = jobject.get("id").toString();
					arena = jobject.get("displayName").toString();
					displayName = jobject.getAsJsonObject("venue").get("displayName").toString();
					jobject = jobject.getAsJsonObject("start");
					date = jobject.get("date").toString();
					time = jobject.get("time").toString();
					
					Event event = new Event(id,displayName,arena,date,time);
					list.add(event);
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
		
	}

	public List<Event> getEventList(){
		return list;
	}
	
	public Event getOneEvent(String id){
		for(int i=0; i<list.size();i++)
		{
			if(list.get(i).getId().equals(id))
			{
				return list.get(i);
			}
		}
		return null;
	}

	public List<User> getAllUsers(){
		//connects to the database and runs a query
		List<User> users = new ArrayList<>();
		//adds each User into the array
		customerRepository.findAll().forEach(users::add);
		return users;
	}
	
	public User getUser(String id) {
	//	return Users.stream().filter(t -> t.getId().equals(id)).findFirst().get();
		//It knows the id is a String because we set it in the User class
		return customerRepository.getOne(id);
	}
	
	public void addUser(User user) {
		customerRepository.save(user);
	}

	public void updateUser(String id, User user) {
	 //A save can update and add a User because the User has information about what it is an a repository can check if it already exists or not.	
		customerRepository.save(user);
	}

	public void deleteUser(String id) {
		customerRepository.deleteById(id);
	}
	
	public User loginCustomer(String email, String password)
	{ 	
		List<User> Customers = this.getAllUsers();
		User customer = new User();
		for(int i=0; i<Customers.size();i++)
		{
		 customer =Customers.get(i);
		if(customer != null && customer.getPassword().equals(password) && customer.getEmail().equals(email)) {
			return customer;
		}
		}
		return null;
	}
	
	public User createCustomer(User customer)
	{ 	
	
		this.addUser(customer);
		return customer;
		
	}
}
