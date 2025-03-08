package org.personal.solarpanel.entity;

import org.junit.jupiter.api.Test;
import org.personal.shared.exception.CustomException;
import org.personal.solarpanel.enums.Recommendation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DamageTest {

	@Test
	void should_ThrowException_When_NoDamage() {
		// GIVEN
		Damage damage = new Damage(
				false,
				false,
				false,
				false,
				""
		);
		// WHEN
		// THEN
		assertThatThrownBy(() -> damage.calculate())
				.isInstanceOf(CustomException.class);
	}

	@Test
	void should_ReturnRepair_When_OneDamageFlagsIsTrue() {
		// GIVEN
		Damage damage = new Damage(
				true,
				false,
				false,
				false,
				""
		);
		// WHEN
		Recommendation actual = damage.calculate();

		// THEN
		assertThat(actual).isEqualTo(Recommendation.REPAIR);
	}

	@Test
	void should_ReturnRepair_When_TwoDamageFlagsAreTrue() {
		// GIVEN
		Damage damage = new Damage(
				true,
				true,
				false,
				false,
				""
		);
		// WHEN
		Recommendation actual = damage.calculate();

		// THEN
		assertThat(actual).isEqualTo(Recommendation.REPAIR);
	}

	@Test
	void should_ReturnRecycle_When_ThreeDamageFlagsAreTrue() {
		// GIVEN
		Damage damage = new Damage(
				true,
				true,
				true,
				false,
				""
		);
		// WHEN
		Recommendation actual = damage.calculate();

		// THEN
		assertThat(actual).isEqualTo(Recommendation.RECYCLE);
	}

	@Test
	void should_ReturnRecycle_When_AllDamageFlagsAreTrue() {
		// GIVEN
		Damage damage = new Damage(
				true,
				true,
				true,
				true,
				""
		);
		// WHEN
		Recommendation actual = damage.calculate();

		// THEN
		assertThat(actual).isEqualTo(Recommendation.DISPOSE);
	}

}