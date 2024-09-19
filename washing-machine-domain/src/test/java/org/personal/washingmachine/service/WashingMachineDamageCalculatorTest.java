package org.personal.washingmachine.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.personal.shared.exception.CustomException;
import org.personal.washingmachine.entity.WashingMachineDetail;
import org.personal.washingmachine.entity.embedded.HiddenSurfaceDamage;
import org.personal.washingmachine.entity.embedded.PackageDamage;
import org.personal.washingmachine.entity.embedded.VisibleSurfaceDamage;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.service.calculators.HiddenSurfacesRecommendationCalculator;
import org.personal.washingmachine.service.calculators.PackageRecommendationCalculator;
import org.personal.washingmachine.service.calculators.PricingRecommendationCalculator;
import org.personal.washingmachine.service.calculators.VisibleSurfacesRecommendationCalculator;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.personal.washingmachine.enums.Recommendation.*;

class WashingMachineDamageCalculatorTest {

	WashingMachineDamageCalculator underTest = new WashingMachineDamageCalculator(
			new PackageRecommendationCalculator(),
			new VisibleSurfacesRecommendationCalculator(),
			new HiddenSurfacesRecommendationCalculator(),
			new PricingRecommendationCalculator()
	);

	@Nested
	class testGetRecommendation {

		@Test
		void should_ReturnREPACKAGE_When_PackageMaterialAvailableIsTrue() {
			// GIVEN
			WashingMachineDetail washingMachineDetail = new WashingMachineDetail(
				new PackageDamage(false, false, true),
					new VisibleSurfaceDamage(
							false,
							0,
							false,
							0,
							false,
							"",
							false,
							""
					),
					new HiddenSurfaceDamage(
							0,
							0,
							"",
							""
					),
				0,
				0
			);

			Recommendation expected = REPACKAGE;

			// WHEN
			Recommendation actual = underTest.getRecommendation(washingMachineDetail);

			// WHEN & THEN
			assertThat(actual)
					.isEqualTo(expected);
		}

		@Test
		void should_ThrowCustomException_When_DtoHasNoApplicableDamage() {
			// GIVEN
			WashingMachineDetail washingMachineDetail = new WashingMachineDetail(
					new PackageDamage(false, false, false),
					new VisibleSurfaceDamage(
							false,
							0,
							false,
							0,
							false,
							"",
							false,
							""
					),
					new HiddenSurfaceDamage(
							0,
							0,
							"",
							""
					),
					0,
					0
			);

			// WHEN & THEN
			assertThatThrownBy(() -> underTest.getRecommendation(washingMachineDetail))
					.isInstanceOf(CustomException.class);
		}
	}
}