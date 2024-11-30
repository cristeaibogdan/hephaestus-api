package org.personal.washingmachine.service.calculators;

import org.junit.jupiter.api.Test;
import org.personal.washingmachine.entity.embedded.PackageDamage;
import org.personal.washingmachine.enums.Recommendation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.personal.washingmachine.enums.Recommendation.*;

class PackageRecommendationCalculatorTest {

	private final PackageRecommendationCalculator underTest = new PackageRecommendationCalculator();

	@Test
	void should_ReturnNONE_When_AllValuesAreFalse() {
		// GIVEN
		PackageDamage packageDamage = PackageDamage.builder()
				.packageDamaged(false)
				.packageDirty(false)
				.packageMaterialAvailable(false)
				.build();

		// WHEN
		Recommendation actual = underTest.calculate(packageDamage);

		// THEN
		assertThat(actual).isEqualTo(NONE);
	}

	@Test
	void should_ReturnREPACKAGE_When_PackageMaterialAvailableIsTrue() {
		// GIVEN
		PackageDamage packageDamage = PackageDamage.builder()
				.packageMaterialAvailable(true)
				.build();

		// WHEN
		Recommendation actual = underTest.calculate(packageDamage);

		// THEN
		assertThat(actual).isEqualTo(REPACKAGE);
	}

	@Test
	void should_ReturnRESALE_When_PackageMaterialAvailableIsFalse() {
		// GIVEN
		PackageDamage packageDamage = PackageDamage.builder()
				.packageDamaged(true)
				.packageMaterialAvailable(false)
				.build();

		// WHEN
		Recommendation actual = underTest.calculate(packageDamage);

		// THEN
		assertThat(actual).isEqualTo(RESALE);
	}
}