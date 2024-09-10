package org.personal.washingmachine.service.calculators;

import org.junit.jupiter.api.Test;
import org.personal.washingmachine.dto.WashingMachineDetailDTO;
import org.personal.washingmachine.enums.Recommendation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.personal.washingmachine.enums.Recommendation.*;

class PricingRecommendationCalculatorTest {

	private final PricingRecommendationCalculator underTest = new PricingRecommendationCalculator();

	@Test
	void should_ReturnNONE_When_PriceIsNull() {
		// GIVEN
		WashingMachineDetailDTO dto = WashingMachineDetailDTO.builder()
				.price(null)
				.build();

		Recommendation expected = NONE;

		// WHEN
		Recommendation actual = underTest.calculate(dto);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_ReturnNONE_When_RepairPriceIsNull() {
		// GIVEN
		WashingMachineDetailDTO dto = WashingMachineDetailDTO.builder()
				.repairPrice(null)
				.build();

		Recommendation expected = NONE;

		// WHEN
		Recommendation actual = underTest.calculate(dto);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_ReturnNONE_When_PriceIsNegative() {
		// GIVEN
		WashingMachineDetailDTO dto = WashingMachineDetailDTO.builder()
				.price(-1)
				.repairPrice(0)
				.build();

		Recommendation expected = NONE;

		// WHEN
		Recommendation actual = underTest.calculate(dto);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_ReturnNONE_When_RepairPriceIsNegative() {
		// GIVEN
		WashingMachineDetailDTO dto = WashingMachineDetailDTO.builder()
				.price(0)
				.repairPrice(-1)
				.build();

		Recommendation expected = NONE;

		// WHEN
		Recommendation actual = underTest.calculate(dto);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_ReturnDISASSEMBLE_When_RepairPriceExceedsHalfOfPrice() {
		// GIVEN
		WashingMachineDetailDTO dto = WashingMachineDetailDTO.builder()
				.repairPrice(100)
				.price(50)
				.build();

		Recommendation expected = DISASSEMBLE;

		// WHEN
		Recommendation actual = underTest.calculate(dto);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void should_ReturnREPAIR_When_RepairPriceIsBelowHalfOfPrice() {
		// GIVEN
		WashingMachineDetailDTO dto = WashingMachineDetailDTO.builder()
				.repairPrice(40)
				.price(100)
				.build();

		Recommendation expected = REPAIR;

		// WHEN
		Recommendation actual = underTest.calculate(dto);

		// THEN
		assertThat(actual).isEqualTo(expected);
	}
}