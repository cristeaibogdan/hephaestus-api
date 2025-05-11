package org.personal.solarpanel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.personal.solarpanel.BaseIntegrationTest;
import org.personal.solarpanel.TestData;
import org.personal.solarpanel.dto.CreateSolarPanelRequest;
import org.personal.solarpanel.entity.SolarPanel;
import org.personal.solarpanel.repository.SolarPanelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SaveIntegrationTest extends BaseIntegrationTest {

	@Autowired SolarPanelApplicationService underTest;
	@Autowired SolarPanelRepository repository;

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@BeforeEach
	void checkInitialDataInDB() {
		assertThat(repository.count()).isZero();
	}

	@Nested
	class IntegrationTest {
		@Test
		void should_SaveRequest_With_AllPropertiesInDB() {
			// GIVEN
			CreateSolarPanelRequest request = new CreateSolarPanelRequest(
					"Solar Panel",
					"Tesla",
					"model1",
					"type2",
					"AX-098-200",
					new CreateSolarPanelRequest.Damage(
							true,
							false,
							true,
							false,
							"blue paint was thrown on half the panel"
					)
			);

			// WHEN
			underTest.save(request);
			SolarPanel actual = repository.findBySerialNumber("AX-098-200").orElseThrow();

			// THEN
			assertThat(actual).satisfies(act -> {
				// main fields
				assertThat(act.getCategory()).isEqualTo(request.category());
				assertThat(act.getManufacturer()).isEqualTo(request.manufacturer());
				assertThat(act.getModel()).isEqualTo(request.model());
				assertThat(act.getType()).isEqualTo(request.type());
				assertThat(act.getSerialNumber()).isEqualTo(request.serialNumber());

				// damage
				assertThat(act.getDamage().isHotSpots()).isEqualTo(request.damage().hotSpots());
				assertThat(act.getDamage().isMicroCracks()).isEqualTo(request.damage().microCracks());
				assertThat(act.getDamage().isSnailTrails()).isEqualTo(request.damage().snailTrails());
				assertThat(act.getDamage().isBrokenGlass()).isEqualTo(request.damage().brokenGlass());
				assertThat(act.getDamage().getAdditionalDetails()).isEqualTo(request.damage().additionalDetails());
			});
		}

		@Test
		void should_ReturnCurrentDate_For_createdAtField() {
			// GIVEN
			CreateSolarPanelRequest request = TestData.createValidSaveSolarPanelRequest().toBuilder()
					.serialNumber("date-test")
					.build();

			// WHEN
			underTest.save(request);
			SolarPanel actual = repository.findBySerialNumber("date-test").orElseThrow();

			// THEN
			assertThat(actual.getCreatedAt().toLocalDate())
					.isEqualTo(LocalDate.now());
		}
	}

	@Nested
	class MvcTest {

		@Test
		void should_ReturnStatusCreated_When_DTOSaved() throws Exception {
			// GIVEN
			CreateSolarPanelRequest request = TestData.createValidSaveSolarPanelRequest();

			// WHEN
			ResultActions resultActions = performRequest(request);

			// THEN
			resultActions.andExpect(status().isCreated())
					.andExpect(content().string(emptyString()));
		}

		@Test
		void should_ThrowCustomException_When_SerialNumberAlreadyTaken() throws Exception {
			// GIVEN
			CreateSolarPanelRequest request = TestData.createValidSaveSolarPanelRequest().toBuilder()
					.serialNumber("duplicated serialNumber")
					.build();

			performRequest(request);

			// WHEN
			ResultActions resultActions = performRequest(request);

			// THEN
			resultActions.andExpect(status().isConflict())
					.andExpect(content().string(not(containsString("Internal Translation Error"))));
		}
	}

	private ResultActions performRequest(CreateSolarPanelRequest request) throws Exception {
		return mockMvc.perform(
				post("/v1/solar-panels/save")
						.content(jackson.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON)
		);
	}

}
