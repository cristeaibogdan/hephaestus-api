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

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
	void checkNoDataInDB() {
		assertThat(repository.count()).isZero();
	}

	@Nested
	class IntegrationTest {

		@Test
		void should_ReturnDTO_With_CorrectProperties() {
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
							TestData.createWashingMachineDetailWithRecommendation(Recommendation.DISASSEMBLE)
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
		void should_ReturnCorrectPageSize() {
			// GIVEN
			insertIntoDB(
					TestData.createWashingMachine().setSerialNumber("serial1"),
					TestData.createWashingMachine().setSerialNumber("serial2"),
					TestData.createWashingMachine().setSerialNumber("serial3")
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
			insertIntoDB(
					TestData.createWashingMachine().setSerialNumber("serial1").setManufacturer("Gorenje"),
					TestData.createWashingMachine().setSerialNumber("serial2").setManufacturer("Gorenje"),
					TestData.createWashingMachine().setSerialNumber("serial3").setManufacturer("WhirlPool"),
					TestData.createWashingMachine().setSerialNumber("serial4").setManufacturer("WhirlPool")
			);

			// WHEN
			Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(
					searchWashingMachineRequest.toBuilder()
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
		@EnumSource(DamageType.class)
		void should_ReturnFilteredList_By_DamageType(DamageType damageType) {
			// GIVEN
			insertIntoDB(
					TestData.createWashingMachine().setSerialNumber("serialOne").setDamageType(DamageType.IN_TRANSIT),
					TestData.createWashingMachine().setSerialNumber("serialTwo").setDamageType(DamageType.IN_TRANSIT),
					TestData.createWashingMachine().setSerialNumber("serialThree").setDamageType(DamageType.IN_USE),
					TestData.createWashingMachine().setSerialNumber("serialFour").setDamageType(DamageType.IN_USE)
			);

			// WHEN
			Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(
					searchWashingMachineRequest.toBuilder()
							.pageIndex(0)
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
					TestData.createWashingMachine().setSerialNumber("serial1").setReturnType(ReturnType.COMMERCIAL),
					TestData.createWashingMachine().setSerialNumber("serial2").setReturnType(ReturnType.COMMERCIAL),
					TestData.createWashingMachine().setSerialNumber("serial3").setReturnType(ReturnType.SERVICE),
					TestData.createWashingMachine().setSerialNumber("serial4").setReturnType(ReturnType.SERVICE),
					TestData.createWashingMachine().setSerialNumber("serial5").setReturnType(ReturnType.TRANSPORT),
					TestData.createWashingMachine().setSerialNumber("serial6").setReturnType(ReturnType.TRANSPORT)
			);

			// WHEN
			Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(
					searchWashingMachineRequest.toBuilder()
							.pageIndex(0)
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
					TestData.createWashingMachine().setSerialNumber("serial1").setIdentificationMode(IdentificationMode.QR_CODE),
					TestData.createWashingMachine().setSerialNumber("serial2").setIdentificationMode(IdentificationMode.QR_CODE),
					TestData.createWashingMachine().setSerialNumber("serial3").setIdentificationMode(IdentificationMode.DATA_MATRIX),
					TestData.createWashingMachine().setSerialNumber("serial4").setIdentificationMode(IdentificationMode.DATA_MATRIX)
			);

			// WHEN
			Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(
					searchWashingMachineRequest.toBuilder()
							.pageIndex(0)
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
					TestData.createWashingMachine().setSerialNumber("serial1"),
					TestData.createWashingMachine().setSerialNumber("serial2")
			);

			// WHEN
			Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(
					searchWashingMachineRequest.toBuilder()
							.pageIndex(0)
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
					TestData.createWashingMachine().setSerialNumber("serial1").setModel("modelA"),
					TestData.createWashingMachine().setSerialNumber("serial2").setModel("modelA"),
					TestData.createWashingMachine().setSerialNumber("serial3").setModel("modelB"),
					TestData.createWashingMachine().setSerialNumber("serial4").setModel("modelB"),
					TestData.createWashingMachine().setSerialNumber("serial5").setModel("modelC")
			);

			// WHEN
			Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(
					searchWashingMachineRequest.toBuilder()
							.pageIndex(0)
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
					TestData.createWashingMachine().setSerialNumber("serial1").setType("TypeY"),
					TestData.createWashingMachine().setSerialNumber("serial2").setType("TypeY"),
					TestData.createWashingMachine().setSerialNumber("serial3").setType("TypeZ"),
					TestData.createWashingMachine().setSerialNumber("serial4").setType("TypeZ"),
					TestData.createWashingMachine().setSerialNumber("serial5").setType("TypeF")
			);

			// WHEN
			Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(
					searchWashingMachineRequest.toBuilder()
							.pageIndex(0)
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
					TestData.createWashingMachine().setSerialNumber("serial1").setWashingMachineDetail(TestData.createWashingMachineDetailWithRecommendation(Recommendation.RESALE)),
					TestData.createWashingMachine().setSerialNumber("serial2").setWashingMachineDetail(TestData.createWashingMachineDetailWithRecommendation(Recommendation.RESALE)),
					TestData.createWashingMachine().setSerialNumber("serial3").setWashingMachineDetail(TestData.createWashingMachineDetailWithRecommendation(Recommendation.DISASSEMBLE)),
					TestData.createWashingMachine().setSerialNumber("serial4").setWashingMachineDetail(TestData.createWashingMachineDetailWithRecommendation(Recommendation.DISASSEMBLE)),
					TestData.createWashingMachine().setSerialNumber("serial5").setWashingMachineDetail(TestData.createWashingMachineDetailWithRecommendation(Recommendation.OUTLET)),
					TestData.createWashingMachine().setSerialNumber("serial6").setWashingMachineDetail(TestData.createWashingMachineDetailWithRecommendation(Recommendation.OUTLET)),
					TestData.createWashingMachine().setSerialNumber("serial7").setWashingMachineDetail(TestData.createWashingMachineDetailWithRecommendation(Recommendation.REPAIR)),
					TestData.createWashingMachine().setSerialNumber("serial8").setWashingMachineDetail(TestData.createWashingMachineDetailWithRecommendation(Recommendation.REPAIR)),
					TestData.createWashingMachine().setSerialNumber("serial9").setWashingMachineDetail(TestData.createWashingMachineDetailWithRecommendation(Recommendation.REPACKAGE)),
					TestData.createWashingMachine().setSerialNumber("serial10").setWashingMachineDetail(TestData.createWashingMachineDetailWithRecommendation(Recommendation.REPACKAGE))
			);

			// WHEN
			Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(
					searchWashingMachineRequest.toBuilder()
							.pageIndex(0)
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
					TestData.createWashingMachine().setSerialNumber("serial1"),
					TestData.createWashingMachine().setSerialNumber("serial2"),
					TestData.createWashingMachine().setSerialNumber("serial3")
			);

			// WHEN
			Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(
					searchWashingMachineRequest.toBuilder()
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

		@Test
		void should_ReturnFilteredList_By_ManufacturerAndReturnType() {
			// GIVEN
			insertIntoDB(
					TestData.createWashingMachine().setSerialNumber("serial1").setManufacturer("WhirlPool").setReturnType(ReturnType.TRANSPORT),
					TestData.createWashingMachine().setSerialNumber("serial2").setManufacturer("WhirlPool").setReturnType(ReturnType.TRANSPORT),
					TestData.createWashingMachine().setSerialNumber("serial3").setManufacturer("WhirlPool").setReturnType(ReturnType.SERVICE),
					TestData.createWashingMachine().setSerialNumber("serial4").setManufacturer("Bosch").setReturnType(ReturnType.COMMERCIAL)
			);

			// WHEN
			Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(
					searchWashingMachineRequest.toBuilder()
							.pageIndex(0)
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
					TestData.createWashingMachine().setSerialNumber("serial1")
							.setIdentificationMode(IdentificationMode.QR_CODE)
							.setModel("modelC")
							.setType("TypeX"),
					TestData.createWashingMachine().setSerialNumber("serial2")
							.setIdentificationMode(IdentificationMode.QR_CODE)
							.setModel("modelC_andMore")
							.setType("TypeX_andMore"),
					TestData.createWashingMachine().setSerialNumber("serial3")
							.setIdentificationMode(IdentificationMode.DATA_MATRIX)
							.setModel("modelC")
							.setType("TypeX")
			);

			// WHEN
			Page<GetWashingMachineSimpleResponse> actual = underTest.loadPaginatedAndFiltered(
					searchWashingMachineRequest.toBuilder()
							.pageIndex(0)
							.pageSize(3)
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
					TestData.createWashingMachine().setSerialNumber("serial8")
			);

			// WHEN
			ResultActions resultActions = performRequest(
					searchWashingMachineRequest.toBuilder()
							.pageIndex(0)
							.pageSize(1)
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