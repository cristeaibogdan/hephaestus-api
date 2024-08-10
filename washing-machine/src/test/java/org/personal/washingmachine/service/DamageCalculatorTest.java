package org.personal.washingmachine.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.personal.shared.exception.CustomException;
import org.personal.washingmachine.service.calculators.HiddenSurfacesDamageCalculator;
import org.personal.washingmachine.service.calculators.PackageDamageCalculator;
import org.personal.washingmachine.service.calculators.PricingDamageCalculator;
import org.personal.washingmachine.service.calculators.VisibleSurfacesDamageCalculator;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class DamageCalculatorTest {

	@Mock
	private PackageDamageCalculator packageDamageCalculator;
	@Mock
	private VisibleSurfacesDamageCalculator visibleSurfacesDamageCalculator;
	@Mock
	private HiddenSurfacesDamageCalculator hiddenSurfacesDamageCalculator;
	@Mock
	private PricingDamageCalculator pricingDamageCalculator;

	@InjectMocks
	private WashingMachineDamageCalculator underTest;

	@Nested
	class testGetRecommendation {

		@ParameterizedTest(name = "For damageLevel = {0} should return recommendation = {1}")
		@CsvSource({
				"1, REPACKAGE",
				"2, RESALE",
				"3, RESALE",
				"4, REPAIR",
				"5, DISASSEMBLE"
		})
		void should_ReturnRecommendation(int damageLevel, String expected) {
			// GIVEN

			// WHEN
			String actual = underTest.getRecommendation(damageLevel);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest(name = "Damage level = {0} is not supported")
		@ValueSource(ints = {-2, -1, 0, 6, 7})
		void should_ThrowCustomException(int damageLevel) {
			// GIVEN

			// WHEN & THEN
			assertThatThrownBy(() -> underTest.getRecommendation(damageLevel))
					.isInstanceOf(CustomException.class);
		}
	}
}