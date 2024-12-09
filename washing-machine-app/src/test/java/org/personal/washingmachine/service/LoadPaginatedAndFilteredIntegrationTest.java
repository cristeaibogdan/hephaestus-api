package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.personal.washingmachine.BaseIntegrationTest;
import org.personal.washingmachine.TestData;
import org.personal.washingmachine.dto.GetWashingMachineSimpleResponse;
import org.personal.washingmachine.dto.SearchWashingMachineRequest;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.enums.ReturnType;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoadPaginatedAndFilteredIntegrationTest extends BaseIntegrationTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@Autowired WashingMachineApplicationService underTest;
	@Autowired WashingMachineRepository washingMachineRepository;

	@BeforeAll
	void loadDataInDB() {
		List<WashingMachine> washingMachines = List.of(
				new WashingMachine("Washing Machine", "Gorenje", DamageType.IN_USE, ReturnType.COMMERCIAL, IdentificationMode.DATA_MATRIX, "serial1", "modelA", "TypeZ", Recommendation.OUTLET, null),
				new WashingMachine("Washing Machine", "Gorenje", DamageType.IN_USE, ReturnType.COMMERCIAL, IdentificationMode.DATA_MATRIX, "serial2", "modelA", "TypeZ", Recommendation.OUTLET, null),
				new WashingMachine("Washing Machine", "Gorenje", DamageType.IN_USE, ReturnType.SERVICE, IdentificationMode.DATA_MATRIX, "serial3", "modelB", "TypeZ", Recommendation.REPACKAGE, null),
				new WashingMachine("Washing Machine", "Gorenje", DamageType.IN_USE, ReturnType.SERVICE, IdentificationMode.QR_CODE, "serial4", "modelB", "TypeX", Recommendation.REPAIR, null),
				new WashingMachine("Washing Machine", "Gorenje", DamageType.IN_USE, ReturnType.SERVICE, IdentificationMode.QR_CODE, "serial5", "modelC", "TypeX", Recommendation.REPAIR, null),

				new WashingMachine("Washing Machine", "WhirlPool", DamageType.IN_TRANSIT, ReturnType.TRANSPORT, IdentificationMode.DATA_MATRIX, "serial6", "modelD", "TypeY", Recommendation.RESALE, null),
				new WashingMachine("Washing Machine", "WhirlPool", DamageType.IN_TRANSIT, ReturnType.TRANSPORT, IdentificationMode.DATA_MATRIX, "serial7", "modelD", "TypeY", Recommendation.RESALE, null),
				new WashingMachine("Washing Machine", "WhirlPool", DamageType.IN_TRANSIT, ReturnType.TRANSPORT, IdentificationMode.DATA_MATRIX, "serial8", "modelD", "TypeY", Recommendation.DISASSEMBLE, null),
				new WashingMachine("Washing Machine", "WhirlPool", DamageType.IN_TRANSIT, ReturnType.TRANSPORT, IdentificationMode.DATA_MATRIX, "serial9", "modelD", "TypeY", Recommendation.DISASSEMBLE, null),
				new WashingMachine("Washing Machine", "WhirlPool", DamageType.IN_TRANSIT, ReturnType.TRANSPORT, IdentificationMode.DATA_MATRIX, "serial10", "modelD", "TypeY", Recommendation.DISASSEMBLE, null)
		);

		washingMachineRepository.saveAll(washingMachines);
	}

	@AfterAll
	void cleanUpDB() {
		washingMachineRepository.deleteAll();
	}

	@Test
	void should_ReturnThreeWashingMachines() {
		// GIVEN
		SearchWashingMachineRequest dto = TestData.searchWashingMachineRequest().toBuilder()
				.pageIndex(0)
				.pageSize(3)
				.build();

		// WHEN
		Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(dto);

		// THEN
		assertThat(actual.getSize())
				.isEqualTo(dto.pageSize());
	}

	@ParameterizedTest
	@ValueSource(strings = {"Gorenje", "WhirlPool"})
	void should_ReturnFilteredList_By_Manufacturer(String manufacturer) {
		// GIVEN
		SearchWashingMachineRequest dto = TestData.searchWashingMachineRequest().toBuilder()
				.manufacturer(manufacturer)
				.build();

		// WHEN
		Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(dto);

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
		SearchWashingMachineRequest dto = TestData.searchWashingMachineRequest().toBuilder()
				.damageType(damageType)
				.build();

		// WHEN
		Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(dto);

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
		SearchWashingMachineRequest dto = TestData.searchWashingMachineRequest().toBuilder()
				.returnType(returnType)
				.build();

		// WHEN
		Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(dto);

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
		SearchWashingMachineRequest dto = TestData.searchWashingMachineRequest().toBuilder()
				.identificationMode(identificationMode)
				.build();

		// WHEN
		Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(dto);

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
		SearchWashingMachineRequest dto = TestData.searchWashingMachineRequest().toBuilder()
				.serialNumber(serialNumber)
				.build();

		// WHEN
		Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(dto);

		// THEN
		assertThat(actual.getContent())
				.isNotEmpty()
				.extracting(wm -> wm.serialNumber())
				.contains(serialNumber);
	}

	@ParameterizedTest
	@ValueSource(strings = {"modelA", "modelB"})
	void should_ReturnFilteredList_By_Model(String model) {
		// GIVEN
		SearchWashingMachineRequest dto = TestData.searchWashingMachineRequest().toBuilder()
				.model(model)
				.build();

		// WHEN
		Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(dto);

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
		SearchWashingMachineRequest dto = TestData.searchWashingMachineRequest().toBuilder()
				.type(type)
				.build();

		// WHEN
		Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(dto);

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
		SearchWashingMachineRequest dto = TestData.searchWashingMachineRequest().toBuilder()
				.recommendation(recommendation)
				.build();

		// WHEN
		Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(dto);

		// THEN
		assertThat(actual.getContent())
				.isNotEmpty()
				.extracting(wm -> wm.recommendation())
				.containsOnly(recommendation);
	}

	@Test
	void should_ReturnListWithDescendingDates() {
		// GIVEN
		SearchWashingMachineRequest dto = TestData.searchWashingMachineRequest().toBuilder()
				.pageSize(5)
				.build();

		// WHEN
		Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(dto);

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
		SearchWashingMachineRequest dto = TestData.searchWashingMachineRequest().toBuilder()
				.manufacturer("WhirL")
				.returnType(ReturnType.TRANSPORT)
				.build();

		// WHEN
		Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(dto);

		// THEN
		assertThat(actual.getContent())
				.isNotEmpty()
				.allSatisfy(wm -> {
					assertThat(wm.manufacturer()).containsIgnoringCase(dto.manufacturer());
					assertThat(wm.returnType()).isEqualTo(dto.returnType());
				});
	}

	@Test
	void should_ReturnFilteredList_By_IdentificationModeAndModelAndType() {
		// GIVEN
		SearchWashingMachineRequest dto = TestData.searchWashingMachineRequest().toBuilder()
				.identificationMode(IdentificationMode.QR_CODE)
				.model("MoDElC")
				.type("tYPeX")
				.build();

		// WHEN
		Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(dto);

		// THEN
		assertThat(actual.getContent())
				.isNotEmpty()
				.allSatisfy(wm -> {
					assertThat(wm.identificationMode()).isEqualTo(dto.identificationMode());
					assertThat(wm.model()).containsIgnoringCase(dto.model());
					assertThat(wm.type()).containsIgnoringCase(dto.type());
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
			SearchWashingMachineRequest request = TestData.searchWashingMachineRequest().toBuilder()
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
	}
}