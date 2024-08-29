package org.personal.washingmachine.service.calculators;

import org.junit.jupiter.api.Test;
import org.personal.washingmachine.dto.WashingMachineDetailsDTO;
import org.personal.washingmachine.enums.Recommendation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.personal.washingmachine.enums.Recommendation.*;


class PackageDamageCalculatorTest {

	private final PackageDamageCalculator underTest = new PackageDamageCalculator();

	@Test
	void should_ReturnNONE_When_ApplicablePackageDamageIsFalse() {
		// GIVEN
		WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
				.applicablePackageDamage(false)
				.build();

		Recommendation expected = NONE;

		// WHEN
		Recommendation actual = underTest.calculate(dto);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_ReturnREPACKAGE_When_PackageMaterialAvailableIsTrue() {
		// GIVEN
		WashingMachineDetailsDTO washingMachineDetailsDTO = WashingMachineDetailsDTO.builder()
				.applicablePackageDamage(true)
				.packageMaterialAvailable(true)
				.build();

		Recommendation expected = REPACKAGE;

		// WHEN
		Recommendation actual = underTest.calculate(washingMachineDetailsDTO);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_ReturnRESALE_When_PackageMaterialAvailableIsFalse() {
		// GIVEN
		WashingMachineDetailsDTO washingMachineDetailsDTO = WashingMachineDetailsDTO.builder()
				.applicablePackageDamage(true)
				.packageMaterialAvailable(false)
				.build();

		Recommendation expected = RESALE;

		// WHEN
		Recommendation actual = underTest.calculate(washingMachineDetailsDTO);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}
}