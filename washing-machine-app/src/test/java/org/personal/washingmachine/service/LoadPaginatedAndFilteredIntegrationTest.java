package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
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
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class LoadPaginatedAndFilteredIntegrationTest extends BaseIntegrationTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@Autowired WashingMachineApplicationService underTest;
	@Autowired WashingMachineRepository repository;

	SearchWashingMachineRequest searchWashingMachineRequest = new SearchWashingMachineRequest(
			0,
			2,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null
	);

	@BeforeEach
	void checkInitialDataInDB() {
		assertThat(repository.count()).isZero();
	}

	@Test
	void should_ReturnDTOs_With_CorrectProperties() {
		// GIVEN
		insertIntoDB(
				new WashingMachine(
						"Washing Machine",
						"WhirlPool",
						DamageType.IN_TRANSIT,
						ReturnType.TRANSPORT,
						IdentificationMode.DATA_MATRIX,
						"serial10",
						"modelOne",
						"TypeOne",
						Recommendation.DISASSEMBLE,
						null
				)
		);

		List<GetWashingMachineSimpleResponse> expected = List.of(
				new GetWashingMachineSimpleResponse(
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
		Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(
				searchWashingMachineRequest.toBuilder()
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
	void should_ReturnThreeWashingMachines() {
		// GIVEN
		insertIntoDB(
				TestData.createWashingMachineWithSerialNumber("serial1"),
				TestData.createWashingMachineWithSerialNumber("serial2"),
				TestData.createWashingMachineWithSerialNumber("serial3")
		);

		// WHEN
		Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(
				searchWashingMachineRequest.toBuilder()
					.pageIndex(0)
					.pageSize(3)
					.build()
		);

		// THEN
		assertThat(actual.getSize())
				.isEqualTo(3);
	}

	@ParameterizedTest
	@ValueSource(strings = {"Gorenje", "WhirlPool"})
	void should_ReturnFilteredList_By_Manufacturer(String manufacturer) {
		// GIVEN
		insertIntoDB( // TODO: Lombok accessors chaining could get rid of all these "additional" TestData methods
				TestData.createWashingMachineWithManufacturer("serialOne", "Gorenje"),
				TestData.createWashingMachineWithManufacturer("serialTwo", "Gorenje"),
				TestData.createWashingMachineWithManufacturer("serialThree", "WhirlPool"),
				TestData.createWashingMachineWithManufacturer("serialFour", "WhirlPool")
		);

		// WHEN
		Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(
				searchWashingMachineRequest.toBuilder()
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
	@EnumSource(DamageType.class)
	void should_ReturnFilteredList_By_DamageType(DamageType damageType) {
		// GIVEN
		insertIntoDB(
				TestData.createWashingMachineWithDamageType("serialOne", DamageType.IN_TRANSIT),
				TestData.createWashingMachineWithDamageType("serialTwo", DamageType.IN_USE),
				TestData.createWashingMachineWithDamageType("serialThree", DamageType.IN_TRANSIT),
				TestData.createWashingMachineWithDamageType("serialFour", DamageType.IN_USE)
		);

		// WHEN
		Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(
				searchWashingMachineRequest.toBuilder()
						.pageSize(4)
						.damageType(damageType)
						.build()
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
		insertIntoDB(
				TestData.createWashingMachineWithReturnType("serial1", ReturnType.COMMERCIAL),
				TestData.createWashingMachineWithReturnType("serial2", ReturnType.COMMERCIAL),
				TestData.createWashingMachineWithReturnType("serial3", ReturnType.SERVICE),
				TestData.createWashingMachineWithReturnType("serial4", ReturnType.SERVICE),
				TestData.createWashingMachineWithReturnType("serial5", ReturnType.TRANSPORT),
				TestData.createWashingMachineWithReturnType("serial6", ReturnType.TRANSPORT)
		);

		// WHEN
		Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(
				searchWashingMachineRequest.toBuilder()
						.pageSize(6)
						.returnType(returnType)
						.build()
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
		insertIntoDB(
				TestData.createWashingMachineWithIdentificationMode("serial1", IdentificationMode.QR_CODE),
				TestData.createWashingMachineWithIdentificationMode("serial2", IdentificationMode.QR_CODE),
				TestData.createWashingMachineWithIdentificationMode("serial3", IdentificationMode.DATA_MATRIX),
				TestData.createWashingMachineWithIdentificationMode("serial4", IdentificationMode.DATA_MATRIX)
		);

		// WHEN
		Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(
				searchWashingMachineRequest.toBuilder()
						.pageSize(4)
						.identificationMode(identificationMode)
						.build()
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
		insertIntoDB(
				TestData.createWashingMachineWithSerialNumber("serial1"),
				TestData.createWashingMachineWithSerialNumber("serial2")
		);

		// WHEN
		Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(
				searchWashingMachineRequest.toBuilder()
						.pageSize(4)
						.serialNumber(serialNumber)
						.build()
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
		insertIntoDB(
				TestData.createWashingMachineWithModel("serial1", "modelA"),
				TestData.createWashingMachineWithModel("serial2", "modelA"),
				TestData.createWashingMachineWithModel("serial3", "modelB"),
				TestData.createWashingMachineWithModel("serial4", "modelB"),
				TestData.createWashingMachineWithModel("serial5", "modelC")
		);

		// WHEN
		Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(
				searchWashingMachineRequest.toBuilder()
						.pageSize(5)
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
		insertIntoDB(
				TestData.createWashingMachineWithType("serial1", "TypeY"),
				TestData.createWashingMachineWithType("serial2", "TypeY"),
				TestData.createWashingMachineWithType("serial3", "TypeZ"),
				TestData.createWashingMachineWithType("serial4", "TypeZ"),
				TestData.createWashingMachineWithType("serial5", "TypeF")
		);

		// WHEN
		Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(
				searchWashingMachineRequest.toBuilder()
						.pageSize(5)
						.type(type)
						.build()
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
		insertIntoDB(
				TestData.createWashingMachineWithRecommendation("serial1", Recommendation.RESALE),
				TestData.createWashingMachineWithRecommendation("serial2", Recommendation.RESALE),
				TestData.createWashingMachineWithRecommendation("serial3", Recommendation.DISASSEMBLE),
				TestData.createWashingMachineWithRecommendation("serial4", Recommendation.DISASSEMBLE),
				TestData.createWashingMachineWithRecommendation("serial5", Recommendation.OUTLET),
				TestData.createWashingMachineWithRecommendation("serial6", Recommendation.OUTLET),
				TestData.createWashingMachineWithRecommendation("serial7", Recommendation.REPAIR),
				TestData.createWashingMachineWithRecommendation("serial8", Recommendation.REPAIR),
				TestData.createWashingMachineWithRecommendation("serial9", Recommendation.REPACKAGE),
				TestData.createWashingMachineWithRecommendation("serial10", Recommendation.REPACKAGE)
		);

		// WHEN
		Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(
				searchWashingMachineRequest.toBuilder()
						.pageSize(10)
						.recommendation(recommendation)
						.build()
		);

		// THEN
		assertThat(actual.getContent())
				.hasSize(2)
				.extracting(wm -> wm.recommendation())
				.containsOnly(recommendation);
	}

	@Test
	void should_ReturnListWithDescendingDates() {
		// GIVEN
		insertIntoDB(
				TestData.createWashingMachineWithSerialNumber("serial1"),
				TestData.createWashingMachineWithSerialNumber("serial2"),
				TestData.createWashingMachineWithSerialNumber("serial3")
		);

		// WHEN
		Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(
				searchWashingMachineRequest.toBuilder()
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

	@Test
	void should_ReturnFilteredList_By_ManufacturerAndReturnType() {
		// GIVEN
		insertIntoDB(
				TestData.createWashingMachineWithManufacturerAndReturnType("serial1", "WhirlPool", ReturnType.TRANSPORT),
				TestData.createWashingMachineWithManufacturerAndReturnType("serial2", "WhirlPool", ReturnType.TRANSPORT),
				TestData.createWashingMachineWithManufacturerAndReturnType("serial3", "WhirlPool", ReturnType.SERVICE),
				TestData.createWashingMachineWithManufacturerAndReturnType("serial4", "Bosch", ReturnType.COMMERCIAL)
		);

		// WHEN
		Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(
				searchWashingMachineRequest.toBuilder()
					.pageSize(4)
					.manufacturer("WhirL")
					.returnType(ReturnType.TRANSPORT)
					.build()
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
		insertIntoDB(
				TestData.createWashingMachineWithIdentificationModeAndModelAndType("serial1", IdentificationMode.QR_CODE, "modelC", "TypeX"),
				TestData.createWashingMachineWithIdentificationModeAndModelAndType("serial2", IdentificationMode.QR_CODE, "modelC_andMore", "TypeX_andMore"),
				TestData.createWashingMachineWithIdentificationModeAndModelAndType("serial3", IdentificationMode.DATA_MATRIX, "modelC", "TypeX"),
				TestData.createWashingMachineWithIdentificationModeAndModelAndType("serial4", IdentificationMode.DATA_MATRIX, "modelC", "TypeX")
		);

		// WHEN
		Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(
				searchWashingMachineRequest.toBuilder()
						.pageSize(4)
						.identificationMode(IdentificationMode.QR_CODE)
						.model("MoDElC")
						.type("tYPeX")
						.build()
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

	private void insertIntoDB(WashingMachine... washingMachines) {
		repository.saveAll(List.of(washingMachines));
	}

	@Nested
	class MvcTest {

		@Test
		void should_ThrowCustomException_When_InvalidDate() throws Exception {
			// GIVEN
			// WHEN
			ResultActions resultActions = performRequest(
					searchWashingMachineRequest.toBuilder()
							.createdAt("invalid date")
							.build()
			);

			// THEN
			resultActions
					.andExpect(status().isBadRequest())
					.andExpect(content().string(not(containsString("Internal Translation Error"))));
		}

		@Test
		void should_ReturnStatusOk_When_SerialNumberFound() throws Exception {
			// GIVEN
			insertIntoDB(
					TestData.createWashingMachineWithSerialNumber("serial8")
			);

			// WHEN
			ResultActions resultActions = performRequest(
					searchWashingMachineRequest.toBuilder()
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