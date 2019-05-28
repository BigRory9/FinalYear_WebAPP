package com.roryharford.user;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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
import com.roryharford.user.security.BCrypt;

//Starting application starts embedded database apache derby
@Service
public class UserService {

	// links it with the customerRepository
	@Autowired
	private UserRepository customerRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	private ArrayList<Event> list = new ArrayList<Event>();

	public List<User> getAllUsers() {
		// connects to the database and runs a query
		List<User> users = new ArrayList<>();
		// adds each User into the array
		customerRepository.findAll().forEach(users::add);
		return users;
	}
	
	public List<User> getAllUsersForGroup(String email) {
		// connects to the database and runs a query
		List<User> allUsers = this.getAllUsers();
		List<User> users = new ArrayList<>();
		// adds each User into the array
		for(int i=0; i<allUsers.size();i++) {
			if(!allUsers.get(i).getEmail().equals(email))
			{
				users.add(allUsers.get(i));
			}
			
		}
		return users;
	}

	public User getUser(int id) {
		// return Users.stream().filter(t -> t.getId().equals(id)).findFirst().get();
		// It knows the id is a String because we set it in the User class
		return customerRepository.getOne( id);
	}
	
//	public User getUserByEmail(String email) {
//		// return Users.stream().filter(t -> t.getId().equals(id)).findFirst().get();
//		// It knows the id is a String because we set it in the User class
//		return customerRepository.getOne(email);
//	}

	public void addUser(User user) {
		customerRepository.save(user);
	}

	public void updateUser(String id, User user) {
		// A save can update and add a User because the User has information about what
		// it is an a repository can check if it already exists or not.
		customerRepository.save(user);
	}

//	public void deleteUser(int id) {
//		customerRepository.deleteById( id);
//	}

	public User loginCustomer(String email, String password) {
//		List<User> Customers = this.getAllUsers();
		User customer = this.getUserByEmail(email);
			if (customer != null && BCrypt.checkpw(password, customer.getPassword()) && customer.getEmail().equals(email)) {
				return customer;
			}
		return null;
	}

	public User createCustomer(User user) {
//		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//		user.setPassword(encoder.encode(user.getPassword()));
		Role userRole = roleRepository.findByRole("USER");
		user.setActive(1);
	    user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		System.out.println(user.getName());
		this.addUser(user);
		return user;

	}

	public User getUserByEmail(String email) {
		return customerRepository.findUserByEmail(email);
		
	}

//	public void createUser(User user) {
//		user.setPassword(encoder.encode(user.getPassword())); 
//		Role userRole = new Role("USER");
//		List<Role> roles = new ArrayList<>();
//		roles.add(userRole);
//		user.setRoles(roles);
//		userRepository.save(user);
//		}
}
