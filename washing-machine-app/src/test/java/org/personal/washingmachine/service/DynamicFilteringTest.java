package org.personal.washingmachine.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DynamicFilteringTest extends BaseIntegrationTest {
	@Autowired WashingMachineApplicationService washingMachineApplicationService;
	@Autowired WashingMachineRepository washingMachineRepository;

	@BeforeAll
	void loadDataInDB() {
		List<WashingMachine> washingMachines = new ArrayList<>();
		washingMachines.add(new WashingMachine("Washing Machine", "Gorenje", DamageType.IN_USE, ReturnType.COMMERCIAL, IdentificationMode.QR_CODE, "serial1", "model1", "Type1", Recommendation.DISASSEMBLE, null));
		washingMachines.add(new WashingMachine("Washing Machine", "Gorenje", DamageType.IN_USE, ReturnType.COMMERCIAL, IdentificationMode.QR_CODE, "serial2", "model2", "Type2", Recommendation.DISASSEMBLE, null));
		washingMachines.add(new WashingMachine("Washing Machine", "Gorenje", DamageType.IN_USE, ReturnType.COMMERCIAL, IdentificationMode.QR_CODE, "serial3", "model3", "Type3", Recommendation.DISASSEMBLE, null));
		washingMachines.add(new WashingMachine("Washing Machine", "Bosch", DamageType.IN_USE, ReturnType.COMMERCIAL, IdentificationMode.QR_CODE, "serial4", "model4", "Type4", Recommendation.DISASSEMBLE, null));
		washingMachines.add(new WashingMachine("Washing Machine", "WhirlPool", DamageType.IN_USE, ReturnType.COMMERCIAL, IdentificationMode.QR_CODE, "serial5", "model5", "Type5", Recommendation.DISASSEMBLE, null));
		washingMachineRepository.saveAll(washingMachines);
	}

	@Test
	void should_ReturnThreeWashingMachines() {
		// GIVEN
		PageRequestDTO dto = PageRequestDTO.builder()
				.pageIndex(0)
				.pageSize(3)
				.build();

		// WHEN
		Page<WashingMachineSimpleDTO> actual = washingMachineApplicationService.loadPaginatedAndFiltered(dto);

		// THEN
		assertThat(actual.getSize())
				.isEqualTo(dto.pageSize());
	}

	@Test
	void should_ReturnWashingMachinesWithSpecificManufacturer() {
		// GIVEN
		String manufacturer = "Gorenje";

		PageRequestDTO dto = PageRequestDTO.builder()
				.pageIndex(0)
				.pageSize(10)
				.manufacturer(manufacturer)
				.build();

		// WHEN
		Page<WashingMachineSimpleDTO> actual = washingMachineApplicationService.loadPaginatedAndFiltered(dto);

		// THEN
		assertThat(actual.getContent().size())
				.isEqualTo(3);

		Assertions.assertThat(actual.getContent())
				.extracting(wm -> wm.manufacturer())
				.contains(manufacturer);
	}
}
