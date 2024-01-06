package fr.Volunteering.UserCreationService;

public class User {
	static int availableID = 0;
	
	private String name;
	private int id;
	
	public User(String name) {
		this.name = name;
		this.id = availableID++;
	}
	
	public User() {
		
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	public int getID() {
		return id;
	}
}
