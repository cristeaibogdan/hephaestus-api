package org.personal.solarpanel;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"org.personal.solarpanel", "org.personal.shared"})
public class SolarPanelApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolarPanelApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner() {
		return args -> {
		};
	}
}


