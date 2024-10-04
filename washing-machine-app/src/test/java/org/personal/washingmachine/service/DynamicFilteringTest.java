package org.personal.washingmachine.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.personal.washingmachine.BaseIntegrationTest;
import org.personal.washingmachine.dto.PageRequestDTO;
import org.personal.washingmachine.dto.WashingMachineSimpleDTO;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.enums.ReturnType;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DynamicFilteringTest extends BaseIntegrationTest {
	@Autowired WashingMachineApplicationService washingMachineApplicationService;
	@Autowired WashingMachineRepository washingMachineRepository;

	@BeforeAll
	void loadDataInDB() {
		List<WashingMachine> washingMachines = new ArrayList<>();
		washingMachines.add(new WashingMachine("Washing Machine", "Gorenje", DamageType.IN_USE, ReturnType.COMMERCIAL, IdentificationMode.DATA_MATRIX, "serial1", "modelA", "TypeZ", Recommendation.OUTLET, null));
		washingMachines.add(new WashingMachine("Washing Machine", "Gorenje", DamageType.IN_USE, ReturnType.COMMERCIAL, IdentificationMode.DATA_MATRIX, "serial2", "modelA", "TypeZ", Recommendation.OUTLET, null));
		washingMachines.add(new WashingMachine("Washing Machine", "Gorenje", DamageType.IN_USE, ReturnType.SERVICE, IdentificationMode.DATA_MATRIX, "serial3", "modelB", "TypeZ", Recommendation.REPACKAGE, null));
		washingMachines.add(new WashingMachine("Washing Machine", "Gorenje", DamageType.IN_USE, ReturnType.SERVICE, IdentificationMode.QR_CODE, "serial4", "modelB", "TypeX", Recommendation.REPAIR, null));
		washingMachines.add(new WashingMachine("Washing Machine", "Gorenje", DamageType.IN_USE, ReturnType.SERVICE, IdentificationMode.QR_CODE, "serial5", "modelC", "TypeX", Recommendation.REPAIR, null));

		washingMachines.add(new WashingMachine("Washing Machine", "WhirlPool", DamageType.IN_TRANSIT, ReturnType.TRANSPORT, IdentificationMode.DATA_MATRIX, "serial6", "modelD", "TypeY", Recommendation.RESALE, null));
		washingMachines.add(new WashingMachine("Washing Machine", "WhirlPool", DamageType.IN_TRANSIT, ReturnType.TRANSPORT, IdentificationMode.DATA_MATRIX, "serial7", "modelD", "TypeY", Recommendation.RESALE, null));
		washingMachines.add(new WashingMachine("Washing Machine", "WhirlPool", DamageType.IN_TRANSIT, ReturnType.TRANSPORT, IdentificationMode.DATA_MATRIX, "serial8", "modelD", "TypeY", Recommendation.DISASSEMBLE, null));
		washingMachines.add(new WashingMachine("Washing Machine", "WhirlPool", DamageType.IN_TRANSIT, ReturnType.TRANSPORT, IdentificationMode.DATA_MATRIX, "serial9", "modelD", "TypeY", Recommendation.DISASSEMBLE, null));
		washingMachines.add(new WashingMachine("Washing Machine", "WhirlPool", DamageType.IN_TRANSIT, ReturnType.TRANSPORT, IdentificationMode.DATA_MATRIX, "serial10", "modelD", "TypeY", Recommendation.DISASSEMBLE, null));

		washingMachineRepository.saveAll(washingMachines);
	}

	private final PageRequestDTO defaultPageRequest = new PageRequestDTO(
			0,
			10,
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

	@Test
	void should_ReturnThreeWashingMachines() {
		// GIVEN
		PageRequestDTO dto = defaultPageRequest.toBuilder()
				.pageIndex(0)
				.pageSize(3)
				.build();

		// WHEN
		Page<WashingMachineSimpleDTO> actual = washingMachineApplicationService.loadPaginatedAndFiltered(dto);

		// THEN
		assertThat(actual.getSize())
				.isEqualTo(dto.pageSize());
	}

	@ParameterizedTest
	@ValueSource(strings = {"Gorenje", "WhirlPool"})
	void should_ReturnFilteredList_By_Manufacturer(String manufacturer) {
		// GIVEN
		PageRequestDTO dto = defaultPageRequest.toBuilder()
				.manufacturer(manufacturer)
				.build();

		// WHEN
		Page<WashingMachineSimpleDTO> actual = washingMachineApplicationService.loadPaginatedAndFiltered(dto);

		// THEN
		assertThat(actual.getContent())
				.isNotEmpty();

		assertThat(actual.getContent())
				.extracting(wm -> wm.manufacturer())
				.contains(manufacturer);
	}

	@ParameterizedTest
	@EnumSource(DamageType.class)
	void should_ReturnFilteredList_By_DamageType(DamageType damageType) {
		// GIVEN
		PageRequestDTO dto = defaultPageRequest.toBuilder()
				.damageType(damageType)
				.build();

		// WHEN
		Page<WashingMachineSimpleDTO> actual = washingMachineApplicationService.loadPaginatedAndFiltered(dto);

		// THEN
		assertThat(actual.getContent())
				.isNotEmpty();

		assertThat(actual.getContent())
				.extracting(wm -> wm.damageType())
				.containsOnly(damageType);
	}

	@ParameterizedTest
	@EnumSource(ReturnType.class)
	void should_ReturnFilteredList_By_ReturnType(ReturnType returnType) {
		// GIVEN
		PageRequestDTO dto = defaultPageRequest.toBuilder()
				.returnType(returnType)
				.build();

		// WHEN
		Page<WashingMachineSimpleDTO> actual = washingMachineApplicationService.loadPaginatedAndFiltered(dto);

		// THEN
		assertThat(actual.getContent())
				.isNotEmpty();

		assertThat(actual.getContent())
				.extracting(wm -> wm.returnType())
				.containsOnly(returnType);
	}

	@ParameterizedTest
	@EnumSource(IdentificationMode.class)
	void should_ReturnFilteredList_By_IdentificationMode(IdentificationMode identificationMode) {
		// GIVEN
		PageRequestDTO dto = defaultPageRequest.toBuilder()
				.identificationMode(identificationMode)
				.build();

		// WHEN
		Page<WashingMachineSimpleDTO> actual = washingMachineApplicationService.loadPaginatedAndFiltered(dto);

		// THEN
		assertThat(actual.getContent())
				.isNotEmpty();

		assertThat(actual.getContent())
				.extracting(wm -> wm.identificationMode())
				.containsOnly(identificationMode);
	}

	@ParameterizedTest
	@ValueSource(strings = {"serial1", "serial2", "serial3"})
	void should_ReturnFilteredList_By_SerialNumber(String serialNumber) {
		// GIVEN
		PageRequestDTO dto = defaultPageRequest.toBuilder()
				.serialNumber(serialNumber)
				.build();

		// WHEN
		Page<WashingMachineSimpleDTO> actual = washingMachineApplicationService.loadPaginatedAndFiltered(dto);

		// THEN
		assertThat(actual.getContent())
				.isNotEmpty();

		assertThat(actual.getContent())
				.extracting(wm -> wm.serialNumber())
				.contains(serialNumber);
	}

	@ParameterizedTest
	@ValueSource(strings = {"modelA", "modelB"})
	void should_ReturnFilteredList_By_Model(String model) {
		// GIVEN
		PageRequestDTO dto = defaultPageRequest.toBuilder()
				.model(model)
				.build();

		// WHEN
		Page<WashingMachineSimpleDTO> actual = washingMachineApplicationService.loadPaginatedAndFiltered(dto);

		// THEN
		assertThat(actual.getContent())
				.isNotEmpty();

		assertThat(actual.getContent())
				.extracting(wm -> wm.model())
				.contains(model);
	}

	@ParameterizedTest
	@ValueSource(strings = {"TypeY", "TypeZ"})
	void should_ReturnFilteredList_By_Type(String type) {
		// GIVEN
		PageRequestDTO dto = defaultPageRequest.toBuilder()
				.type(type)
				.build();

		// WHEN
		Page<WashingMachineSimpleDTO> actual = washingMachineApplicationService.loadPaginatedAndFiltered(dto);

		// THEN
		assertThat(actual.getContent())
				.isNotEmpty();

		assertThat(actual.getContent())
				.extracting(wm -> wm.type())
				.contains(type);
	}

	@ParameterizedTest
	@EnumSource(value = Recommendation.class, mode = EnumSource.Mode.EXCLUDE, names = "NONE")
	void should_ReturnFilteredList_By_Recommendation(Recommendation recommendation) {
		// GIVEN
		PageRequestDTO dto = defaultPageRequest.toBuilder()
				.recommendation(recommendation)
				.build();

		// WHEN
		Page<WashingMachineSimpleDTO> actual = washingMachineApplicationService.loadPaginatedAndFiltered(dto);

		// THEN
		assertThat(actual.getContent())
				.isNotEmpty();

		assertThat(actual.getContent())
				.extracting(wm -> wm.recommendation())
				.containsOnly(recommendation);
	}

	@Test
	void should_ReturnListWithDescendingDates() {
		// GIVEN
		PageRequestDTO dto = defaultPageRequest.toBuilder()
				.build();

		// WHEN
		Page<WashingMachineSimpleDTO> actual = washingMachineApplicationService.loadPaginatedAndFiltered(dto);

		// THEN
		assertThat(actual.getContent())
				.isNotEmpty();

		assertThat(actual.getContent())
				.extracting(wm -> wm.createdAt())
				.doesNotContainNull()
				.isSortedAccordingTo(Comparator.reverseOrder()); // Check descending order
	}

	@Test
	void should_ReturnFilteredList_By_ManufacturerAndReturnType() {
		// GIVEN
		PageRequestDTO dto = defaultPageRequest.toBuilder()
				.manufacturer("WhirlPool")
				.returnType(ReturnType.TRANSPORT)
				.build();

		// WHEN
		Page<WashingMachineSimpleDTO> actual = washingMachineApplicationService.loadPaginatedAndFiltered(dto);

		// THEN
		assertThat(actual.getContent())
				.isNotEmpty();

		assertThat(actual.getContent())
				.extracting(wm -> wm.manufacturer())
				.contains(dto.manufacturer());

		assertThat(actual.getContent())
				.extracting(wm -> wm.returnType())
				.containsOnly(dto.returnType());
	}

	@Test
	void should_ReturnFilteredList_By_IdentificationModeAndModelAndType() {
		// GIVEN
		PageRequestDTO dto = defaultPageRequest.toBuilder()
				.identificationMode(IdentificationMode.QR_CODE)
				.model("modelC")
				.type("TypeX")
				.build();

		// WHEN
		Page<WashingMachineSimpleDTO> actual = washingMachineApplicationService.loadPaginatedAndFiltered(dto);

		// THEN
		assertThat(actual.getContent())
				.isNotEmpty();

		assertThat(actual.getContent())
				.extracting(wm -> wm.identificationMode())
				.containsOnly(dto.identificationMode());

		assertThat(actual.getContent())
				.extracting(wm -> wm.model())
				.contains(dto.model());

		assertThat(actual.getContent())
				.extracting(wm -> wm.type())
				.contains(dto.type());
	}

}
