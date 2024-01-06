package fr.Volunteering.MissionCreationService;

import java.sql.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/missions")
public class MissionCreationResource {
	@Autowired
	private RestTemplate restTemplate;
	
	@PostMapping(value = "/seekHelp/{seeker}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mission seekHelp(@PathVariable int seeker, @RequestBody String description) {
        return createMission(seeker, description, false);
    }

    @PostMapping(value = "/offerHelp/{volunteer}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mission offerHelp(@PathVariable int volunteer, @RequestBody String description) {
        return createMission(volunteer, description, true);
    }
    
    // Creates a mission with the given description, adds it to the database, and returns it
    private Mission createMission(int userId, String description, boolean isVolunteer) {
        String dbHost = restTemplate.getForObject("http://ConfServ/conf/db/host", String.class);
        String dbPort = restTemplate.getForObject("http://ConfServ/conf/db/port", String.class);
        Mission mission = new Mission(userId, description, isVolunteer);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/projet_gei_067", "projet_gei_067", "Naif2zai")) {
            Statement stmt1 = conn.createStatement();
            ResultSet rs = stmt1.executeQuery("SELECT role FROM users WHERE id = " + userId + ";");

            while (rs.next()) {
                int role = rs.getInt("role");
                if (isVolunteer && role == 1) {
                    String insertSql = "INSERT INTO missions(volunteer, status, description) VALUES (?, 0, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                        stmt.setInt(1, userId);
                        stmt.setString(2, description);
                        stmt.executeUpdate();
                        ResultSet generatedKeys = stmt.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            mission.setID(generatedKeys.getInt(1));
                        }
                        System.out.println("Volunteer -> offering help");
                    }
                } else if (!isVolunteer && role == 0) {
                    String insertSql = "INSERT INTO missions(seeker, status, description) VALUES (?, 0, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                        stmt.setInt(1, userId);
                        stmt.setString(2, description);
                        stmt.executeUpdate();
                        ResultSet generatedKeys = stmt.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            mission.setID(generatedKeys.getInt(1));
                        }
                        System.out.println("HelpSeeker -> creating mission");
                    }
                } else {
                    System.out.println(isVolunteer ? "Not a Volunteer" : "Not a HelpSeeker");
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mission;
    }
}
