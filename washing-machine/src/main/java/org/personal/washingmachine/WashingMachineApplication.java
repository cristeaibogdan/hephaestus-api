package org.personal.washingmachine;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class WashingMachineApplication {

	public static void main(String[] args) {
		SpringApplication.run(WashingMachineApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner() {
		return args -> {
		};
	}
}
