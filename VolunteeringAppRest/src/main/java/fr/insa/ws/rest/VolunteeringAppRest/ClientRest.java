package fr.insa.ws.rest.VolunteeringAppRest;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class ClientRest {

    private static final String BASE_URI = "http://localhost:8080/VolunteeringAppRest/webapi/";

    public static void main(String[] args) {
        // Create a JAX-RS client
        Client client = ClientBuilder.newClient();

        // Example: Sending a POST request to create a HelpRequester
        String helpRequesterId = createUser(client, "helprequester", "Alice");
        System.out.println("HelpRequester ID: " + helpRequesterId);

        // Example: Sending a POST request to create a Volunteer
        String volunteerId = createUser(client, "volunteer", "Bob");
        System.out.println("Volunteer ID: " + volunteerId);

        // Example: Sending a POST request to create an Admin
        String adminId = createUser(client, "admin", "AdminUser");
        System.out.println("Admin ID: " + adminId);

        // Example: Sending a POST request to request help
        String helpRequestId = requestHelp(client, helpRequesterId, "I need assistance");
        System.out.println("HelpRequest ID: " + helpRequestId);

        // Example: Sending a PUT request to accept a mission
        String acceptMissionResult = acceptMission(client, volunteerId, "mission123");
        System.out.println("Accept Mission Result: " + acceptMissionResult);

        // Close the client
        client.close();
    }

    private static String createUser(Client client, String userType, String name) {
        String resourcePath = "user/" + userType + "/" + name;
        Response response = client.target(BASE_URI + resourcePath)
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.text(""), Response.class);

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(String.class);
        } else {
            System.out.println("Error creating user: " + response.getStatusInfo().getReasonPhrase());
            return null;
        }
    }

    private static String requestHelp(Client client, String userId, String description) {
        String resourcePath = "mission/request/" + userId + "/" + description;
        Response response = client.target(BASE_URI + resourcePath)
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.json(""), Response.class);

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(String.class);
        } else {
            System.out.println("Error requesting help: " + response.getStatusInfo().getReasonPhrase());
            return null;
        }
    }

    private static String acceptMission(Client client, String userId, String missionId) {
        String resourcePath = "mission/accept/" + userId + "/" + missionId;
        Response response = client.target(BASE_URI + resourcePath)
                .request(MediaType.TEXT_PLAIN)
                .put(Entity.text(""), Response.class);

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(String.class);
        } else {
            System.out.println("Error accepting mission: " + response.getStatusInfo().getReasonPhrase());
            return null;
        }
    }
}
