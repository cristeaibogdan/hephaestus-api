package org.personal.solarpanel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
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
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SearchIntegrationTest extends BaseIntegrationTest {

	@Autowired SolarPanelApplicationService underTest;
	@Autowired SolarPanelRepository repository;

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@Nested
	class IntegrationTest {
		@Test
		void should_ReturnDTO_With_CorrectProperties() {
			// GIVEN
			saveIntoDB(
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
			Page<SearchSolarPanelResponse> actual = underTest.search(
					TestData.createSearchSolarPanelRequest().toBuilder()
							.pageIndex(0)
							.pageSize(2)
							.build()
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
			saveIntoDB(
					TestData.createValidSolarPanel("serial1"),
					TestData.createValidSolarPanel("serial2"),
					TestData.createValidSolarPanel("serial3")
			);

			// WHEN
			Page<SearchSolarPanelResponse> actual = underTest.search(
					TestData.createSearchSolarPanelRequest().toBuilder()
							.pageIndex(0)
							.pageSize(3)
							.build()
			);

			// THEN
			assertThat(actual.getNumber()).isZero();
			assertThat(actual.getSize()).isEqualTo(3);
		}

		@Test
		void should_ReturnListWithDescendingDates() {
			// GIVEN
			saveIntoDB(
					TestData.createValidSolarPanel("serial1"),
					TestData.createValidSolarPanel("serial2"),
					TestData.createValidSolarPanel("serial3")
			);

			// WHEN
			Page<SearchSolarPanelResponse> actual = underTest.search(
					TestData.createSearchSolarPanelRequest().toBuilder()
							.pageIndex(0)
							.pageSize(5)
							.build()
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(3)
					.extracting(wm -> wm.createdAt())
					.doesNotContainNull()
					.isSortedAccordingTo(Comparator.reverseOrder()); // Check descending order
		}

		@ParameterizedTest
		@ValueSource(strings = {"Tesla", "Huawei"})
		void should_ReturnFilteredList_By_Manufacturer(String manufacturer) {
			// GIVEN
			saveIntoDB(
					TestData.createValidSolarPanel("serial1").setManufacturer("Tesla"),
					TestData.createValidSolarPanel("serial2").setManufacturer("Tesla"),
					TestData.createValidSolarPanel("serial3").setManufacturer("Huawei"),
					TestData.createValidSolarPanel("serial4").setManufacturer("Huawei")
			);

			// WHEN
			Page<SearchSolarPanelResponse> actual = underTest.search(
					TestData.createSearchSolarPanelRequest().toBuilder()
							.pageIndex(0)
							.pageSize(4)
							.manufacturer(manufacturer)
							.build()
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(2)
					.extracting(wm -> wm.manufacturer())
					.contains(manufacturer);
		}

		@ParameterizedTest
		@ValueSource(strings = {"ModelA", "ModelB"})
		void should_ReturnFilteredList_By_Model(String model) {
			// GIVEN
			saveIntoDB(
					TestData.createValidSolarPanel("serial1").setModel("ModelA"),
					TestData.createValidSolarPanel("serial2").setModel("ModelA"),
					TestData.createValidSolarPanel("serial3").setModel("ModelB"),
					TestData.createValidSolarPanel("serial4").setModel("ModelB"),
					TestData.createValidSolarPanel("serial4").setModel("ModelC")
			);

			// WHEN
			Page<SearchSolarPanelResponse> actual = underTest.search(
					TestData.createSearchSolarPanelRequest().toBuilder()
							.pageIndex(0)
							.pageSize(4)
							.model(model)
							.build()
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(2)
					.extracting(wm -> wm.model())
					.contains(model);
		}

		@ParameterizedTest
		@ValueSource(strings = {"TypeY", "TypeZ"})
		void should_ReturnFilteredList_By_Type(String type) {
			// GIVEN
			saveIntoDB(
					TestData.createValidSolarPanel("serial1").setType("TypeY"),
					TestData.createValidSolarPanel("serial2").setType("TypeY"),
					TestData.createValidSolarPanel("serial3").setType("TypeZ"),
					TestData.createValidSolarPanel("serial4").setType("TypeZ"),
					TestData.createValidSolarPanel("serial4").setType("TypeWHY")
			);

			// WHEN
			Page<SearchSolarPanelResponse> actual = underTest.search(
					TestData.createSearchSolarPanelRequest().toBuilder()
							.pageIndex(0)
							.pageSize(4)
							.type(type)
							.build()
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(2)
					.extracting(wm -> wm.type())
					.contains(type);
		}
	}

	@ParameterizedTest
	@EnumSource(value = Recommendation.class)
	void should_ReturnFilteredList_By_Recommendation(Recommendation recommendation) {
		// GIVEN
		saveIntoDB(
				TestData.createValidSolarPanel("serial1").setDamage(TestData.createDamageWithRecommendation(Recommendation.REPAIR)),
				TestData.createValidSolarPanel("serial2").setDamage(TestData.createDamageWithRecommendation(Recommendation.REPAIR)),
				TestData.createValidSolarPanel("serial3").setDamage(TestData.createDamageWithRecommendation(Recommendation.DISPOSE)),
				TestData.createValidSolarPanel("serial4").setDamage(TestData.createDamageWithRecommendation(Recommendation.DISPOSE)),
				TestData.createValidSolarPanel("serial5").setDamage(TestData.createDamageWithRecommendation(Recommendation.RECYCLE)),
				TestData.createValidSolarPanel("serial6").setDamage(TestData.createDamageWithRecommendation(Recommendation.RECYCLE))
		);

		// WHEN
		Page<SearchSolarPanelResponse> actual = underTest.search(
				TestData.createSearchSolarPanelRequest().toBuilder()
						.pageIndex(0)
						.pageSize(6)
						.recommendation(recommendation)
						.build()
		);

		// THEN
		assertThat(actual.getContent())
				.hasSize(2)
				.extracting(wm -> wm.recommendation())
				.contains(recommendation);
	}

	@Nested
	class MvcTest {
		@Test
		void should_ReturnStatusOk_When_EntitiesInDB() throws Exception {
			// GIVEN
			saveIntoDB(
					TestData.createValidSolarPanel("serial1"),
					TestData.createValidSolarPanel("serial2")
			);

			// WHEN
			ResultActions resultActions = performRequest(
					TestData.createSearchSolarPanelRequest().toBuilder()
							.pageIndex(0)
							.pageSize(2)
							.build()
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
					TestData.createSearchSolarPanelRequest().toBuilder()
							.pageIndex(0)
							.pageSize(2)
							.build()
			);

			// THEN
			resultActions
					.andExpect(status().isOk())
					.andExpect(content().string(not(emptyString())));
		}

	}

	private void saveIntoDB(SolarPanel... solarPanels) {
		repository.saveAll(List.of(solarPanels));
	}

	private ResultActions performRequest(SearchSolarPanelRequest request) throws Exception {
		return mockMvc.perform(
				post("/v1/solar-panels")
						.content(jackson.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON));
	}
}
