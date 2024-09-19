package org.personal.washingmachine;

import org.personal.washingmachine.dto.OrganizationAndCountryDTO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication(scanBasePackages = {"org.personal.washingmachine", "org.personal.shared"})
@EnableFeignClients(basePackages = "org.personal.shared")
public class WashingMachineApplication {

	public static void main(String[] args) {
		SpringApplication.run(WashingMachineApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner() {
		return args -> {
		};
	}

	@Bean
	Map<OrganizationAndCountryDTO, List<String>> initializeRegistrationCodes() {  // TODO: Replace with proper authentication
		Map<OrganizationAndCountryDTO, List<String>> registerCodes = new HashMap<>();
		registerCodes.put(new OrganizationAndCountryDTO("ZEOS", "SLOVENIA"), List.of("RX1000", "RX1001", "RX1002", "RX1003"));
		registerCodes.put(new OrganizationAndCountryDTO("GORENJE", "SLOVENIA"), List.of("RX2000", "RX2001", "RX2002", "RX2003"));
		registerCodes.put(new OrganizationAndCountryDTO("BOSCH", "GERMANY"), List.of("RX3000", "RX3001", "RX3002", "RX3003"));
		registerCodes.put(new OrganizationAndCountryDTO("SMEG", "ITALY"), List.of("RX4000", "RX4001", "RX4002", "RX4003"));
		registerCodes.put(new OrganizationAndCountryDTO("ORIGIN", "ROMANIA"), List.of("RX5000", "RX5001", "RX5002", "RX5003"));

		return registerCodes;
	}
}


