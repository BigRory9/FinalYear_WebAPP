package com.roryharford.event;

public class Event {

	String id;
	String arena;
	String displayName;
	String date;
	String time;
	int price;
	
	public Event(String id, String arena, String displayName,int price, String date, String time) {
		super();
		this.id = id;
		this.arena = arena;
		this.price = price;
		this.displayName = displayName;
		this.date = date;
		this.time = time;
	}
	
	public int getPrice() {
		return price;
	}


	public void setPrice(int price) {
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
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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
		return "Event [id=" + id + ", arena=" + arena + ", displayName=" + displayName + ", date=" + date + ", time="
				+ time + "]";
	}
	
	
	
}
