package org.personal.washingmachine.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.personal.shared.exception.CustomException;
import org.personal.washingmachine.dto.WashingMachineDetailsDTO;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.service.calculators.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class WashingMachineDamageCalculatorTest {

	@Mock
	private PackageRecommendationCalculator packageRecommendationCalculator;
	@Mock
	private VisibleSurfacesRecommendationCalculator visibleSurfacesRecommendationCalculator;
	@Mock
	private HiddenSurfacesRecommendationCalculator hiddenSurfacesRecommendationCalculator;
	@Mock
	private PricingRecommendationCalculator pricingRecommendationCalculator;

	@InjectMocks
	private WashingMachineDamageCalculator underTest;

	@Nested
	class testGetRecommendation {

		@Test
		void should_ThrowCustomException() {
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

			given(packageRecommendationCalculator.calculate(dto))
					.willReturn(Recommendation.NONE);
			given(visibleSurfacesRecommendationCalculator.calculate(dto))
					.willReturn(Recommendation.NONE);
			given(hiddenSurfacesRecommendationCalculator.calculate(dto))
					.willReturn(Recommendation.NONE);
			given(pricingRecommendationCalculator.calculate(dto))
					.willReturn(Recommendation.NONE);

			// WHEN & THEN
			assertThatThrownBy(() -> underTest.getRecommendation(dto))
					.isInstanceOf(CustomException.class);
		}
	}
}