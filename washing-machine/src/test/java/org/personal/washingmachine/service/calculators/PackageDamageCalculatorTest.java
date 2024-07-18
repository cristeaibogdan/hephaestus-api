package org.personal.washingmachine.service.calculators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.personal.washingmachine.entity.dtos.WashingMachineDetailsDTO;

import static org.assertj.core.api.Assertions.assertThat;


class PackageDamageCalculatorTest {

	private final PackageDamageCalculator underTest = new PackageDamageCalculator();

	@Test
	void should_Return0_When_ApplicablePackageDamageIsFalse() {
		// GIVEN
		WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
				.applicablePackageDamage(false)
				.build();

		int expected = 0;

		// WHEN
		int actual = underTest.calculate(dto);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_Return1_When_PackageMaterialAvailableIsTrue() {
		// GIVEN
		WashingMachineDetailsDTO washingMachineDetailsDTO = WashingMachineDetailsDTO.builder()
				.applicablePackageDamage(true)
				.packageMaterialAvailable(true)
				.build();

		int expected = 1;

		// WHEN
		int actual = underTest.calculate(washingMachineDetailsDTO);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_Return2_When_PackageMaterialAvailableIsFalse() {
		// GIVEN
		WashingMachineDetailsDTO washingMachineDetailsDTO = WashingMachineDetailsDTO.builder()
				.applicablePackageDamage(true)
				.packageMaterialAvailable(false)
				.build();

		int expected = 2;

		// WHEN
		int actual = underTest.calculate(washingMachineDetailsDTO);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}
}