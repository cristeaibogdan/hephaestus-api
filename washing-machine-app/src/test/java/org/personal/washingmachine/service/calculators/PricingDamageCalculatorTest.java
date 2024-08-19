package org.personal.washingmachine.service.calculators;

import org.junit.jupiter.api.Test;
import org.personal.washingmachine.dto.WashingMachineDetailsDTO;

import static org.assertj.core.api.Assertions.assertThat;

class PricingDamageCalculatorTest {

	private final PricingDamageCalculator underTest = new PricingDamageCalculator();

	@Test
	void should_Return0_When_PriceIsNull() {
		// GIVEN
		WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
				.price(null)
				.build();

		int expected = 0;

		// WHEN
		int actual = underTest.calculate(dto);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_Return0_When_RepairPriceIsNull() {
		// GIVEN
		WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
				.repairPrice(null)
				.build();

		int expected = 0;

		// WHEN
		int actual = underTest.calculate(dto);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_Return0_When_PriceIsNegative() {
		// GIVEN
		WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
				.price(-1)
				.build();

		int expected = 0;

		// WHEN
		int actual = underTest.calculate(dto);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_Return0_When_RepairPriceIsNegative() {
		// GIVEN
		WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
				.repairPrice(-1)
				.build();

		int expected = 0;

		// WHEN
		int actual = underTest.calculate(dto);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_Return5_When_RepairPriceExceedsHalfOfPrice() {
		// GIVEN
		WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
				.repairPrice(100)
				.price(50)
				.build();

		int expected = 5;

		// WHEN
		int actual = underTest.calculate(dto);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_Return4_When_RepairPriceIsBelowHalfOfPrice() {
		// GIVEN
		WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
				.repairPrice(40)
				.price(100)
				.build();

		int expected = 4;

		// WHEN
		int actual = underTest.calculate(dto);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}
}