package com.roryharford.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.roryharford.ticket.Ticket;

//Creates Table in DB
@Entity
@Table(name="user")
public class User {

	//Marks Id as Primary key
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private int id;
	private String username;
	private String email;
	private String password;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name="user_id")
	private List<Ticket> tickets = new ArrayList<>();
	
	
	public User() {
		
	}
	
	
	

	public User( String name, String email, String password) {
		super();
		this.username = name;
		this.email = email;
		this.password = password;
	}




	public int getId() {
		return id;
	}




	public void setId(int id) {
		this.id = id;
	}




	public String getEmail() {
		return email;
	}




	public void setEmail(String email) {
		this.email = email;
	}




	public String getName() {
		return username;
	}
	public void setName(String name) {
		this.username = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void addTicket(Ticket ticket) {
		tickets.add(ticket);
	}
	
	
}
