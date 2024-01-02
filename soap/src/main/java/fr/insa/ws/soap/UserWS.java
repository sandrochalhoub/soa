package fr.insa.ws.soap;

import javax.jws.*;
import java.util.ArrayList;
import java.util.List;

@WebService(serviceName = "user")
public class UserWS {
    private List<User> users = new ArrayList<User>();
    private List<Mission> missions = new ArrayList<Mission>();
	
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
	
	@WebMethod(operationName = "requestHelp")
    public String requestHelp(String userId, String description) {
        // Check the user exists and is a HelpRequester
        User user = getUserById(userId);

        if (user instanceof HelpRequester) {
            Mission mission = new Mission(description);
            missions.add(mission);
            System.out.println("Help request added with ID: " + mission.getId());
            return mission.getId();
        } else {
            System.out.println("User is not a HelpRequester. Cannot request help.");
            return null;
        }
    }

    @WebMethod(operationName = "getUserById")
    public User getUserById(String userId) {
        for (User user : users) {
            if (user.getID().equals(userId)) {
                return user;
            }
        }
        return null;
    }
}
