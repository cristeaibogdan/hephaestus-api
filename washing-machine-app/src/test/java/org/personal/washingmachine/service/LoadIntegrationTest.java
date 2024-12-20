package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.personal.washingmachine.BaseIntegrationTest;
import org.personal.washingmachine.TestData;
import org.personal.washingmachine.dto.GetWashingMachineDetailResponse;
import org.personal.washingmachine.dto.GetWashingMachineFullResponse;
import org.personal.washingmachine.dto.GetWashingMachineImageResponse;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineDetail;
import org.personal.washingmachine.entity.WashingMachineImage;
import org.personal.washingmachine.entity.embedded.HiddenSurfaceDamage;
import org.personal.washingmachine.entity.embedded.PackageDamage;
import org.personal.washingmachine.entity.embedded.VisibleSurfaceDamage;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.enums.ReturnType;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional // Ensure Hibernate sessions are properly managed in your testing environment. Avoids "could not initialize proxy" Exception. Specific to tests only.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoadIntegrationTest extends BaseIntegrationTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@Autowired WashingMachineApplicationService underTest;
	@Autowired WashingMachineRepository repository;

	@BeforeEach
	void checkInitialDataInDB() {
		assertThat(repository.count()).isZero();
	}

	@Test
	void should_ReturnDTO_When_ProvidedValidSerialNumber() {
		// GIVEN
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
				new WashingMachineDetail(
						new PackageDamage(false,false,false),
						new VisibleSurfaceDamage(
								0,
								0,
								"",
								""),
						new HiddenSurfaceDamage(
								0,
								0,
								"",
								""),
						0,
						0
				)
		);
		washingMachine.addImage(
				new WashingMachineImage("some random prefix", new byte[0])
		);
		insertIntoDB(washingMachine);

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
				new GetWashingMachineDetailResponse(
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
				),
				List.of(new GetWashingMachineImageResponse(
						"some random prefix",
						new byte[0]
				))
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
		insertIntoDB(TestData.createWashingMachineWithSerialNumber("current-date-test"));

		// WHEN
		GetWashingMachineFullResponse actual = underTest.load("current-date-test");

		// THEN
		assertThat(actual.createdAt().toLocalDate())
				.isEqualTo(LocalDate.now());
	}

	private void insertIntoDB(WashingMachine... washingMachines) {
		repository.saveAll(List.of(washingMachines));
	}

	@Nested
	class MvcTest {

		@Test
		void should_ThrowCustomException_When_SerialNumberNotFound() throws Exception {
			// GIVEN
			// WHEN
			ResultActions resultActions = performRequest("I don't exist in DB");

			// THEN
			resultActions
					.andExpect(status().isNotFound())
					.andExpect(content().string(not(containsString("Internal Translation Error"))));
		}

		@Test
		void should_ReturnStatusOk_When_SerialNumberExists() throws Exception {
			// GIVEN
			insertIntoDB(TestData.createWashingMachineWithSerialNumber("ok-status"));

			// WHEN
			ResultActions resultActions = performRequest("ok-status");

			// THEN
			resultActions
					.andExpect(status().isOk())
					.andExpect(content().string(not(emptyString())));
		}

		private ResultActions performRequest(String serialNumber) throws Exception {
			return mockMvc.perform(
					get("/api/v1/washing-machines/{serialNumber}", serialNumber));
		}
	}

}