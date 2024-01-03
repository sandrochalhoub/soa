package fr.insa.ws.rest.VolunteeringAppRest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
	
	private String name;
	private String id;
    private List<Link> links;
	
	public User(String name) {
		this.name = name;
		this.id = UUID.randomUUID().toString();
		this.links = new ArrayList<>();
	}
	
	public String getName() {
		return this.name;
	}

	public String getID() {
		return this.id;
	}
	
	public List<Link> getLinks() {
        return this.links;
    }

    public void addLink(Link link) {
        this.links.add(link);
    }
}
