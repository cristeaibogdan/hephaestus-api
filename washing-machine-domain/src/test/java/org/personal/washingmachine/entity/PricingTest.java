package org.personal.washingmachine.entity;

import org.junit.jupiter.api.Test;
import org.personal.washingmachine.enums.Recommendation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.personal.washingmachine.enums.Recommendation.*;

class PricingTest {
	@Test
	void should_ReturnNONE_When_OneOfThePricesIsZero() {
		// GIVEN
		WashingMachineDetail underTest = WashingMachineDetail.builder()
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
		WashingMachineDetail underTest = WashingMachineDetail.builder()
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
		WashingMachineDetail underTest = WashingMachineDetail.builder()
				.price(100)
				.repairPrice(40)
				.build();

		// WHEN
		Recommendation actual = underTest.calculate();

		// THEN
		assertThat(actual).isEqualTo(REPAIR);
	}
}