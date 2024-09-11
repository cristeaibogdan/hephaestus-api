package org.personal.washingmachine.service.calculators;

import org.junit.jupiter.api.Test;
import org.personal.washingmachine.dto.WashingMachineDetailDTO;
import org.personal.washingmachine.entity.embedded.PackageDamage;
import org.personal.washingmachine.enums.Recommendation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.personal.washingmachine.enums.Recommendation.*;


class PackageRecommendationCalculatorTest {

	private final PackageRecommendationCalculator underTest = new PackageRecommendationCalculator();

	@Test
	void should_ReturnNONE_When_AllValuesAreFalse() {
		// GIVEN
		PackageDamage packageDamage = new PackageDamage(
				false,
				false,
				false);

		Recommendation expected = NONE;

		// WHEN
		Recommendation actual = underTest.calculate(packageDamage);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_ReturnREPACKAGE_When_PackageMaterialAvailableIsTrue() {
		// GIVEN
		PackageDamage packageDamage = new PackageDamage(
				false,
				false,
				true);

		Recommendation expected = REPACKAGE;

		// WHEN
		Recommendation actual = underTest.calculate(packageDamage);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_ReturnRESALE_When_PackageMaterialAvailableIsFalse() {
		// GIVEN
		PackageDamage packageDamage = new PackageDamage(
				true,
				false,
				false);

		Recommendation expected = RESALE;

		// WHEN
		Recommendation actual = underTest.calculate(packageDamage);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}
}