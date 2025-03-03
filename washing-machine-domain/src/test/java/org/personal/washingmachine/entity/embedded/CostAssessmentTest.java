package org.personal.washingmachine.entity.embedded;

import org.junit.jupiter.api.Test;
import org.personal.washingmachine.enums.Recommendation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.personal.washingmachine.enums.Recommendation.*;

class CostAssessmentTest {
	@Test
	void should_ReturnNONE_When_OneOfThePricesIsZero() {
		// GIVEN
		CostAssessment underTest = CostAssessment.builder()
				.price(0)
				.repairPrice(0)
				.build();

		// WHEN
		Recommendation actual = underTest.calculate();

		// THEN
		assertThat(actual).isEqualTo(NONE);
	}

	@Test
	void should_ReturnDISASSEMBLE_When_RepairPriceIsEqualOrExceedsHalfOfPrice() {
		// GIVEN
		CostAssessment underTest = CostAssessment.builder()
				.price(50)
				.repairPrice(100)
				.build();

		// WHEN
		Recommendation actual = underTest.calculate();

		// THEN
		assertThat(actual).isEqualTo(DISASSEMBLE);
	}

	@Test
	void should_ReturnREPAIR_When_RepairPriceIsBelowHalfOfPrice() {
		// GIVEN
		CostAssessment underTest = CostAssessment.builder()
				.price(100)
				.repairPrice(40)
				.build();

		// WHEN
		Recommendation actual = underTest.calculate();

		// THEN
		assertThat(actual).isEqualTo(REPAIR);
	}
}