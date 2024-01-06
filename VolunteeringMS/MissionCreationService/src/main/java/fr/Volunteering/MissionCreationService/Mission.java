package fr.Volunteering.MissionCreationService;

public class Mission {
	static int availableID = 0;
	
	private int id;
	private String description;
	private int helpSeekerID;
	private int volunteerID;
	private int status;
	private String fbSeeker;
	private String fbVolunteer;
	
	public Mission() {
	}
	
	public Mission(int ID, String description, boolean createdByVolunteer) {
		this.id = availableID++;
		this.description = description;
		if (createdByVolunteer) {
			this.volunteerID = ID;
		}
		else {
			this.helpSeekerID = ID;
		}
	}
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getHelpSeekerID() {
		return helpSeekerID;
	}
	
	public void setHelpSeekerID(int helpSeekerID) {
		this.helpSeekerID = helpSeekerID;
	}
	
	public int getVolunteerID() {
		return volunteerID;
	}
	
	public void setVolunteerID(int volunteerID) {
		this.volunteerID = volunteerID;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getFbSeeker() {
		return fbSeeker;
	}
	
	public void setFbSeeker(String fbSeeker) {
		this.fbSeeker = fbSeeker;
	}
	
	public String getFbVolunteer() {
		return fbVolunteer;
	}
	
	public void setFbVolunteer(String fbVolunteer) {
		this.fbVolunteer = fbVolunteer;
	}	
}
