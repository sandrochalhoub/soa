package fr.insa.ws.rest.VolunteeringAppRest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

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
    public Response manageHelpRequester(@PathParam("name") String name, @Context UriInfo uriInfo) {
        HelpRequester user = new HelpRequester(name);
        users.add(user);
        System.out.println("Added help requester " + name + " with id: " + user.getID());
        
        // Construct HATEOAS links
        String baseUri = uriInfo.getBaseUriBuilder().path(UserResource.class).build().toString();
        user.addLink(createLink(baseUri, "/" + user.getID(), "self", "GET"));
        user.addLink(createLink(baseUri, "/request/" + user.getID() + "/{description}", "requestHelp", "POST"));

        return Response.ok().entity(user).build();
    }

    @POST
    @Path("volunteer/{name}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response manageVolunteer(@PathParam("name") String name, @Context UriInfo uriInfo) {
        Volunteer user = new Volunteer(name);
        users.add(user);
        System.out.println("Added volunteer " + name + " with id: " + user.getID());
        
        // Construct HATEOAS links
        String baseUri = uriInfo.getBaseUriBuilder().path(UserResource.class).build().toString();
        user.addLink(createLink(baseUri, "/" + user.getID(), "self", "GET"));
        user.addLink(createLink(baseUri, "/accept/" + user.getID() + "/{missionID}", "acceptMission", "POST"));

        return Response.ok().entity(user).build();
    }

    @POST
    @Path("admin/{name}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response manageAdmin(@PathParam("name") String name, @Context UriInfo uriInfo) {
        Admin user = new Admin(name);
        users.add(user);
        System.out.println("Added admin " + name + " with ID: " + user.getID());
        
        // Construct HATEOAS links
        String baseUri = uriInfo.getBaseUriBuilder().path(UserResource.class).path(user.getID()).build().toString();
        user.addLink(createLink(baseUri, "", "self", "GET"));

        return Response.ok().entity(user).build();
    }
    
    @POST
    @Path("request/{userId}/{description}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Mission requestHelp(@PathParam("userId") String userId, @PathParam("description") String description, @Context UriInfo uriInfo) {
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
    
    private Link createLink(String baseUri, String path, String rel, String method) {
        Link link = new Link();
        link.setUri(baseUri + path);
        link.setRel(rel);
        link.setMethod(method);
        return link;
    }
}
