package org.personal.washingmachine.service.calculators;

import org.junit.jupiter.api.Test;
import org.personal.washingmachine.dto.WashingMachineDetailDTO;
import org.personal.washingmachine.enums.Recommendation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.personal.washingmachine.enums.Recommendation.*;


class PackageRecommendationCalculatorTest {

	private final PackageRecommendationCalculator underTest = new PackageRecommendationCalculator();

	@Test
	void should_ReturnNONE_When_ApplicablePackageDamageIsFalse() {
		// GIVEN
		WashingMachineDetailDTO dto = WashingMachineDetailDTO.builder()
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
		WashingMachineDetailDTO washingMachineDetailDTO = WashingMachineDetailDTO.builder()
				.applicablePackageDamage(true)
				.packageMaterialAvailable(true)
				.build();

		Recommendation expected = REPACKAGE;

		// WHEN
		Recommendation actual = underTest.calculate(washingMachineDetailDTO);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_ReturnRESALE_When_PackageMaterialAvailableIsFalse() {
		// GIVEN
		WashingMachineDetailDTO washingMachineDetailDTO = WashingMachineDetailDTO.builder()
				.applicablePackageDamage(true)
				.packageMaterialAvailable(false)
				.build();

		Recommendation expected = RESALE;

		// WHEN
		Recommendation actual = underTest.calculate(washingMachineDetailDTO);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}
}