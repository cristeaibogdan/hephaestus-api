package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.personal.washingmachine.BaseIntegrationTest;
import org.personal.washingmachine.TestData;
import org.personal.washingmachine.dto.GetWashingMachineReportResponse;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.enums.ReturnType;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GetReportIntegrationTest extends BaseIntegrationTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@Autowired WashingMachineApplicationService underTest;
	@Autowired WashingMachineRepository repository;

	@BeforeEach
	void checkNoDataInDB() {
		assertThat(repository.count()).isZero();
	}

	@Nested
	class IntegrationTest {

		@Test
		void should_ReturnDTO_With_ValidProperties() {
			// GIVEN
			saveToDB(new WashingMachine(
					"Washing Machine",
					"Gorenje",
					DamageType.IN_USE,
					ReturnType.COMMERCIAL,
					IdentificationMode.DATA_MATRIX,
					"I will return a Report!",
					"modelA",
					"TypeZ",
					TestData.createWashingMachineDetailWithRecommendation(Recommendation.RESALE)
			));

			// WHEN
			GetWashingMachineReportResponse actual = underTest.getReport("I will return a Report!");

			// THEN
			assertThat(actual.report())
					.isNotNull()
					.isNotEmpty();

			assertThat(actual.createdAt())
					.isNotNull()
					.isNotEmpty();
		}
	}

	private void saveToDB(WashingMachine... washingMachines) {
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
		void should_ReturnStatusOk_When_SerialNumberFound() throws Exception {
			// GIVEN
			saveToDB(
					TestData.createValidWashingMachine("I exist")
			);

			// WHEN
			ResultActions resultActions = performRequest("I exist");

			// THEN
			resultActions
					.andExpect(status().isOk())
					.andExpect(content().string(not(emptyString())));
		}

		private ResultActions performRequest(String serialNumber) throws Exception {
			return mockMvc.perform(
					get("/v1/washing-machines/{serialNumber}/report", serialNumber));
		}
	}
}
