package com.roryharford.event;

import java.util.ArrayList;

public class Event {

	String id;
	String arena;
	String name;
	String date;
	String time;
	double price;
	String imageUrl;

	public Event(String id, String arena, String name, double price, String date, String time,
			String imageUrl) {
		super();
		this.id = id;
		this.arena = arena;
		this.price = price;
		this.name = name;
		this.date = date;
		this.time = time;
		this.imageUrl = imageUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getArena() {
		return arena;
	}

	public void setArena(String arena) {
		this.arena = arena;
	}

	public String getDisplayName() {
		return name;
	}

	public void setDisplayName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", arena=" + arena + ", displayName=" + name + ", date=" + date + ", time=" + time
				+ "]";
	}

}
