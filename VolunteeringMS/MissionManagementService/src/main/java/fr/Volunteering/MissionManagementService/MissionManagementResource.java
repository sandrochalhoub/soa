package fr.Volunteering.MissionManagementService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/missions")
public class MissionManagementResource {
	@Autowired
	private RestTemplate restTemplate;
	
	@PutMapping(value="/volunteerForMission/{volunteer}/{mission}", produces=MediaType.APPLICATION_JSON_VALUE)
	public String volunteerForMission(@PathVariable int volunteer, @PathVariable int mission) {
	    String dbHost = restTemplate.getForObject("http://ConfServ/conf/db/host", String.class);
	    String dbPort = restTemplate.getForObject("http://ConfServ/conf/db/port", String.class);

	    try (Connection conn = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/projet_gei_067", "projet_gei_067", "Naif2zai")) {
	        // Check if there is already a volunteer for the mission
	    	PreparedStatement checkStmt = conn.prepareStatement("SELECT volunteer, status FROM missions WHERE id = ?;");
	        checkStmt.setInt(1, mission);
	        ResultSet checkResult = checkStmt.executeQuery();

	        while (checkResult.next()) {
	            int existingVolunteer = checkResult.getInt("volunteer");
	            int missionStatus = checkResult.getInt("status");

	            if (existingVolunteer != 0) {
	                System.out.println("Mission already has a volunteer.");
	                return "500";
	            }

	            // Check if the mission status allows volunteering (not rejected nor finished)
	            if (missionStatus == -1 || missionStatus == 3) {
	                System.out.println("Mission status does not allow volunteering.");
	                return "500";
	            }
	        }

	        // Check the role of the user
	        PreparedStatement roleStmt = conn.prepareStatement("SELECT role FROM users WHERE id = ?;");
	        roleStmt.setInt(1, volunteer);
	        ResultSet roleResult = roleStmt.executeQuery();

	        while (roleResult.next()) {
	            int role = roleResult.getInt("role");
	            if (role == 1) {
	                // Update the mission with the volunteer
	                PreparedStatement updateStmt = conn.prepareStatement("UPDATE missions SET volunteer = ? WHERE id = ?;");
	                updateStmt.setInt(1, volunteer);
	                updateStmt.setInt(2, mission);
	                updateStmt.executeUpdate();
	                System.out.println("Volunteer -> updating mission");
	                return "200";
	            } else {
	                System.out.println("Not a Volunteer");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return "500";
	}

	
	@PutMapping(value="/giveFeedback/{user}/{mission}", produces=MediaType.APPLICATION_JSON_VALUE)
	public String giveFeedback(@PathVariable int user, @PathVariable int mission, @RequestBody String feedback) {
		String dbHost = restTemplate.getForObject("http://ConfServ/conf/db/host", String.class);
		String dbPort = restTemplate.getForObject("http://ConfServ/conf/db/port", String.class);
		
		try(Connection conn = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/projet_gei_067", "projet_gei_067", "Naif2zai")) {	
			// Check if the mission status is validated
	        PreparedStatement statusStmt = conn.prepareStatement("SELECT status FROM missions WHERE id = ?;");
	        statusStmt.setInt(1, mission);
	        ResultSet statusResult = statusStmt.executeQuery();

	        while (statusResult.next()) {
	            int missionStatus = statusResult.getInt("status");

	            if (missionStatus != 2) {
	                System.out.println("Cannot give feedback on a mission that isn't finished.");
	                return "500";
	            }
	        }
			
			Statement stmt1 = conn.createStatement();
			ResultSet rs = stmt1.executeQuery("SELECT seeker, volunteer FROM missions WHERE seeker = " + user + " OR volunteer = " + user + ";");
						
			while (rs.next()) {
				int seeker = rs.getInt("seeker");
				int volunteer = rs.getInt("volunteer");
				String columnName = (seeker == user) ? "fb_seeker" : (volunteer == user) ? "fb_volunteer" : null;
				
				if(columnName != null) {
					PreparedStatement stmt2 = conn.prepareStatement("UPDATE missions SET " + columnName + " = ? WHERE id = ?;");
			        stmt2.setString(1, feedback);
			        stmt2.setInt(2, mission);
			        stmt2.executeUpdate();
			        System.out.println(((seeker == user) ? "HelpSeeker" : "Volunteer") + " -> updating mission");
			        return "200";
			    } 
				else {
				    System.out.println("User not associated with the mission.");
				}
			}
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    }
		
		return "500";	
	}
}
