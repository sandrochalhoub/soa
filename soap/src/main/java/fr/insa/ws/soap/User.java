package fr.insa.ws.soap;

import java.util.UUID;

public class User {
	
	private String name;
	private String id;
	
	public User(String name) {
		this.name = name;
		this.id = UUID.randomUUID().toString();
	}
	
	public String getName() {
		return this.name;
	}

	public String getID() {
		return this.id;
	}
}
