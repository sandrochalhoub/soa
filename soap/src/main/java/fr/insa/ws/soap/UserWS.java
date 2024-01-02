package fr.insa.ws.soap;

import javax.jws.*;

@WebService(serviceName="user")
public class UserWS {
	
	@WebMethod(operationName="manageHelpRequester")
	public String manageHelpRequester(String name) {
		HelpRequester user = new HelpRequester(name);
		System.out.println("Added help requester with id: " + user.getID());
		return user.getID();
	}
	
	@WebMethod(operationName="manageVolunteer")
	public String manageVolunteer(String name) {
		Volunteer user = new Volunteer(name);
		System.out.println("Added volunteer with id: " + user.getID());
		return user.getID();
	}
	
	@WebMethod(operationName="manageAdmin")
	public String manageAdmin(String name) {
		Admin user = new Admin(name);
		System.out.println("Added admin with ID: " + user.getID());
		return user.getID();
	}
}
