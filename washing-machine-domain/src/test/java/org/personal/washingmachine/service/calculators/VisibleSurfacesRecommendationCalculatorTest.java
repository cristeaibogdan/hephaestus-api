package org.personal.washingmachine.service.calculators;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.personal.washingmachine.entity.embedded.VisibleSurfaceDamage;
import org.personal.washingmachine.enums.Recommendation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.personal.washingmachine.enums.Recommendation.*;

class VisibleSurfacesRecommendationCalculatorTest {

	private final VisibleSurfacesRecommendationCalculator underTest = new VisibleSurfacesRecommendationCalculator();

	@Nested
	class testCalculate {

		@Test
		void should_ReturnNONE_When_AllValuesAreFalse() {
			// GIVEN
			VisibleSurfaceDamage visibleSurfaceDamage = new VisibleSurfaceDamage(
					false,
					0,
					false,
					0,
					false,
					"",
					false,
					""
			);

			Recommendation expected = NONE;

			// WHEN
			Recommendation actual = underTest.calculate(visibleSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		void should_ReturnRESALE_When_MajorDamageTrue() {
			// GIVEN
			VisibleSurfaceDamage visibleSurfaceDamage = new VisibleSurfaceDamage(
					false,
					0,
					false,
					0,
					false,
					"",
					true,
					"I'm the tested property"
			);

			Recommendation expected = OUTLET;

			// WHEN
			Recommendation actual = underTest.calculate(visibleSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class testCalculateForScratches {

		@Test
		void should_ReturnNONE_When_VisibleSurfacesHasScratchesIsFalse() {
			// GIVEN
			VisibleSurfaceDamage visibleSurfaceDamage = new VisibleSurfaceDamage(
					false,
					0,
					false,
					0,
					false,
					"",
					false,
					""
			);

			Recommendation expected = NONE;

			// WHEN
			Recommendation actual = underTest.calculateForScratches(visibleSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {0.5, 1, 4, 4.5})
		void should_ReturnRESALE_When_ScratchesAreUnder5cm(double scratchValue) {
			// GIVEN
			VisibleSurfaceDamage visibleSurfaceDamage = new VisibleSurfaceDamage(
					true,
					scratchValue,
					false,
					0,
					false,
					"",
					false,
					""
			);

			Recommendation expected = RESALE;

			// WHEN
			Recommendation actual = underTest.calculateForScratches(visibleSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {5, 5.5, 10})
		void should_ReturnOUTLET_When_ScratchesAreEqualOrOver5cm(double scratchValue) {
			// GIVEN
			VisibleSurfaceDamage visibleSurfaceDamage = new VisibleSurfaceDamage(
					true,
					scratchValue,
					false,
					0,
					false,
					"",
					false,
					""
			);

			Recommendation expected = OUTLET;

			// WHEN
			Recommendation actual = underTest.calculateForScratches(visibleSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class testCalculateForDents {

		@Test
		void should_ReturnNONE_When_VisibleSurfacesHasDentsIsFalse() {
			// GIVEN
			VisibleSurfaceDamage visibleSurfaceDamage = new VisibleSurfaceDamage(
					false,
					0,
					false,
					0,
					false,
					"",
					false,
					""
			);

			Recommendation expected = NONE;

			// WHEN
			Recommendation actual = underTest.calculateForDents(visibleSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {0.5, 1, 4, 4.5})
		void should_ReturnRESALE_When_DentsAreUnder5cm(double dentDepth) {
			// GIVEN
			VisibleSurfaceDamage visibleSurfaceDamage = new VisibleSurfaceDamage(
					false,
					0,
					true,
					dentDepth,
					false,
					"",
					false,
					""
			);

			Recommendation expected = RESALE;

			// WHEN
			Recommendation actual = underTest.calculateForDents(visibleSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {5, 5.5, 10})
		void should_ReturnOUTLET_When_DentsAreEqualOrOver5cm(double dentDepth) {
			// GIVEN
			VisibleSurfaceDamage visibleSurfaceDamage = new VisibleSurfaceDamage(
					false,
					0,
					true,
					dentDepth,
					false,
					"",
					false,
					""
			);

			Recommendation expected = OUTLET;

			// WHEN
			Recommendation actual = underTest.calculateForDents(visibleSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class testCalculateForMinorDamage {

		@Test
		void should_ReturnRESALE_When_VisibleSurfacesHasMinorDamageIsTrue() {
			// GIVEN
			VisibleSurfaceDamage visibleSurfaceDamage = new VisibleSurfaceDamage(
					false,
					0,
					false,
					0,
					true,
					"I'm the tested property",
					false,
					""
			);

			Recommendation expected = RESALE;

			// WHEN
			Recommendation actual = underTest.calculateForMinorDamage(visibleSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		void should_ReturnNONE_When_VisibleSurfacesHasMinorDamageIsFalse() {
			// GIVEN
			VisibleSurfaceDamage visibleSurfaceDamage = new VisibleSurfaceDamage(
					false,
					0,
					false,
					0,
					false,
					"",
					false,
					""
			);

			Recommendation expected = NONE;

			// WHEN
			Recommendation actual = underTest.calculateForMinorDamage(visibleSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class testCalculateForMajorDamage {

		@Test
		void should_ReturnOUTLET_When_VisibleSurfacesHasMajorDamageIsTrue() {
			// GIVEN
			VisibleSurfaceDamage visibleSurfaceDamage = new VisibleSurfaceDamage(
					false,
					0,
					false,
					0,
					false,
					"",
					true,
					"I'm the tested property"
			);

			Recommendation expected = OUTLET;

			// WHEN
			Recommendation actual = underTest.calculateForMajorDamage(visibleSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		void should_ReturnNONE_When_VisibleSurfacesHasMajorDamageIsFalse() {
			// GIVEN
			VisibleSurfaceDamage visibleSurfaceDamage = new VisibleSurfaceDamage(
					false,
					0,
					false,
					0,
					false,
					"",
					false,
					""
			);

			Recommendation expected = NONE;

			// WHEN
			Recommendation actual = underTest.calculateForMajorDamage(visibleSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}
}