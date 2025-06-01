package org.personal.washingmachine.entity.embedded;

import org.junit.jupiter.api.Test;
import org.personal.washingmachine.entity.embedded.PackageDamage;
import org.personal.washingmachine.enums.Recommendation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.personal.washingmachine.enums.Recommendation.*;

class PackageDamageTest {

	@Test
	void should_ReturnNONE_When_AllValuesAreFalse() {
		// GIVEN
		PackageDamage underTest = new PackageDamage()
				.withPackageDamaged(false)
				.withPackageDirty(false)
				.withPackageMaterialAvailable(false);

		// WHEN
		Recommendation actual = underTest.calculate();

		// THEN
		assertThat(actual).isEqualTo(NONE);
	}

	@Test
	void should_ReturnREPACKAGE_When_PackageMaterialAvailableIsTrue() {
		// GIVEN
		PackageDamage underTest = new PackageDamage()
				.withPackageMaterialAvailable(true);

		// WHEN
		Recommendation actual = underTest.calculate();

		// THEN
		assertThat(actual).isEqualTo(REPACKAGE);
	}

	@Test
	void should_ReturnRESALE_When_PackageMaterialAvailableIsFalse() {
		// GIVEN
		PackageDamage underTest = new PackageDamage()
				.withPackageDamaged(true)
				.withPackageMaterialAvailable(false);

		// WHEN
		Recommendation actual = underTest.calculate();

		// THEN
		assertThat(actual).isEqualTo(RESALE);
	}
}