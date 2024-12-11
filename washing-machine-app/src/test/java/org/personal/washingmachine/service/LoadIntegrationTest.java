package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.personal.washingmachine.BaseIntegrationTest;
import org.personal.washingmachine.TestData;
import org.personal.washingmachine.dto.GetWashingMachineDetailResponse;
import org.personal.washingmachine.dto.GetWashingMachineFullResponse;
import org.personal.washingmachine.dto.GetWashingMachineImageResponse;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineImage;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.enums.ReturnType;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional // Ensure Hibernate sessions are properly managed in your testing environment. Avoids "could not initialize proxy" Exception. Specific to tests only.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoadIntegrationTest extends BaseIntegrationTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@Autowired WashingMachineApplicationService underTest;
	@Autowired WashingMachineRepository washingMachineRepository;

	@BeforeAll
	void loadDataInDB() {
		WashingMachine washingMachine = new WashingMachine(
				"Washing Machine",
				"Gorenje",
				DamageType.IN_USE,
				ReturnType.COMMERCIAL,
				IdentificationMode.DATA_MATRIX,
				"The only one in DB",
				"modelA",
				"TypeZ",
				Recommendation.OUTLET,
				TestData.washingMachineDetail()
		);
		washingMachine.addImage(
				new WashingMachineImage("some random prefix", new byte[0])
		);
		washingMachineRepository.save(washingMachine);
	}

	@AfterAll
	void cleanUpDB() {
		washingMachineRepository.deleteAll();
	}

	@BeforeEach
	void oneEntityInDB() {
		assertThat(washingMachineRepository.count()).isOne();
	}

	@Test
	void should_ReturnDTO_When_ProvidedValidSerialNumber() {
		// GIVEN
		GetWashingMachineDetailResponse detail = new GetWashingMachineDetailResponse(
				false,
				false,
				false,
				false,
				false,
				false,
				0,
				false,
				0,
				false,
				"",
				false,
				"",
				false,
				false,
				0,
				false,
				0,
				false,
				"",
				false,
				"",
				0,
				0
		);

		GetWashingMachineImageResponse image = new GetWashingMachineImageResponse(
				"some random prefix",
				new byte[0]
		);

		GetWashingMachineFullResponse expected = new GetWashingMachineFullResponse(
				"Washing Machine",
				"Gorenje",
				IdentificationMode.DATA_MATRIX,
				"modelA",
				"TypeZ",
				"The only one in DB",
				ReturnType.COMMERCIAL,
				DamageType.IN_USE,
				Recommendation.OUTLET,
				LocalDateTime.now(),
				detail,
				List.of(image)
		);

		// WHEN
		GetWashingMachineFullResponse actual = underTest.load("The only one in DB");

		// THEN
		assertThat(actual).usingRecursiveComparison()
				.ignoringFields("createdAt")
				.isEqualTo(expected);
	}

	@Test
	void should_ReturnCurrentDateInCreatedAt() {
		// GIVEN

		// WHEN
		GetWashingMachineFullResponse actual = underTest.load("The only one in DB");

		// THEN
		assertThat(actual.createdAt().toLocalDate()).isEqualTo(LocalDate.now());
	}

}