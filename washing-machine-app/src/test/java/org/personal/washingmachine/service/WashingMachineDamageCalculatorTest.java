package org.personal.washingmachine.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.personal.shared.exception.CustomException;
import org.personal.washingmachine.dto.WashingMachineDetailsDTO;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.service.calculators.HiddenSurfacesRecommendationCalculator;
import org.personal.washingmachine.service.calculators.PackageRecommendationCalculator;
import org.personal.washingmachine.service.calculators.PricingRecommendationCalculator;
import org.personal.washingmachine.service.calculators.VisibleSurfacesRecommendationCalculator;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class WashingMachineDamageCalculatorTest {

	@Spy
	private PackageRecommendationCalculator packageRecommendationCalculator;
	@Spy
	private VisibleSurfacesRecommendationCalculator visibleSurfacesRecommendationCalculator;
	@Spy
	private HiddenSurfacesRecommendationCalculator hiddenSurfacesRecommendationCalculator;
	@Spy
	private PricingRecommendationCalculator pricingRecommendationCalculator;

	@InjectMocks
	private WashingMachineDamageCalculator underTest;

	@Nested
	class testGetRecommendation {

		@Test
		void should_ReturnREPACKAGE_When_PackageMaterialAvailableIsTrue() {
			// GIVEN
			WashingMachineDetailsDTO dto = new WashingMachineDetailsDTO(
					true,
					true,
					false,
					true,
					false,
					false,
					0,
					false,
					0,
					false,
					null,
					false,
					null,
					false,
					false,
					0,
					false,
					0,
					false,
					null,
					false,
					null,
					0,
					0
			);

			Recommendation expected = Recommendation.REPACKAGE;

			// WHEN
			Recommendation actual = underTest.getRecommendation(dto);

			// WHEN & THEN
			assertThat(actual)
					.isEqualTo(expected);
		}

		@Test
		void should_ThrowCustomException_When_DtoHasNoApplicableDamage() {
			// GIVEN
			WashingMachineDetailsDTO dto = new WashingMachineDetailsDTO(
					false,
					false,
					false,
					false,
					false,
					false,
					0,
					false,
					0,
					false,
					null,
					false,
					null,
					false,
					false,
					0,
					false,
					0,
					false,
					null,
					false,
					null,
					0,
					0
			);

			// WHEN & THEN
			assertThatThrownBy(() -> underTest.getRecommendation(dto))
					.isInstanceOf(CustomException.class);
		}
	}
}