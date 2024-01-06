package fr.Volunteering.MissionAdminManagementService;

import java.sql.*;

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
public class MissionAdminManagementResource {
	@Autowired
	private RestTemplate restTemplate;
	
	private String getDbHost() {
        return restTemplate.getForObject("http://ConfServ/conf/db/host", String.class);
    }

    private String getDbPort() {
        return restTemplate.getForObject("http://ConfServ/conf/db/port", String.class);
    }
	
	private boolean isAdmin(int userId) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://" + getDbHost() + ":" + getDbPort() + "/projet_gei_067", "projet_gei_067", "Naif2zai");
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT role FROM users WHERE id = " + userId + ";");
            if (rs.next()) {
                return rs.getInt("role") == 2;
            }
        }
        return false;
    }

    private String updateMissionStatus(int admin, int id, int status, String adminMsg) {
        String dbHost = getDbHost();
        String dbPort = getDbPort();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/projet_gei_067", "projet_gei_067", "Naif2zai")) {
            if (isAdmin(admin)) {
                PreparedStatement stmt = conn.prepareStatement("UPDATE missions SET status = ?, admin_msg = ? WHERE id = ?;");
                stmt.setInt(1, status);
                stmt.setString(2, adminMsg);
                stmt.setInt(3, id);
                stmt.executeUpdate();
                System.out.println("Admin -> updating mission");
                return "200";
            } else {
                System.out.println("Not an Admin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "500";
    }

    @PutMapping(value = "/validateMission/{admin}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String validateMission(@PathVariable int admin, @PathVariable int id) {
    	return updateMissionStatus(admin, id, 1, null);
    }

    @PutMapping(value = "/rejectMission/{admin}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String rejectMission(@PathVariable int admin, @PathVariable int id, @RequestBody String message) {
    	return updateMissionStatus(admin, id, -1, message);
    }

    @PutMapping(value = "/updateMissionStatus/{admin}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String updateMissionStatus(@PathVariable int admin, @PathVariable int id, Integer status) {
    	return updateMissionStatus(admin, id, status, null);
    }
	
}
