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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

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
		Map<String, WashingMachine> actual = washingMachineApplicationService.loadMany(serialNumbers);

		// THEN
		assertThat(actual)
				.isNotEmpty()
				.containsOnlyKeys(serialNumbers)
				.extractingFromEntries(entry -> entry.getValue().getSerialNumber())
				.containsExactlyInAnyOrderElementsOf(serialNumbers);
	}

	@Test
	void should_ReturnWashingMachines_When_SerialNumbersContainNull() {
		// GIVEN
		List<String> serialNumbers = new ArrayList<>();
		serialNumbers.add(null);
		serialNumbers.add("serial1");
		serialNumbers.add("serial2");

		List<String> expectedSerialNumbers = serialNumbers.stream()
				.filter(sn -> Objects.nonNull(sn))
				.toList();

		// WHEN
		Map<String, WashingMachine> actual = washingMachineApplicationService.loadMany(serialNumbers);

		// THEN
		assertThat(actual)
				.isNotEmpty()
				.containsOnlyKeys(expectedSerialNumbers)
				.extractingFromEntries(entry -> entry.getValue().getSerialNumber())
				.containsExactlyInAnyOrderElementsOf(expectedSerialNumbers);
	}

	@Test
	void should_ReturnNullWashingMachines_When_SerialNumbersAreNotFound() {
		// GIVEN
		List<String> serialNumbers = new ArrayList<>(List.of(
				"I don't exist",
				"serial1",
				"serial2",
				"serial3",
				"serial4",
				"Can't find me",
				"Nothing"
		));

		List<String> notFoundSerialNumbers = new ArrayList<>(List.of(
				"I don't exist",
				"Can't find me",
				"Nothing"
		));

		// WHEN
		Map<String, WashingMachine> actual = washingMachineApplicationService.loadMany(serialNumbers);

		// THEN
		assertThat(actual)
				.isNotEmpty()
				.containsOnlyKeys(serialNumbers);

		assertThat(notFoundSerialNumbers)
				.allSatisfy(sn -> assertThat(actual).containsEntry(sn, null));
	}
}
