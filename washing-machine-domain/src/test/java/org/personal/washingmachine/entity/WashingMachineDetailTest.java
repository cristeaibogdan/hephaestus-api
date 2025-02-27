package org.personal.washingmachine.entity;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.personal.shared.exception.CustomException;
import org.personal.washingmachine.entity.embedded.HiddenSurfaceDamage;
import org.personal.washingmachine.entity.embedded.PackageDamage;
import org.personal.washingmachine.entity.embedded.VisibleSurfaceDamage;
import org.personal.washingmachine.enums.Recommendation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.personal.washingmachine.enums.Recommendation.REPACKAGE;

class WashingMachineDetailTest {

	@Nested
	class testGetRecommendation {

		@Test
		void should_ReturnREPACKAGE_When_PackageMaterialAvailableIsTrue() {
			// GIVEN
			WashingMachineDetail underTest = new WashingMachineDetail(
				new PackageDamage(false, false, true),
					new VisibleSurfaceDamage(
							0,
							0,
							"",
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

			// WHEN
			Recommendation actual = underTest.getRecommendation();

			// WHEN & THEN
			assertThat(actual).isEqualTo(REPACKAGE);
		}

		@Test
		void should_ThrowCustomException_When_DtoHasNoApplicableDamage() {
			// GIVEN
			WashingMachineDetail underTest = new WashingMachineDetail(
					new PackageDamage(false, false, false),
					new VisibleSurfaceDamage(
							0,
							0,
							"",
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
			assertThatThrownBy(() -> underTest.getRecommendation())
					.isInstanceOf(CustomException.class);
		}
	}
}