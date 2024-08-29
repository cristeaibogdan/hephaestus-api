package org.personal.washingmachine.service.calculators;

import org.junit.jupiter.api.Test;
import org.personal.washingmachine.dto.WashingMachineDetailsDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.personal.washingmachine.service.calculators.DamageLevel.*;


class PackageDamageCalculatorTest {

	private final PackageDamageCalculator underTest = new PackageDamageCalculator();

	@Test
	void should_ReturnNONE_When_ApplicablePackageDamageIsFalse() {
		// GIVEN
		WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
				.applicablePackageDamage(false)
				.build();

		DamageLevel expected = NONE;

		// WHEN
		DamageLevel actual = underTest.calculate(dto);

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

		DamageLevel expected = REPACKAGE;

		// WHEN
		DamageLevel actual = underTest.calculate(washingMachineDetailsDTO);

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

		DamageLevel expected = RESALE;

		// WHEN
		DamageLevel actual = underTest.calculate(washingMachineDetailsDTO);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}
}