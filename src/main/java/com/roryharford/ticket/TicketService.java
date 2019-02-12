package com.roryharford.ticket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roryharford.user.User;

//Starting application starts embedded database apache derby
@Service
public class TicketService {
	
	//links it with the customerRepository
	@Autowired
	private TicketRepository ticketRepository;


	public List<Ticket> getAllTickets(){
		//connects to the database and runs a query
		List<Ticket> tickets = new ArrayList<>();
		//adds each User into the array
		ticketRepository.findAll().forEach(tickets::add);
		return tickets;
	}
	
	public Ticket getTicket(int id) {
		//	return Users.stream().filter(t -> t.getId().equals(id)).findFirst().get();
			//It knows the id is a String because we set it in the User class
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
	
	public Ticket createTicket(Ticket ticket)
	{ 	
	
		ticketRepository.save(ticket);
		return ticket;
		
	}
}
