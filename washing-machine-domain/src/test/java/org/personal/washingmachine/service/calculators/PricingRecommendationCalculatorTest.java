package org.personal.washingmachine.service.calculators;

import org.junit.jupiter.api.Test;
import org.personal.washingmachine.enums.Recommendation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.personal.washingmachine.enums.Recommendation.*;

class PricingRecommendationCalculatorTest {

	private final PricingRecommendationCalculator underTest = new PricingRecommendationCalculator();

	@Test
	void should_ReturnNONE_When_OneOfThePricesIsZero() {
		// GIVEN
		int price = 0;
		int repairPrice = 0;

		Recommendation expected = NONE;

		// WHEN
		Recommendation actual = underTest.calculate(price, repairPrice);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_ReturnDISASSEMBLE_When_RepairPriceIsEqualOrExceedsHalfOfPrice() {
		// GIVEN
		int price = 50;
		int repairPrice = 100;

		Recommendation expected = DISASSEMBLE;

		// WHEN
		Recommendation actual = underTest.calculate(price, repairPrice);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_ReturnREPAIR_When_RepairPriceIsBelowHalfOfPrice() {
		// GIVEN
		int price = 100;
		int repairPrice = 40;

		Recommendation expected = REPAIR;

		// WHEN
		Recommendation actual = underTest.calculate(price, repairPrice);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}
}