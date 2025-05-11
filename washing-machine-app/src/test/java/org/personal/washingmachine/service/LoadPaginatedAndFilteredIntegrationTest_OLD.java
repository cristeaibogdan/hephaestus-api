package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.personal.washingmachine.BaseIntegrationTest;
import org.personal.washingmachine.TestData;
import org.personal.washingmachine.dto.SearchWashingMachineResponse;
import org.personal.washingmachine.dto.SearchWashingMachineRequest;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.enums.ReturnType;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Deprecated
@Disabled
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoadPaginatedAndFilteredIntegrationTest_OLD extends BaseIntegrationTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@Autowired WashingMachineApplicationService underTest;
	@Autowired WashingMachineRepository repository;

	@BeforeAll
	void loadDataInDB() {
		List<WashingMachine> washingMachines = List.of(
				new WashingMachine("Washing Machine", "Gorenje", DamageType.IN_USE, ReturnType.COMMERCIAL, IdentificationMode.DATA_MATRIX, "serial1", "modelA", "TypeZ", TestData.createWashingMachineDetailWithRecommendation(Recommendation.OUTLET)),
				new WashingMachine("Washing Machine", "Gorenje", DamageType.IN_USE, ReturnType.COMMERCIAL, IdentificationMode.DATA_MATRIX, "serial2", "modelA", "TypeZ", TestData.createWashingMachineDetailWithRecommendation(Recommendation.OUTLET)),
				new WashingMachine("Washing Machine", "Gorenje", DamageType.IN_USE, ReturnType.SERVICE, IdentificationMode.DATA_MATRIX, "serial3", "modelB", "TypeZ", TestData.createWashingMachineDetailWithRecommendation(Recommendation.REPACKAGE)),
				new WashingMachine("Washing Machine", "Gorenje", DamageType.IN_USE, ReturnType.SERVICE, IdentificationMode.QR_CODE, "serial4", "modelB", "TypeX", TestData.createWashingMachineDetailWithRecommendation(Recommendation.REPAIR)),
				new WashingMachine("Washing Machine", "Gorenje", DamageType.IN_USE, ReturnType.SERVICE, IdentificationMode.QR_CODE, "serial5", "modelC", "TypeX", TestData.createWashingMachineDetailWithRecommendation(Recommendation.REPAIR)),

				new WashingMachine("Washing Machine", "WhirlPool", DamageType.IN_TRANSIT, ReturnType.TRANSPORT, IdentificationMode.DATA_MATRIX, "serial6", "modelD", "TypeY", TestData.createWashingMachineDetailWithRecommendation(Recommendation.RESALE)),
				new WashingMachine("Washing Machine", "WhirlPool", DamageType.IN_TRANSIT, ReturnType.TRANSPORT, IdentificationMode.DATA_MATRIX, "serial7", "modelD", "TypeY", TestData.createWashingMachineDetailWithRecommendation(Recommendation.RESALE)),
				new WashingMachine("Washing Machine", "WhirlPool", DamageType.IN_TRANSIT, ReturnType.TRANSPORT, IdentificationMode.DATA_MATRIX, "serial8", "modelD", "TypeY", TestData.createWashingMachineDetailWithRecommendation(Recommendation.DISASSEMBLE)),
				new WashingMachine("Washing Machine", "WhirlPool", DamageType.IN_TRANSIT, ReturnType.TRANSPORT, IdentificationMode.DATA_MATRIX, "serial9", "modelD", "TypeY", TestData.createWashingMachineDetailWithRecommendation(Recommendation.DISASSEMBLE)),
				new WashingMachine("Washing Machine", "WhirlPool", DamageType.IN_TRANSIT, ReturnType.TRANSPORT, IdentificationMode.DATA_MATRIX, "serial10", "modelD", "TypeY", TestData.createWashingMachineDetailWithRecommendation(Recommendation.DISASSEMBLE))
		);

		repository.saveAll(washingMachines);
	}

	@AfterAll
	void cleanUpDB() {
		repository.deleteAll();
	}

	@BeforeEach
	void checkInitialDataInDB() {
		assertThat(repository.count()).isEqualTo(10);
	}

	@Test
	void should_ReturnDTOs_With_CorrectProperties() {
		// GIVEN
		List<SearchWashingMachineResponse> expected = List.of(
				new SearchWashingMachineResponse(
						"Washing Machine",
						"WhirlPool",
						IdentificationMode.DATA_MATRIX,
						"modelD",
						"TypeY",
						"serial10",
						ReturnType.TRANSPORT,
						DamageType.IN_TRANSIT,
						Recommendation.DISASSEMBLE,
						LocalDateTime.now())
		);

		// WHEN
		Page<SearchWashingMachineResponse> actual = underTest.loadPaginatedAndFiltered(
				TestData.createSearchWashingMachineRequest().toBuilder()
						.pageIndex(0)
						.pageSize(1)
						.serialNumber("serial10")
						.build()
		);

		// THEN
		assertThat(actual.getContent())
				.usingRecursiveComparison()
				.ignoringFields("createdAt")
				.isEqualTo(expected);
	}

	@Test
	void should_ReturnTenWashingMachines() {
		// GIVEN
		// WHEN
		Page<SearchWashingMachineResponse> actual = underTest.loadPaginatedAndFiltered(
				TestData.createSearchWashingMachineRequest().toBuilder()
						.pageIndex(0)
						.pageSize(10)
						.build()
		);

		// THEN
		assertThat(actual.getSize())
				.isEqualTo(10);
	}

	@ParameterizedTest
	@ValueSource(strings = {"Gorenje", "WhirlPool"})
	void should_ReturnFilteredList_By_Manufacturer(String manufacturer) {
		// GIVEN
		// WHEN
		Page<SearchWashingMachineResponse> actual = underTest.loadPaginatedAndFiltered(
				TestData.createSearchWashingMachineRequest().toBuilder()
						.pageSize(10)
						.manufacturer(manufacturer)
						.build()
		);

		// THEN
		assertThat(actual.getContent())
				.isNotEmpty()
				.extracting(wm -> wm.manufacturer())
				.contains(manufacturer);
	}

	@ParameterizedTest
	@EnumSource(DamageType.class)
	void should_ReturnFilteredList_By_DamageType(DamageType damageType) {
		// GIVEN
		// WHEN
		Page<SearchWashingMachineResponse> actual = underTest.loadPaginatedAndFiltered(
				TestData.createSearchWashingMachineRequest().toBuilder()
						.pageSize(10)
						.damageType(damageType)
						.build()
		);

		// THEN
		assertThat(actual.getContent())
				.isNotEmpty()
				.extracting(wm -> wm.damageType())
				.containsOnly(damageType);
	}

	@ParameterizedTest
	@EnumSource(ReturnType.class)
	void should_ReturnFilteredList_By_ReturnType(ReturnType returnType) {
		// GIVEN
		// WHEN
		Page<SearchWashingMachineResponse> actual = underTest.loadPaginatedAndFiltered(
				TestData.createSearchWashingMachineRequest().toBuilder()
						.pageSize(10)
						.returnType(returnType)
						.build()
		);

		// THEN
		assertThat(actual.getContent())
				.isNotEmpty()
				.extracting(wm -> wm.returnType())
				.containsOnly(returnType);
	}

	@ParameterizedTest
	@EnumSource(IdentificationMode.class)
	void should_ReturnFilteredList_By_IdentificationMode(IdentificationMode identificationMode) {
		// GIVEN
		// WHEN
		Page<SearchWashingMachineResponse> actual = underTest.loadPaginatedAndFiltered(
				TestData.createSearchWashingMachineRequest().toBuilder()
						.pageSize(10)
						.identificationMode(identificationMode)
						.build()
		);

		// THEN
		assertThat(actual.getContent())
				.isNotEmpty()
				.extracting(wm -> wm.identificationMode())
				.containsOnly(identificationMode);
	}

	@ParameterizedTest
	@ValueSource(strings = {"serial1", "serial2", "serial3"})
	void should_ReturnFilteredList_By_SerialNumber(String serialNumber) {
		// GIVEN
		// WHEN
		Page<SearchWashingMachineResponse> actual = underTest.loadPaginatedAndFiltered(
				TestData.createSearchWashingMachineRequest().toBuilder()
						.pageSize(10)
						.serialNumber(serialNumber)
						.build()
		);

		// THEN
		assertThat(actual.getContent())
				.isNotEmpty()
				.extracting(wm -> wm.serialNumber())
				.contains(serialNumber); // If I were to use .containsOnly, the test would fail, as it will return serial1 AND serial10.
	}

	@ParameterizedTest
	@ValueSource(strings = {"modelA", "modelB"})
	void should_ReturnFilteredList_By_Model(String model) {
		// GIVEN
		// WHEN
		Page<SearchWashingMachineResponse> actual = underTest.loadPaginatedAndFiltered(
				TestData.createSearchWashingMachineRequest().toBuilder()
						.pageSize(10)
						.model(model)
						.build()
		);

		// THEN
		assertThat(actual.getContent())
				.isNotEmpty()
				.extracting(wm -> wm.model())
				.contains(model);
	}

	@ParameterizedTest
	@ValueSource(strings = {"TypeY", "TypeZ"})
	void should_ReturnFilteredList_By_Type(String type) {
		// GIVEN
		// WHEN
		Page<SearchWashingMachineResponse> actual = underTest.loadPaginatedAndFiltered(
				TestData.createSearchWashingMachineRequest().toBuilder()
						.pageSize(10)
						.type(type)
						.build()
		);

		// THEN
		assertThat(actual.getContent())
				.isNotEmpty()
				.extracting(wm -> wm.type())
				.contains(type);
	}

	@ParameterizedTest
	@EnumSource(value = Recommendation.class, mode = EnumSource.Mode.EXCLUDE, names = "NONE")
	void should_ReturnFilteredList_By_Recommendation(Recommendation recommendation) {
		// GIVEN
		// WHEN
		Page<SearchWashingMachineResponse> actual = underTest.loadPaginatedAndFiltered(
				TestData.createSearchWashingMachineRequest().toBuilder()
						.pageSize(10)
						.recommendation(recommendation)
						.build()
		);

		// THEN
		assertThat(actual.getContent())
				.isNotEmpty()
				.extracting(wm -> wm.recommendation())
				.containsOnly(recommendation);
	}

	@Test
	void should_ReturnListWithDescendingDates() {
		// GIVEN
		// WHEN
		Page<SearchWashingMachineResponse> actual = underTest.loadPaginatedAndFiltered(
				TestData.createSearchWashingMachineRequest().toBuilder()
						.pageSize(10)
						.build()
		);

		// THEN
		assertThat(actual.getContent())
				.isNotEmpty()
				.extracting(wm -> wm.createdAt())
				.doesNotContainNull()
				.isSortedAccordingTo(Comparator.reverseOrder()); // Check descending order
	}

	@Test
	void should_ReturnFilteredList_By_ManufacturerAndReturnType() {
		// GIVEN
		// WHEN
		Page<SearchWashingMachineResponse> actual = underTest.loadPaginatedAndFiltered(
				TestData.createSearchWashingMachineRequest().toBuilder()
						.pageSize(10)
						.manufacturer("WhirL")
						.returnType(ReturnType.TRANSPORT)
						.build()
		);

		// THEN
		assertThat(actual.getContent())
				.isNotEmpty()
				.allSatisfy(wm -> {
					assertThat(wm.manufacturer()).containsIgnoringCase("WhirL");
					assertThat(wm.returnType()).isEqualTo(ReturnType.TRANSPORT);
				});
	}

	@Test
	void should_ReturnFilteredList_By_IdentificationModeAndModelAndType() {
		// GIVEN
		// WHEN
		Page<SearchWashingMachineResponse> actual = underTest.loadPaginatedAndFiltered(
				TestData.createSearchWashingMachineRequest().toBuilder()
						.pageSize(10)
						.identificationMode(IdentificationMode.QR_CODE)
						.model("MoDElC")
						.type("tYPeX")
						.build()
		);

		// THEN
		assertThat(actual.getContent())
				.isNotEmpty()
				.allSatisfy(wm -> {
					assertThat(wm.identificationMode()).isEqualTo(IdentificationMode.QR_CODE);
					assertThat(wm.model()).containsIgnoringCase("MoDElC");
					assertThat(wm.type()).containsIgnoringCase("tYPeX");
				});
	}

	@Nested
	class MvcTest {

		private static final String[] availableLocales = {"en", "es", "it", "ro", "sl"};

		private static Stream<String> localeProvider() {
			return Stream.of(availableLocales);
		}

		@ParameterizedTest
		@MethodSource("localeProvider")
		void should_ThrowCustomException_When_InvalidDate(String locale) throws Exception {
			// GIVEN
			SearchWashingMachineRequest request = TestData.createSearchWashingMachineRequest().toBuilder()
					.createdAt("invalid date")
					.build();

			// WHEN
			ResultActions resultActions = mockMvc.perform(
					post("/api/v1/washing-machines")
							.content(jackson.writeValueAsString(request))
							.contentType(MediaType.APPLICATION_JSON)
							.header("Accept-Language", locale));

			// THEN
			resultActions
					.andExpect(status().isBadRequest())
					.andExpect(content().string(not(containsString("Internal Translation Error"))));
		}

		@Test
		void should_ReturnStatusOk_When_SerialNumberFound() throws Exception {
			// GIVEN
			// WHEN
			ResultActions resultActions = performRequest(
					TestData.createSearchWashingMachineRequest().toBuilder()
							.pageSize(10)
							.serialNumber("serial8")
							.build()
			);

			// THEN
			resultActions
					.andExpect(status().isOk())
					.andExpect(content().string(not(emptyString())));
		}

		private ResultActions performRequest(SearchWashingMachineRequest request) throws Exception {
			return mockMvc.perform(
					post("/api/v1/washing-machines")
							.content(jackson.writeValueAsString(request))
							.contentType(MediaType.APPLICATION_JSON));
		}
	}
}