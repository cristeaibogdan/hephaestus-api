package org.personal.washingmachine.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.personal.shared.exception.CustomException;
import org.personal.washingmachine.dto.WashingMachineDetailsDTO;
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
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.applicablePackageDamage(true)
					.packageMaterialAvailable(true)
					.build();

			Recommendation expected = REPACKAGE;

			// WHEN
			Recommendation actual = underTest.getRecommendation(dto);

			// WHEN & THEN
			assertThat(actual)
					.isEqualTo(expected);
		}

		@Test
		void should_ThrowCustomException_When_DtoHasNoApplicableDamage() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.applicablePackageDamage(false)
					.applicableHiddenSurfacesDamage(false)
					.applicableVisibleSurfacesDamage(false)
					.build();

			// WHEN & THEN
			assertThatThrownBy(() -> underTest.getRecommendation(dto))
					.isInstanceOf(CustomException.class);
		}
	}
}