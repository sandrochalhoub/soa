package fr.Volunteering.Conf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfApplication.class, args);
	}

}
