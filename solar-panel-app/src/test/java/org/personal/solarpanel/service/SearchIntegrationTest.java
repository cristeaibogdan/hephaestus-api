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
import static org.hamcrest.Matchers.*;
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
			saveToDB(
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
			saveToDB(
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
		void should_ReturnSortedListBy_DescendingDates_When_SortIsNull() {
			// GIVEN
			saveToDB(
					TestData.createValidSolarPanel("serial1"),
					TestData.createValidSolarPanel("serial2"),
					TestData.createValidSolarPanel("serial3")
			);

			// WHEN
			Page<SearchSolarPanelResponse> actual = underTest.search(
					TestData.createSearchSolarPanelRequest().toBuilder()
							.pageIndex(0)
							.pageSize(3)
							.sortByField(null)
							.build()
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(3)
					.extracting(solarPanel -> solarPanel.createdAt())
					.doesNotContainNull()
					.isSortedAccordingTo(Comparator.reverseOrder()); // Check descending order
		}

		@Test
		void should_ReturnSortedListBy_DescendingDates_When_SortDirectionIsNull() {
			// GIVEN
			saveToDB(
					TestData.createValidSolarPanel("serial1"),
					TestData.createValidSolarPanel("serial2"),
					TestData.createValidSolarPanel("serial3")
			);

			// WHEN
			Page<SearchSolarPanelResponse> actual = underTest.search(
					TestData.createSearchSolarPanelRequest().toBuilder()
							.pageIndex(0)
							.pageSize(3)
							.sortByField("Needed to pass the first check in if()")
							.sortDirection(null)
							.build()
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(3)
					.extracting(solarPanel -> solarPanel.createdAt())
					.doesNotContainNull()
					.isSortedAccordingTo(Comparator.reverseOrder()); // Check descending order
		}

		/*
			Due to how AngularMaterial table works, it is possible to send:
			sortByField: "serialNumber"
			sortDirection: ""
		 */
		@Test
		void should_ReturnSortedListBy_DescendingDates_When_SortDirectionIsEmpty() {
			// GIVEN
			saveToDB(
					TestData.createValidSolarPanel("serial1"),
					TestData.createValidSolarPanel("serial2"),
					TestData.createValidSolarPanel("serial3")
			);

			// WHEN
			Page<SearchSolarPanelResponse> actual = underTest.search(
					TestData.createSearchSolarPanelRequest().toBuilder()
							.pageIndex(0)
							.pageSize(3)
							.sortByField("serialNumber")
							.sortDirection("")
							.build()
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(3)
					.extracting(solarPanel -> solarPanel.createdAt())
					.doesNotContainNull()
					.isSortedAccordingTo(Comparator.reverseOrder()); // Check descending order
		}

		@Test
		void should_ReturnSortedListBy_DescendingDates_When_SortFieldDoesNotMatchAnyProperty() {
			// GIVEN
			saveToDB(
					TestData.createValidSolarPanel("serial1"),
					TestData.createValidSolarPanel("serial2"),
					TestData.createValidSolarPanel("serial3"),
					TestData.createValidSolarPanel("serial4")
			);

			// WHEN
			Page<SearchSolarPanelResponse> actual = underTest.search(
					TestData.createSearchSolarPanelRequest().toBuilder()
							.pageIndex(0)
							.pageSize(4)
							.sortByField("some gibberish")
							.sortDirection("desc")
							.build()
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(4)
					.extracting(solarPanel -> solarPanel.createdAt())
					.doesNotContainNull()
					.isSortedAccordingTo(Comparator.reverseOrder()); // Check descending order
		}

		@Test
		void should_ReturnSortedListBy_AscendingManufacturer() {
			// GIVEN
			saveToDB(
					TestData.createValidSolarPanel("serial1").setManufacturer("A"),
					TestData.createValidSolarPanel("serial2").setManufacturer("B"),
					TestData.createValidSolarPanel("serial3").setManufacturer("C"),
					TestData.createValidSolarPanel("serial4").setManufacturer("F")
			);

			// WHEN
			Page<SearchSolarPanelResponse> actual = underTest.search(
					TestData.createSearchSolarPanelRequest().toBuilder()
							.pageIndex(0)
							.pageSize(4)
							.sortByField("manufacturer")
							.sortDirection("asc")
							.build()
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(4)
					.extracting(solarPanel -> solarPanel.manufacturer())
					.isSortedAccordingTo(Comparator.naturalOrder());
		}

		@Test
		void should_ReturnSortedListBy_DescendingModel() {
			// GIVEN
			saveToDB(
					TestData.createValidSolarPanel("serial1").setModel("A"),
					TestData.createValidSolarPanel("serial2").setModel("B"),
					TestData.createValidSolarPanel("serial3").setModel("C"),
					TestData.createValidSolarPanel("serial4").setModel("F")
			);

			// WHEN
			Page<SearchSolarPanelResponse> actual = underTest.search(
					TestData.createSearchSolarPanelRequest().toBuilder()
							.pageIndex(0)
							.pageSize(4)
							.sortByField("model")
							.sortDirection("desc")
							.build()
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(4)
					.extracting(solarPanel -> solarPanel.model())
					.isSortedAccordingTo(Comparator.reverseOrder());
		}

		@Test
		void should_ReturnSortedListBy_AscendingType() {
			// GIVEN
			saveToDB(
					TestData.createValidSolarPanel("serial1").setType("A"),
					TestData.createValidSolarPanel("serial2").setType("B"),
					TestData.createValidSolarPanel("serial3").setType("C"),
					TestData.createValidSolarPanel("serial4").setType("F")
			);

			// WHEN
			Page<SearchSolarPanelResponse> actual = underTest.search(
					TestData.createSearchSolarPanelRequest().toBuilder()
							.pageIndex(0)
							.pageSize(4)
							.sortByField("type")
							.sortDirection("asc")
							.build()
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(4)
					.extracting(solarPanel -> solarPanel.type())
					.isSortedAccordingTo(Comparator.naturalOrder());
		}

		@Test
		void should_ReturnSortedListBy_AscendingSerialNumber() {
			// GIVEN
			saveToDB(
					TestData.createValidSolarPanel("serial1"),
					TestData.createValidSolarPanel("serial2"),
					TestData.createValidSolarPanel("serial3"),
					TestData.createValidSolarPanel("serial4")
			);

			// WHEN
			Page<SearchSolarPanelResponse> actual = underTest.search(
					TestData.createSearchSolarPanelRequest().toBuilder()
							.pageIndex(0)
							.pageSize(4)
							.sortByField("serialNumber")
							.sortDirection("asc")
							.build()
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(4)
					.extracting(solarPanel -> solarPanel.serialNumber())
					.isSortedAccordingTo(Comparator.naturalOrder());
		}

		@Test
		void should_ReturnSortedListBy_AscendingRecommendation() {
			// GIVEN
			saveToDB(
					TestData.createValidSolarPanel("serial1").setDamage(TestData.createDamageWithRecommendation(Recommendation.REPAIR)),
					TestData.createValidSolarPanel("serial2").setDamage(TestData.createDamageWithRecommendation(Recommendation.DISPOSE)),
					TestData.createValidSolarPanel("serial3").setDamage(TestData.createDamageWithRecommendation(Recommendation.RECYCLE)),
					TestData.createValidSolarPanel("serial4").setDamage(TestData.createDamageWithRecommendation(Recommendation.REPAIR))
			);

			// WHEN
			Page<SearchSolarPanelResponse> actual = underTest.search(
					TestData.createSearchSolarPanelRequest().toBuilder()
							.pageIndex(0)
							.pageSize(4)
							.sortByField("recommendation")
							.sortDirection("asc")
							.build()
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(4)
					.extracting(solarPanel -> solarPanel.recommendation())
					.isSortedAccordingTo(Comparator.comparing(Recommendation::name));
		}

		@Test
		void should_ReturnSortedListBy_AscendingDate() {
			// GIVEN
			saveToDB(
					TestData.createValidSolarPanel("serial1"),
					TestData.createValidSolarPanel("serial2"),
					TestData.createValidSolarPanel("serial3"),
					TestData.createValidSolarPanel("serial4")
			);

			// WHEN
			Page<SearchSolarPanelResponse> actual = underTest.search(
					TestData.createSearchSolarPanelRequest().toBuilder()
							.pageIndex(0)
							.pageSize(4)
							.sortByField("createdAt")
							.sortDirection("asc")
							.build()
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(4)
					.extracting(solarPanel -> solarPanel.serialNumber())
					.isSortedAccordingTo(Comparator.naturalOrder());
		}

		@ParameterizedTest
		@ValueSource(strings = {"Tesla", "Huawei"})
		void should_ReturnFilteredList_By_Manufacturer(String manufacturer) {
			// GIVEN
			saveToDB(
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
					.extracting(sp -> sp.manufacturer())
					.contains(manufacturer);
		}

		@ParameterizedTest
		@ValueSource(strings = {"ModelA", "ModelB"})
		void should_ReturnFilteredList_By_Model(String model) {
			// GIVEN
			saveToDB(
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
					.extracting(sp -> sp.model())
					.contains(model);
		}

		@ParameterizedTest
		@ValueSource(strings = {"TypeY", "TypeZ"})
		void should_ReturnFilteredList_By_Type(String type) {
			// GIVEN
			saveToDB(
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
					.extracting(sp -> sp.type())
					.contains(type);
		}

		@ParameterizedTest
		@ValueSource(strings = {"serial1", "serial2"})
		void should_ReturnFilteredList_By_SerialNumber(String serialNumber) {
			// GIVEN
			saveToDB(
					TestData.createValidSolarPanel("serial1"),
					TestData.createValidSolarPanel("serial2"),
					TestData.createValidSolarPanel("serial3")
			);

			// WHEN
			Page<SearchSolarPanelResponse> actual = underTest.search(
					TestData.createSearchSolarPanelRequest().toBuilder()
							.pageIndex(0)
							.pageSize(3)
							.serialNumber(serialNumber)
							.build()
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(1)
					.extracting(sp -> sp.serialNumber())
					.contains(serialNumber);
		}

		@ParameterizedTest
		@EnumSource(value = Recommendation.class)
		void should_ReturnFilteredList_By_Recommendation(Recommendation recommendation) {
			// GIVEN
			saveToDB(
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
					.extracting(sp -> sp.recommendation())
					.contains(recommendation);
		}

		@Test
		void should_ReturnFilteredList_By_ManufacturerAndType() {
			// GIVEN
			saveToDB(
					TestData.createValidSolarPanel("serial1").setManufacturer("Tesla").setType("TypeY"),
					TestData.createValidSolarPanel("serial2").setManufacturer("Tesla").setType("TypeY"),
					TestData.createValidSolarPanel("serial3").setManufacturer("Tesla").setType("TypeZ"),
					TestData.createValidSolarPanel("serial4").setManufacturer("Huawei").setType("TypeZ"),
					TestData.createValidSolarPanel("serial4").setManufacturer("Tesla").setType("TypeWHY")
			);

			// WHEN
			Page<SearchSolarPanelResponse> actual = underTest.search(
					TestData.createSearchSolarPanelRequest().toBuilder()
							.pageIndex(0)
							.pageSize(5)
							.manufacturer("Tesla")
							.type("TypeY")
							.build()
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(2)
					.allSatisfy(sp -> {
						assertThat(sp.manufacturer()).containsIgnoringCase("Tesla");
						assertThat(sp.type()).containsIgnoringCase("TypeY");
					});
		}
	}

	@Nested
	class MvcTest {
		@Test
		void should_ReturnStatusOk_When_EntitiesInDB() throws Exception {
			// GIVEN
			saveToDB(
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

		@Test
		void should_ThrowCustomException_When_InvalidDate() throws Exception {
			// GIVEN
			// WHEN
			ResultActions resultActions = performRequest(
					TestData.createSearchSolarPanelRequest().toBuilder()
							.createdAt("invalid date")
							.build()
			);

			// THEN
			resultActions
					.andExpect(status().isBadRequest())
					.andExpect(content().string(not(containsString("Internal Translation Error"))));
		}
	}

	private void saveToDB(SolarPanel... solarPanels) {
		repository.saveAll(List.of(solarPanels));
	}

	private ResultActions performRequest(SearchSolarPanelRequest request) throws Exception {
		return mockMvc.perform(
				post("/v1/solar-panels")
						.content(jackson.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON));
	}
}

