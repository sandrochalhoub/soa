package fr.insa.ws.rest.VolunteeringAppRest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("user")
public class UserResource {

    private static List<User> users = new ArrayList<>();
    private static List<Mission> missions = new ArrayList<>();

    @POST
    @Path("helprequester/{name}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public User manageHelpRequester(@PathParam("name") String name) {
        HelpRequester user = new HelpRequester(name);
        users.add(user);
        System.out.println("Added help requester " + name + " with id: " + user.getID());
        return user;
    }

    @POST
    @Path("volunteer/{name}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public User manageVolunteer(@PathParam("name") String name) {
        Volunteer user = new Volunteer(name);
        users.add(user);
        System.out.println("Added volunteer " + name + " with id: " + user.getID());
        return user;
    }

    @POST
    @Path("admin/{name}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public User manageAdmin(@PathParam("name") String name) {
        Admin user = new Admin(name);
        users.add(user);
        System.out.println("Added admin " + name + " with ID: " + user.getID());
        return user;
    }
    
    @POST
    @Path("request/{userId}/{description}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Mission requestHelp(@PathParam("userId") String userId, @PathParam("description") String description) {
        // Check the user exists and is a HelpRequester
        User user = getUserById(userId);

        if (user instanceof HelpRequester) {
            Mission mission = new Mission(description);
            missions.add(mission);
            System.out.println("Help request added with ID: " + mission.getID());
            return mission;
        } else if (user != null ){
            System.out.println("User " + user.getName() + " is not a HelpRequester. Cannot request help.");
            return null;
        }
        else {
            System.out.println("User with ID  " + userId + "  does not exist.");
            return null;
        }
    }
    
    @PUT
    @Path("accept/{userId}/{missionId}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Mission acceptMission(@PathParam("userId") String userId, @PathParam("missionId") String missionId) {
        // Implement the logic to handle mission acceptance
        User user = getUserById(userId);
        Mission mission = getMissionById(missionId);

        if (user instanceof Volunteer && mission != null && mission.getStatus() == 0) {
            mission.setStatus(1); // validée
            System.out.println("Volunteer " + user.getName() + " accepted mission with ID: " + missionId);
            return mission;
        } else if (user != null) {
            System.out.println("User " + user.getName() + "  cannot accept the mission. User has to be of type Volunteer.");
            return null;
        } else if (mission.getStatus() != 0) {
            System.out.println("Mission is not waiting to be validated. Current mission status: " + mission.getStatus());
            return null;
        } else {
            System.out.println("User with ID  " + userId + "  does not exist.");
            return null;
        }
    }

    @GET
    @Path("{userId}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserById(@PathParam("userId") String userId) {
        for (User user : users) {
            if (user.getID().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    @GET
    @Path("mission/{missionId}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Mission getMissionById(@PathParam("missionId") String missionId) {
        for (Mission mission : missions) {
            if (mission.getID().equals(missionId)) {
                return mission;
            }
        }
        return null;
    }
}
