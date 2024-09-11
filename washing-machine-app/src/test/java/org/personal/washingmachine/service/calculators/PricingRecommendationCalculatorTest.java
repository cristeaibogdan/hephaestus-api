package org.personal.washingmachine.service.calculators;

import org.junit.jupiter.api.Test;
import org.personal.washingmachine.enums.Recommendation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.personal.washingmachine.enums.Recommendation.*;

class PricingRecommendationCalculatorTest {

	private final PricingRecommendationCalculator underTest = new PricingRecommendationCalculator();

	@Test
	void should_ReturnNONE_When_OneOfThePricesIsNull() {
		// GIVEN
		Integer price = null;
		Integer repairPrice = null;

		Recommendation expected = NONE;

		// WHEN
		Recommendation actual = underTest.calculate(price, repairPrice);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_ReturnNONE_When_OneOfThePricesIsNegative() {
		// GIVEN
		Integer price = -1;
		Integer repairPrice = -1;

		Recommendation expected = NONE;

		// WHEN
		Recommendation actual = underTest.calculate(price, repairPrice);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_ReturnDISASSEMBLE_When_RepairPriceIsEqualOrExceedsHalfOfPrice() {
		// GIVEN
		Integer price = 50;
		Integer repairPrice = 100;

		Recommendation expected = DISASSEMBLE;

		// WHEN
		Recommendation actual = underTest.calculate(price, repairPrice);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_ReturnREPAIR_When_RepairPriceIsBelowHalfOfPrice() {
		// GIVEN
		Integer price = 100;
		Integer repairPrice = 40;

		Recommendation expected = REPAIR;

		// WHEN
		Recommendation actual = underTest.calculate(price, repairPrice);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}
}