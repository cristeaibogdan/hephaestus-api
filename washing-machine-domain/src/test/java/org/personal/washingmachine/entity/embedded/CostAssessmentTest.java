package org.personal.washingmachine.entity.embedded;

import org.junit.jupiter.api.Test;
import org.personal.washingmachine.enums.Recommendation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.personal.washingmachine.enums.Recommendation.*;

class CostAssessmentTest {
	@Test
	void should_ReturnNONE_When_OneOfThePricesIsZero() {
		// GIVEN
		CostAssessment underTest = new CostAssessment()
				.withPrice(0)
				.withRepairPrice(0);

		// WHEN
		Recommendation actual = underTest.calculate();

		// THEN
		assertThat(actual).isEqualTo(NONE);
	}

	@Test
	void should_ReturnDISASSEMBLE_When_RepairPriceIsEqualOrExceedsHalfOfPrice() {
		// GIVEN
		CostAssessment underTest = new CostAssessment()
				.withPrice(50)
				.withRepairPrice(100);

		// WHEN
		Recommendation actual = underTest.calculate();

		// THEN
		assertThat(actual).isEqualTo(DISASSEMBLE);
	}

	@Test
	void should_ReturnREPAIR_When_RepairPriceIsBelowHalfOfPrice() {
		// GIVEN
		CostAssessment underTest = new CostAssessment()
				.withPrice(100)
				.withRepairPrice(40);

		// WHEN
		Recommendation actual = underTest.calculate();

		// THEN
		assertThat(actual).isEqualTo(REPAIR);
	}
}