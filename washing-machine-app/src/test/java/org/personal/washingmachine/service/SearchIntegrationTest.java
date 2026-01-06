package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.personal.washingmachine.BaseIntegrationTest;
import org.personal.shared.time.ClockHolder;
import org.personal.washingmachine.TestData;
import org.personal.washingmachine.dto.SearchWashingMachineResponse;
import org.personal.washingmachine.dto.SearchWashingMachineRequest;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.enums.ReturnType;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.personal.washingmachine.time.ClockUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SearchIntegrationTest extends BaseIntegrationTest {

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
		void should_ReturnDTO_With_CorrectProperties() {
			// GIVEN
			saveToDB(
					new WashingMachine(
							"Washing Machine",
							"WhirlPool",
							DamageType.IN_TRANSIT,
							ReturnType.TRANSPORT,
							IdentificationMode.DATA_MATRIX,
							"serial10",
							"modelOne",
							"TypeOne",
							TestData.createWashingMachineDetailWithRecommendation(Recommendation.DISASSEMBLE)
					)
			);

			List<SearchWashingMachineResponse> expected = List.of(
					new SearchWashingMachineResponse(
							"Washing Machine",
							"WhirlPool",
							IdentificationMode.DATA_MATRIX,
							"modelOne",
							"TypeOne",
							"serial10",
							ReturnType.TRANSPORT,
							DamageType.IN_TRANSIT,
							Recommendation.DISASSEMBLE,
							LocalDateTime.now())
			);

			// WHEN
			Page<SearchWashingMachineResponse> actual = underTest.search(
					TestData.createSearchWashingMachineRequest()
							.withPageIndex(0)
							.withPageSize(1)
							.withSerialNumber("serial10")
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
					TestData.createValidWashingMachine("serial1"),
					TestData.createValidWashingMachine("serial2"),
					TestData.createValidWashingMachine("serial3")
			);

			// WHEN
			Page<SearchWashingMachineResponse> actual = underTest.search(
					TestData.createSearchWashingMachineRequest()
							.withPageIndex(0)
							.withPageSize(3)
			);

			// THEN
			assertThat(actual.getNumber()).isZero();
			assertThat(actual.getSize()).isEqualTo(3);
		}

		@Test
		void should_ReturnSortedListBy_DescendingDates_When_SortByFieldIsNull() {
			// GIVEN
			saveToDB(
					TestData.createValidWashingMachine("serial1"),
					TestData.createValidWashingMachine("serial2"),
					TestData.createValidWashingMachine("serial3")
			);

			// WHEN
			Page<SearchWashingMachineResponse> actual = underTest.search(
					TestData.createSearchWashingMachineRequest()
							.withPageIndex(0)
							.withPageSize(3)
							.withSortByField(null)
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(3)
					.extracting(washingMachine -> washingMachine.createdAt())
					.doesNotContainNull()
					.isSortedAccordingTo(Comparator.reverseOrder());
		}

		@Test
		void should_ReturnSortedListBy_DescendingDates_When_SortDirectionIsNull() {
			// GIVEN
			saveToDB(
					TestData.createValidWashingMachine("serial1"),
					TestData.createValidWashingMachine("serial2"),
					TestData.createValidWashingMachine("serial3")
			);

			// WHEN
			Page<SearchWashingMachineResponse> actual = underTest.search(
					TestData.createSearchWashingMachineRequest()
							.withPageIndex(0)
							.withPageSize(3)
							.withSortByField("Needed to pass the first check in if()")
							.withSortDirection(null)
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(3)
					.extracting(washingMachine -> washingMachine.createdAt())
					.doesNotContainNull()
					.isSortedAccordingTo(Comparator.reverseOrder()); // Check descending order
		}

		@ParameterizedTest
		@ValueSource(strings = {"Gorenje", "WhirlPool"})
		void should_ReturnFilteredList_By_Manufacturer(String manufacturer) {
			// GIVEN
			saveToDB(
					TestData.createValidWashingMachine("serial1").setManufacturer("Gorenje"),
					TestData.createValidWashingMachine("serial2").setManufacturer("Gorenje"),
					TestData.createValidWashingMachine("serial3").setManufacturer("WhirlPool"),
					TestData.createValidWashingMachine("serial4").setManufacturer("WhirlPool")
			);

			// WHEN
			Page<SearchWashingMachineResponse> actual = underTest.search(
					TestData.createSearchWashingMachineRequest()
							.withPageIndex(0)
							.withPageSize(4)
							.withManufacturer(manufacturer)
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(2)
					.extracting(wm -> wm.manufacturer())
					.contains(manufacturer);
		}

		/*
			Due to how AngularMaterial table works, it is possible to send:
			sortByField: "serialNumber"
			withSortDirection(): ""
		 */
		@Test
		void should_ReturnSortedListBy_DescendingDates_When_SortDirectionIsEmpty() {
			// GIVEN
			saveToDB(
					TestData.createValidWashingMachine("serial1"),
					TestData.createValidWashingMachine("serial2"),
					TestData.createValidWashingMachine("serial3")
			);

			// WHEN
			Page<SearchWashingMachineResponse> actual = underTest.search(
					TestData.createSearchWashingMachineRequest()
							.withPageIndex(0)
							.withPageSize(3)
							.withSortByField("serialNumber")
							.withSortDirection("")
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(3)
					.extracting(washingMachine -> washingMachine.createdAt())
					.doesNotContainNull()
					.isSortedAccordingTo(Comparator.reverseOrder()); // Check descending order
		}

		@Test
		void should_ReturnSortedListBy_DescendingDates_When_SortFieldDoesNotMatchAnyProperty() {
			// GIVEN
			saveToDB(
					TestData.createValidWashingMachine("serial1"),
					TestData.createValidWashingMachine("serial2"),
					TestData.createValidWashingMachine("serial3"),
					TestData.createValidWashingMachine("serial4")
			);

			// WHEN
			Page<SearchWashingMachineResponse> actual = underTest.search(
					TestData.createSearchWashingMachineRequest()
							.withPageIndex(0)
							.withPageSize(4)
							.withSortByField("some gibberish")
							.withSortDirection("desc")
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(4)
					.extracting(washingMachine -> washingMachine.createdAt())
					.doesNotContainNull()
					.isSortedAccordingTo(Comparator.reverseOrder()); // Check descending order
		}

		@Test
		void should_ReturnSortedListBy_AscendingManufacturer() {
			// GIVEN
			saveToDB(
					TestData.createValidWashingMachine("serial1").setManufacturer("A"),
					TestData.createValidWashingMachine("serial2").setManufacturer("B"),
					TestData.createValidWashingMachine("serial3").setManufacturer("C"),
					TestData.createValidWashingMachine("serial4").setManufacturer("F")
			);

			// WHEN
			Page<SearchWashingMachineResponse> actual = underTest.search(
					TestData.createSearchWashingMachineRequest()
							.withPageIndex(0)
							.withPageSize(4)
							.withSortByField("manufacturer")
							.withSortDirection("asc")
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(4)
					.extracting(washingMachine -> washingMachine.manufacturer())
					.isSortedAccordingTo(Comparator.naturalOrder());
		}

		@Test
		void should_ReturnSortedListBy_DescendingModel() {
			// GIVEN
			saveToDB(
					TestData.createValidWashingMachine("serial1").setModel("A"),
					TestData.createValidWashingMachine("serial2").setModel("B"),
					TestData.createValidWashingMachine("serial3").setModel("C"),
					TestData.createValidWashingMachine("serial4").setModel("F")
			);

			// WHEN
			Page<SearchWashingMachineResponse> actual = underTest.search(
					TestData.createSearchWashingMachineRequest()
							.withPageIndex(0)
							.withPageSize(4)
							.withSortByField("model")
							.withSortDirection("desc")
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(4)
					.extracting(washingMachine -> washingMachine.model())
					.isSortedAccordingTo(Comparator.reverseOrder());
		}

		@Test
		void should_ReturnSortedListBy_AscendingType() {
			// GIVEN
			saveToDB(
					TestData.createValidWashingMachine("serial1").setType("A"),
					TestData.createValidWashingMachine("serial2").setType("B"),
					TestData.createValidWashingMachine("serial3").setType("C"),
					TestData.createValidWashingMachine("serial4").setType("F")
			);

			// WHEN
			Page<SearchWashingMachineResponse> actual = underTest.search(
					TestData.createSearchWashingMachineRequest()
							.withPageIndex(0)
							.withPageSize(4)
							.withSortByField("type")
							.withSortDirection("asc")
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(4)
					.extracting(washingMachine -> washingMachine.type())
					.isSortedAccordingTo(Comparator.naturalOrder());
		}

		@Test
		void should_ReturnSortedListBy_AscendingSerialNumber() {
			// GIVEN
			saveToDB(
					TestData.createValidWashingMachine("serial1"),
					TestData.createValidWashingMachine("serial2"),
					TestData.createValidWashingMachine("serial3"),
					TestData.createValidWashingMachine("serial4")
			);

			// WHEN
			Page<SearchWashingMachineResponse> actual = underTest.search(
					TestData.createSearchWashingMachineRequest()
							.withPageIndex(0)
							.withPageSize(4)
							.withSortByField("serialNumber")
							.withSortDirection("asc")
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(4)
					.extracting(washingMachine -> washingMachine.serialNumber())
					.isSortedAccordingTo(Comparator.naturalOrder());
		}

		@Test
		void should_ReturnSortedListBy_AscendingRecommendation() {
			// GIVEN
			saveToDB(
					TestData.createValidWashingMachine("serial1").setWashingMachineDetail(TestData.createWashingMachineDetailWithRecommendation(Recommendation.REPAIR)),
					TestData.createValidWashingMachine("serial2").setWashingMachineDetail(TestData.createWashingMachineDetailWithRecommendation(Recommendation.RESALE)),
					TestData.createValidWashingMachine("serial3").setWashingMachineDetail(TestData.createWashingMachineDetailWithRecommendation(Recommendation.OUTLET)),
					TestData.createValidWashingMachine("serial4").setWashingMachineDetail(TestData.createWashingMachineDetailWithRecommendation(Recommendation.REPAIR))
			);

			// WHEN
			Page<SearchWashingMachineResponse> actual = underTest.search(
					TestData.createSearchWashingMachineRequest()
							.withPageIndex(0)
							.withPageSize(4)
							.withSortByField("recommendation")
							.withSortDirection("asc")
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(4)
					.extracting(washingMachine -> washingMachine.recommendation())
					.isSortedAccordingTo(Comparator.comparing(Recommendation::name));
		}

		@Test
		void should_ReturnSortedListBy_AscendingDate() {
			// GIVEN
			saveToDB(
					TestData.createValidWashingMachine("serial1"),
					TestData.createValidWashingMachine("serial2"),
					TestData.createValidWashingMachine("serial3"),
					TestData.createValidWashingMachine("serial4")
			);

			// WHEN
			Page<SearchWashingMachineResponse> actual = underTest.search(
					TestData.createSearchWashingMachineRequest()
							.withPageIndex(0)
							.withPageSize(4)
							.withSortByField("createdAt")
							.withSortDirection("asc")
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(4)
					.extracting(washingMachine -> washingMachine.serialNumber())
					.isSortedAccordingTo(Comparator.naturalOrder());
		}

		@ParameterizedTest
		@EnumSource(DamageType.class)
		void should_ReturnFilteredList_By_DamageType(DamageType damageType) {
			// GIVEN
			saveToDB(
					TestData.createValidWashingMachine("serialOne").setDamageType(DamageType.IN_TRANSIT),
					TestData.createValidWashingMachine("serialTwo").setDamageType(DamageType.IN_TRANSIT),
					TestData.createValidWashingMachine("serialThree").setDamageType(DamageType.IN_USE),
					TestData.createValidWashingMachine("serialFour").setDamageType(DamageType.IN_USE)
			);

			// WHEN
			Page<SearchWashingMachineResponse> actual = underTest.search(
					TestData.createSearchWashingMachineRequest()
							.withPageIndex(0)
							.withPageSize(4)
							.withDamageType(damageType)
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(2)
					.extracting(wm -> wm.damageType())
					.containsOnly(damageType);
		}

		@ParameterizedTest
		@EnumSource(ReturnType.class)
		void should_ReturnFilteredList_By_ReturnType(ReturnType returnType) {
			// GIVEN
			saveToDB(
					TestData.createValidWashingMachine("serial1").setReturnType(ReturnType.COMMERCIAL),
					TestData.createValidWashingMachine("serial2").setReturnType(ReturnType.COMMERCIAL),
					TestData.createValidWashingMachine("serial3").setReturnType(ReturnType.SERVICE),
					TestData.createValidWashingMachine("serial4").setReturnType(ReturnType.SERVICE),
					TestData.createValidWashingMachine("serial5").setReturnType(ReturnType.TRANSPORT),
					TestData.createValidWashingMachine("serial6").setReturnType(ReturnType.TRANSPORT)
			);

			// WHEN
			Page<SearchWashingMachineResponse> actual = underTest.search(
					TestData.createSearchWashingMachineRequest()
							.withPageIndex(0)
							.withPageSize(6)
							.withReturnType(returnType)
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(2)
					.extracting(wm -> wm.returnType())
					.containsOnly(returnType);
		}

		@ParameterizedTest
		@EnumSource(IdentificationMode.class)
		void should_ReturnFilteredList_By_IdentificationMode(IdentificationMode identificationMode) {
			// GIVEN
			saveToDB(
					TestData.createValidWashingMachine("serial1").setIdentificationMode(IdentificationMode.QR_CODE),
					TestData.createValidWashingMachine("serial2").setIdentificationMode(IdentificationMode.QR_CODE),
					TestData.createValidWashingMachine("serial3").setIdentificationMode(IdentificationMode.DATA_MATRIX),
					TestData.createValidWashingMachine("serial4").setIdentificationMode(IdentificationMode.DATA_MATRIX)
			);

			// WHEN
			Page<SearchWashingMachineResponse> actual = underTest.search(
					TestData.createSearchWashingMachineRequest()
							.withPageIndex(0)
							.withPageSize(4)
							.withIdentificationMode(identificationMode)
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(2)
					.extracting(wm -> wm.identificationMode())
					.containsOnly(identificationMode);
		}

		@ParameterizedTest
		@ValueSource(strings = {"serial1", "serial2"})
		void should_ReturnFilteredList_By_SerialNumber(String serialNumber) {
			// GIVEN
			saveToDB(
					TestData.createValidWashingMachine("serial1"),
					TestData.createValidWashingMachine("serial2")
			);

			// WHEN
			Page<SearchWashingMachineResponse> actual = underTest.search(
					TestData.createSearchWashingMachineRequest()
							.withPageIndex(0)
							.withPageSize(4)
							.withSerialNumber(serialNumber)
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(1)
					.extracting(wm -> wm.serialNumber())
					.contains(serialNumber); // If I were to use .containsOnly, the test would fail, as it will return serial1 AND serial10.
		}

		@ParameterizedTest
		@ValueSource(strings = {"modelA", "modelB"})
		void should_ReturnFilteredList_By_Model(String model) {
			// GIVEN
			saveToDB(
					TestData.createValidWashingMachine("serial1").setModel("modelA"),
					TestData.createValidWashingMachine("serial2").setModel("modelA"),
					TestData.createValidWashingMachine("serial3").setModel("modelB"),
					TestData.createValidWashingMachine("serial4").setModel("modelB"),
					TestData.createValidWashingMachine("serial5").setModel("modelC")
			);

			// WHEN
			Page<SearchWashingMachineResponse> actual = underTest.search(
					TestData.createSearchWashingMachineRequest()
							.withPageIndex(0)
							.withPageSize(5)
							.withModel(model)
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
			saveToDB(
					TestData.createValidWashingMachine("serial1").setType("TypeY"),
					TestData.createValidWashingMachine("serial2").setType("TypeY"),
					TestData.createValidWashingMachine("serial3").setType("TypeZ"),
					TestData.createValidWashingMachine("serial4").setType("TypeZ"),
					TestData.createValidWashingMachine("serial5").setType("TypeF")
			);

			// WHEN
			Page<SearchWashingMachineResponse> actual = underTest.search(
					TestData.createSearchWashingMachineRequest()
							.withPageIndex(0)
							.withPageSize(5)
							.withType(type)
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(2)
					.extracting(wm -> wm.type())
					.contains(type);
		}

		@ParameterizedTest
		@EnumSource(value = Recommendation.class, mode = EnumSource.Mode.EXCLUDE, names = "NONE")
		void should_ReturnFilteredList_By_Recommendation(Recommendation recommendation) {
			// GIVEN
			saveToDB(
					TestData.createValidWashingMachine("serial1").setWashingMachineDetail(TestData.createWashingMachineDetailWithRecommendation(Recommendation.RESALE)),
					TestData.createValidWashingMachine("serial2").setWashingMachineDetail(TestData.createWashingMachineDetailWithRecommendation(Recommendation.RESALE)),
					TestData.createValidWashingMachine("serial3").setWashingMachineDetail(TestData.createWashingMachineDetailWithRecommendation(Recommendation.DISASSEMBLE)),
					TestData.createValidWashingMachine("serial4").setWashingMachineDetail(TestData.createWashingMachineDetailWithRecommendation(Recommendation.DISASSEMBLE)),
					TestData.createValidWashingMachine("serial5").setWashingMachineDetail(TestData.createWashingMachineDetailWithRecommendation(Recommendation.OUTLET)),
					TestData.createValidWashingMachine("serial6").setWashingMachineDetail(TestData.createWashingMachineDetailWithRecommendation(Recommendation.OUTLET)),
					TestData.createValidWashingMachine("serial7").setWashingMachineDetail(TestData.createWashingMachineDetailWithRecommendation(Recommendation.REPAIR)),
					TestData.createValidWashingMachine("serial8").setWashingMachineDetail(TestData.createWashingMachineDetailWithRecommendation(Recommendation.REPAIR)),
					TestData.createValidWashingMachine("serial9").setWashingMachineDetail(TestData.createWashingMachineDetailWithRecommendation(Recommendation.REPACKAGE)),
					TestData.createValidWashingMachine("serial10").setWashingMachineDetail(TestData.createWashingMachineDetailWithRecommendation(Recommendation.REPACKAGE))
			);

			// WHEN
			Page<SearchWashingMachineResponse> actual = underTest.search(
					TestData.createSearchWashingMachineRequest()
							.withPageIndex(0)
							.withPageSize(10)
							.withRecommendation(recommendation)
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(2)
					.extracting(wm -> wm.recommendation())
					.containsOnly(recommendation);
		}

		@Test
		void should_ReturnFilteredList_By_createdAt() {
			// GIVEN
			saveToDBOnDate(TestData.createValidWashingMachine("serial1"), LocalDate.of(2024, 12, 2));
			saveToDBOnDate(TestData.createValidWashingMachine("serial2"), LocalDate.of(2024, 12, 3));
			saveToDBOnDate(TestData.createValidWashingMachine("serial3"), LocalDate.of(2024, 12, 3));
			saveToDBOnDate(TestData.createValidWashingMachine("serial4"), LocalDate.of(2024, 12, 5));

			// WHEN
			Page<SearchWashingMachineResponse> actual = underTest.search(
					TestData.createSearchWashingMachineRequest()
							.withPageIndex(0)
							.withPageSize(4)
							.withCreatedAt("2024-12-03")
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(2)
					.extracting(wm -> wm.createdAt().toLocalDate())
					.containsOnly(LocalDate.of(2024, 12, 3));
		}

		@Test
		void should_ReturnFilteredList_By_ManufacturerAndReturnType() {
			// GIVEN
			saveToDB(
					TestData.createValidWashingMachine("serial1").setManufacturer("WhirlPool").setReturnType(ReturnType.TRANSPORT),
					TestData.createValidWashingMachine("serial2").setManufacturer("WhirlPool").setReturnType(ReturnType.TRANSPORT),
					TestData.createValidWashingMachine("serial3").setManufacturer("WhirlPool").setReturnType(ReturnType.SERVICE),
					TestData.createValidWashingMachine("serial4").setManufacturer("Bosch").setReturnType(ReturnType.COMMERCIAL)
			);

			// WHEN
			Page<SearchWashingMachineResponse> actual = underTest.search(
					TestData.createSearchWashingMachineRequest()
							.withPageIndex(0)
							.withPageSize(4)
							.withManufacturer("WhirL")
							.withReturnType(ReturnType.TRANSPORT)
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(2)
					.allSatisfy(wm -> {
						assertThat(wm.manufacturer()).containsIgnoringCase("WhirL");
						assertThat(wm.returnType()).isEqualTo(ReturnType.TRANSPORT);
					});
		}

		@Test
		void should_ReturnFilteredList_By_IdentificationModeAndModelAndType() {
			// GIVEN
			saveToDB(
					TestData.createValidWashingMachine("serial1")
							.setIdentificationMode(IdentificationMode.QR_CODE)
							.setModel("modelC")
							.setType("TypeX"),
					TestData.createValidWashingMachine("serial2")
							.setIdentificationMode(IdentificationMode.QR_CODE)
							.setModel("modelC_andMore")
							.setType("TypeX_andMore"),
					TestData.createValidWashingMachine("serial3")
							.setIdentificationMode(IdentificationMode.DATA_MATRIX)
							.setModel("modelC")
							.setType("TypeX")
			);

			// WHEN
			Page<SearchWashingMachineResponse> actual = underTest.search(
					TestData.createSearchWashingMachineRequest()
							.withPageIndex(0)
							.withPageSize(3)
							.withIdentificationMode(IdentificationMode.QR_CODE)
							.withModel("MoDElC")
							.withType("tYPeX")
			);

			// THEN
			assertThat(actual.getContent())
					.hasSize(2)
					.allSatisfy(wm -> {
						assertThat(wm.identificationMode()).isEqualTo(IdentificationMode.QR_CODE);
						assertThat(wm.model()).containsIgnoringCase("MoDElC");
						assertThat(wm.type()).containsIgnoringCase("tYPeX");
					});
		}
	}

	private void saveToDB(WashingMachine... washingMachines) {
		repository.saveAll(List.of(washingMachines));
	}

	/**
	 * Saves an entity with a specific creation date by temporarily fixing the clock.
	 *
	 * <p>Purpose:
	 * - Makes tests deterministic when using @CreatedDate.
	 * - Wraps ClockHolder set/reset for cleaner test code.
	 *
	 * <p>Note:
	 * - Clock is reset immediately after saving.
	 * - Only intended for use in tests.
	 */
	private void saveToDBOnDate(WashingMachine washingMachine, LocalDate localDate) {
		ClockHolder.setClock(ClockUtils.fixedClock(localDate));
		repository.save(washingMachine);
		ClockHolder.reset();
	}

	@Nested
	class MvcTest {

		@Test
		void should_ThrowCustomException_When_InvalidDate() throws Exception {
			// GIVEN
			// WHEN
			ResultActions resultActions = performRequest(
					TestData.createSearchWashingMachineRequest()
							.withCreatedAt("invalid date")
			);

			// THEN
			resultActions
					.andExpect(status().isBadRequest())
					.andExpect(content().string(not(containsString("Internal Translation Error"))));
		}

		@Test
		void should_ReturnStatusOk_When_SerialNumberFound() throws Exception {
			// GIVEN
			saveToDB(
					TestData.createValidWashingMachine("serial8")
			);

			// WHEN
			ResultActions resultActions = performRequest(
					TestData.createSearchWashingMachineRequest()
							.withPageIndex(0)
							.withPageSize(1)
							.withSerialNumber("serial8")
			);

			// THEN
			resultActions
					.andExpect(status().isOk())
					.andExpect(content().string(not(emptyString())));
		}

		private ResultActions performRequest(SearchWashingMachineRequest request) throws Exception {
			return mockMvc.perform(
					post("/v1/washing-machines/search")
							.content(jackson.writeValueAsString(request))
							.contentType(MediaType.APPLICATION_JSON));
		}
	}
}