package org.personal.solarpanel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.personal.solarpanel.BaseIntegrationTest;
import org.personal.solarpanel.TestData;
import org.personal.solarpanel.dto.SearchSolarPanelRequest;
import org.personal.solarpanel.dto.SearchSolarPanelResponse;
import org.personal.solarpanel.entity.Damage;
import org.personal.solarpanel.entity.SolarPanel;
import org.personal.solarpanel.enums.Recommendation;
import org.personal.solarpanel.repository.SolarPanelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoadPaginatedIntegrationTest extends BaseIntegrationTest {

	@Autowired SolarPanelApplicationService underTest;
	@Autowired SolarPanelRepository repository;

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@Nested
	class IntegrationTest {
		@Test
		void should_ReturnDTO_With_CorrectProperties() {
			// GIVEN
			insertIntoDB(
					new SolarPanel(
							"Solar Panel",
							"manufacturer",
							"model",
							"type",
							"serialNumber",
							new Damage(
									true,
									true,
									false,
									true,
									"20% of cells are missing."
							)
					)
			);

			List<SearchSolarPanelResponse> expected = List.of(new SearchSolarPanelResponse(
					"Solar Panel",
					"manufacturer",
					"model",
					"type",
					"serialNumber",
					Recommendation.RECYCLE,
					LocalDateTime.now()
			));

			// WHEN
			Page<SearchSolarPanelResponse> actual = underTest.loadPaginated(
					new SearchSolarPanelRequest(0, 2)
			);

			// THEN
			assertThat(actual.getContent())
					.usingRecursiveComparison()
					.ignoringFields("createdAt")
					.isEqualTo(expected);
		}

		@Test
		void should_ReturnCorrectPageIndexAndSize() {
			// GIVEN
			insertIntoDB(
					TestData.createValidSolarPanel("serial1"),
					TestData.createValidSolarPanel("serial2"),
					TestData.createValidSolarPanel("serial3")
			);

			// WHEN
			Page<SearchSolarPanelResponse> actual = underTest.loadPaginated(
					new SearchSolarPanelRequest(0, 3)
			);

			// THEN
			assertThat(actual.getNumber()).isEqualTo(0);
			assertThat(actual.getContent()).size().isEqualTo(3);
		}
	}

	@Nested
	class MvcTest {
		@Test
		void should_ReturnStatusOk_When_EntitiesInDB() throws Exception {
			// GIVEN
			insertIntoDB(
					TestData.createValidSolarPanel("serial1"),
					TestData.createValidSolarPanel("serial2")
			);

			// WHEN
			ResultActions resultActions = performRequest(
					new SearchSolarPanelRequest(0, 2)
			);

			// THEN
			resultActions
					.andExpect(status().isOk())
					.andExpect(content().string(not(emptyString())));
		}

		@Test
		void should_ReturnStatusOk_When_NoEntitiesInDB() throws Exception {
			// GIVEN
			// WHEN
			ResultActions resultActions = performRequest(
					new SearchSolarPanelRequest(0, 2)
			);

			// THEN
			resultActions
					.andExpect(status().isOk())
					.andExpect(content().string(not(emptyString())));
		}
	}

	private void insertIntoDB(SolarPanel... solarPanels) {
		repository.saveAll(List.of(solarPanels));
	}

	private ResultActions performRequest(SearchSolarPanelRequest request) throws Exception {
		return mockMvc.perform(
				post("/v1/solar-panels")
						.content(jackson.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON));
	}
}
