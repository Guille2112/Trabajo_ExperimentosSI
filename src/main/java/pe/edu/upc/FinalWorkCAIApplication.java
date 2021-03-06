package pe.edu.upc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import pe.edu.upc.service.ISecurityService;

@SpringBootApplication
public class FinalWorkCAIApplication implements CommandLineRunner {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private ISecurityService secService;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(FinalWorkCAIApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		String password = "cai2021";
		secService.createUser("admin", password, "diegopaz25814@gmail.com", "ADMIN", "ADMIN", "ROLE_ADMIN");

		String bcryptPassword = passwordEncoder.encode(password);
		System.out.println(bcryptPassword);

	}

}
