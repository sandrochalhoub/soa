package fr.insa.ws.rest.VolunteeringAppRest;

public class Link {
	private String uri; 
	private String rel; 
	private String method; 
	
	public String getUri() {
		return uri; 
	}
	
	public void setUri(String uri) {
		this.uri = uri; 
	}
	
	public String getRel() {
		return rel; 
	}
	
	public void setRel(String rel) {
		this.rel = rel; 
	}
	
	public String getMethod() {
		return method; 
	}
	
	public void setMethod(String method) {
		this.method = method; 
	}

}