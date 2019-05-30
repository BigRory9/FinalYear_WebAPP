package com.roryharford.group;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.roryharford.ticket.Ticket;
import com.roryharford.user.User;

//Creates Table in DB
@Entity
@Table(name = "friends")
public class Group {

	

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String groupName;
	
	@ManyToOne()
	@JoinColumn(name="user_id", referencedColumnName = "id")    
	private User user;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany
	List<User> friends = new ArrayList<>();
	
	public Group() {

	}
	
	
	public Group(String groupName) {
		super();
		this.groupName = groupName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public List<User> getFriends() {
		return friends;
	}
	public void setFriends(List<User> friends) {
		this.friends = friends;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	



}
