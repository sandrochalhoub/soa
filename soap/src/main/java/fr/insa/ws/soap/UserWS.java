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
		users.add(user);
		System.out.println("Added help requester " + name + " with id: " + user.getID());
		return user.getID();
	}
	
	@WebMethod(operationName="manageVolunteer")
	public String manageVolunteer(String name) {
		Volunteer user = new Volunteer(name);
		users.add(user);
		System.out.println("Added volunteer " + name + " with id: " + user.getID());
		return user.getID();
	}
	
	@WebMethod(operationName="manageAdmin")
	public String manageAdmin(String name) {
		Admin user = new Admin(name);
		users.add(user);
		System.out.println("Added admin " + name + " with ID: " + user.getID());
		return user.getID();
	}
	
	@WebMethod(operationName = "requestHelp")
    public String requestHelp(String userId, String description) {
        // Check the user exists and is a HelpRequester
        User user = getUserById(userId);

        if (user instanceof HelpRequester) {
            Mission mission = new Mission(description);
            missions.add(mission);
            System.out.println("Help request added with ID: " + mission.getID());
            return mission.getID();
        } else {
            System.out.println("User is not a HelpRequester. Cannot request help.");
            return null;
        }
    }
	
	@WebMethod(operationName = "acceptMission")
	public boolean acceptMission(String userId, String missionId) {
	    User user = getUserById(userId);
	    Mission mission = getMissionById(missionId);

	    if (user instanceof Volunteer && mission != null && mission.getStatus() == 0) {
	        mission.setStatus(1); // validée
	        System.out.println("Volunteer accepted mission with ID: " + missionId);
	        return true;
	    } else {
	    	System.out.println(user.getClass());
	    	System.out.println(mission.getStatus());
	        System.out.println("User cannot accept the mission. Check user type or mission status.");
	        return false;
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

    @WebMethod(operationName = "getMissionById")
    public Mission getMissionById(String missionId) {
        for (Mission mission : missions) {
            if (mission.getID().equals(missionId)) {
                return mission;
            }
        }
        return null;
    }
}
