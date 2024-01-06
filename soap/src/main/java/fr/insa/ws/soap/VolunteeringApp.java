package fr.insa.ws.soap;

import java.net.MalformedURLException;
import javax.xml.ws.Endpoint;
import javafx.application.Application;

public class VolunteeringApp {
	
	public static String host = "localhost";
	public static short port = 8080;
	
	public void demarrerService() {
		String url = "http://" + host + ":" + port + "/";
		Endpoint.publish(url, new UserWS());
	}
	
	public static void main(String[] args) throws MalformedURLException {
		new VolunteeringApp().demarrerService();
		System.out.println("Service started.");
		Application.launch(GUI.class, args);
	}
}
