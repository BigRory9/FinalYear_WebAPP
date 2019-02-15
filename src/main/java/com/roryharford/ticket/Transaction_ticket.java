package com.roryharford.ticket;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Transaction_ticket")
public class Transaction_ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private int amount;
	private String currency;

//	@OneToMany(fetch = FetchType.EAGER)
//	@JoinColumn(name = "ticket_id")
//	private List<Ticket> tickets = new ArrayList<>();
	
	

	public Transaction_ticket(int amount, String currency) {
		super();
		this.amount = amount;
		this.currency = currency;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

//	public List<Ticket> getTickets() {
//		return tickets;
//	}
//
//	public void setTickets(List<Ticket> tickets) {
//		this.tickets = tickets;
//	}
//
//	public void addTicket(Ticket ticket) {
//		tickets.add(ticket);
//	}

}
