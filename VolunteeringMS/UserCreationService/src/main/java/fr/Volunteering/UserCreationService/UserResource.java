package fr.Volunteering.UserCreationService;

import java.sql.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/users")
public class UserResource {
	@Autowired
	private RestTemplate restTemplate;
	
	@PostMapping(value = "/createHelpSeeker/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User createHelpSeeker(@PathVariable String name) {
        return createUser(name, 0, HelpSeeker.class);
    }

    @PostMapping(value = "/createVolunteer/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User createVolunteer(@PathVariable String name) {
        return createUser(name, 1, Volunteer.class);
    }

    @PostMapping(value = "/createAdmin/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User createAdmin(@PathVariable String name) {
        return createUser(name, 2, Admin.class);
    }
    
    // Creates a user with the given name, adds it to the database, and returns it
    private <T extends User> T createUser(String name, int role, Class<T> userType) {
        String dbHost = restTemplate.getForObject("http://ConfServ/conf/db/host", String.class);
        String dbPort = restTemplate.getForObject("http://ConfServ/conf/db/port", String.class);

        T user = null;

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/projet_gei_067", "projet_gei_067", "Naif2zai");
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users(name, role) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, name);
            stmt.setInt(2, role);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user = userType.getDeclaredConstructor(String.class).newInstance(name);
                    user.setID(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }
}
