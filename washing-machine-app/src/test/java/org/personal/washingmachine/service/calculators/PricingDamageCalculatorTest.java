package org.personal.washingmachine.service.calculators;

import org.junit.jupiter.api.Test;
import org.personal.washingmachine.dto.WashingMachineDetailsDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.personal.washingmachine.service.calculators.DamageLevel.*;

class PricingDamageCalculatorTest {

	private final PricingDamageCalculator underTest = new PricingDamageCalculator();

	@Test
	void should_ReturnNONE_When_PriceIsNull() {
		// GIVEN
		WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
				.price(null)
				.build();

		DamageLevel expected = NONE;

		// WHEN
		DamageLevel actual = underTest.calculate(dto);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_ReturnNONE_When_RepairPriceIsNull() {
		// GIVEN
		WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
				.repairPrice(null)
				.build();

		DamageLevel expected = NONE;

		// WHEN
		DamageLevel actual = underTest.calculate(dto);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_ReturnNONE_When_PriceIsNegative() {
		// GIVEN
		WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
				.price(-1)
				.build();

		DamageLevel expected = NONE;

		// WHEN
		DamageLevel actual = underTest.calculate(dto);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_ReturnNONE_When_RepairPriceIsNegative() {
		// GIVEN
		WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
				.repairPrice(-1)
				.build();

		DamageLevel expected = NONE;

		// WHEN
		DamageLevel actual = underTest.calculate(dto);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_ReturnDISASSEMBLE_When_RepairPriceExceedsHalfOfPrice() {
		// GIVEN
		WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
				.repairPrice(100)
				.price(50)
				.build();

		DamageLevel expected = DISASSEMBLE;

		// WHEN
		DamageLevel actual = underTest.calculate(dto);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_ReturnREPAIR_When_RepairPriceIsBelowHalfOfPrice() {
		// GIVEN
		WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
				.repairPrice(40)
				.price(100)
				.build();

		DamageLevel expected = REPAIR;

		// WHEN
		DamageLevel actual = underTest.calculate(dto);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}
}