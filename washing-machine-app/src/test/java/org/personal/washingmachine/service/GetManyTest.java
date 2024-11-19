package org.personal.washingmachine.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.personal.washingmachine.BaseIntegrationTest;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.enums.ReturnType;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetManyTest extends BaseIntegrationTest {

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

		washingMachineRepository.saveAll(washingMachines);
	}

	@AfterAll
	void cleanUpDB() {
		washingMachineRepository.deleteAll();
	}

	@Test
	void should_Count5ElementsInDB() {
		assertThat(washingMachineRepository.count()).isEqualTo(5);
	}

	@Test
	void should_ReturnWashingMachines_When_ProvidedValidSerialNumbers() {
		// GIVEN
		List<String> serialNumbers = new ArrayList<>(List.of(
				"serial1",
				"serial2",
				"serial3"
		));

		// WHEN
		List<WashingMachine> actual = washingMachineApplicationService.loadMany(serialNumbers);

		// THEN
		assertThat(actual)
				.isNotEmpty()
				.extracting(wm -> wm.getSerialNumber())
				.containsExactlyInAnyOrderElementsOf(serialNumbers);
	}

	@Test
	void should_ReturnWashingMachines_When_SerialNumbersContainNull() {
		// GIVEN
		List<String> serialNumbers = new ArrayList<>();
		serialNumbers.add(null);
		serialNumbers.add("serial1");
		serialNumbers.add("serial2");

		List<String> expected = serialNumbers.stream()
				.filter(sn -> Objects.nonNull(sn))
				.toList();

		// WHEN
		List<WashingMachine> actual = washingMachineApplicationService.loadMany(serialNumbers);

		// THEN
		assertThat(actual)
				.isNotEmpty()
				.extracting(wm -> wm.getSerialNumber())
				.containsExactlyInAnyOrderElementsOf(expected);
	}
}
