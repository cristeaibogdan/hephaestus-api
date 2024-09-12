package org.personal.washingmachine.service.calculators;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.personal.washingmachine.entity.embedded.HiddenSurfaceDamage;
import org.personal.washingmachine.enums.Recommendation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.personal.washingmachine.enums.Recommendation.*;

class HiddenSurfacesRecommendationCalculatorTest {
	private final HiddenSurfacesRecommendationCalculator underTest = new HiddenSurfacesRecommendationCalculator();

	@Nested
	class testCalculate {

		@Test
		void should_ReturnNONE_When_AllValuesAreFalse() {
			// GIVEN
			HiddenSurfaceDamage hiddenSurfaceDamage = new HiddenSurfaceDamage(
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
			Recommendation actual = underTest.calculate(hiddenSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		void should_ReturnRESALE_When_MinorDamageTrue() {
			// GIVEN
			HiddenSurfaceDamage hiddenSurfaceDamage = new HiddenSurfaceDamage(
					false,
					0,
					false,
					0,
					true,
					"testing property",
					false,
					""
			);

			Recommendation expected = RESALE;

			// WHEN
			Recommendation actual = underTest.calculate(hiddenSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class testCalculateForScratches {

		@Test
		void should_ReturnNONE_When_HiddenSurfacesHasScratchesIsFalse() {
			// GIVEN
			HiddenSurfaceDamage hiddenSurfaceDamage = new HiddenSurfaceDamage(
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
			Recommendation actual = underTest.calculateForScratches(hiddenSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {0.5, 1, 6, 6.5})
		void should_ReturnRESALE_When_ScratchesAreUnder7cm(double scratchLength) {
			// GIVEN
			HiddenSurfaceDamage hiddenSurfaceDamage = new HiddenSurfaceDamage(
					true,
					scratchLength,
					false,
					0,
					false,
					"",
					false,
					""
			);

			Recommendation expected = RESALE;

			// WHEN
			Recommendation actual = underTest.calculateForScratches(hiddenSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {7, 7.5, 10})
		void should_ReturnOUTLET_When_ScratchesAreEqualOrOver7cm(double scratchLength) {
			// GIVEN
			HiddenSurfaceDamage hiddenSurfaceDamage = new HiddenSurfaceDamage(
					true,
					scratchLength,
					false,
					0,
					false,
					"",
					false,
					""
			);

			Recommendation expected = OUTLET;

			// WHEN
			Recommendation actual = underTest.calculateForScratches(hiddenSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class testCalculateForDents {

		@Test
		void should_ReturnNONE_When_HiddenSurfacesHasDentsIsFalse() {
			// GIVEN
			HiddenSurfaceDamage hiddenSurfaceDamage = new HiddenSurfaceDamage(
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
			Recommendation actual = underTest.calculateForDents(hiddenSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {0.5, 1, 6, 6.5})
		void should_ReturnRESALE_When_DentsAreUnder7cm(double dentValue) {
			// GIVEN
			HiddenSurfaceDamage hiddenSurfaceDamage = new HiddenSurfaceDamage(
					false,
					0,
					true,
					dentValue,
					false,
					"",
					false,
					""
			);

			Recommendation expected = RESALE;

			// WHEN
			Recommendation actual = underTest.calculateForDents(hiddenSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {7, 7.5, 10})
		void should_ReturnOUTLET_When_DentsAreEqualOrOver7cm(double dentValue) {
			// GIVEN
			HiddenSurfaceDamage hiddenSurfaceDamage = new HiddenSurfaceDamage(
					false,
					0,
					true,
					dentValue,
					false,
					"",
					false,
					""
			);

			Recommendation expected = OUTLET;

			// WHEN
			Recommendation actual = underTest.calculateForDents(hiddenSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class testCalculateForMinorDamage {

		@Test
		void should_ReturnRESALE_When_HiddenSurfacesHasMinorDamageIsTrue() {
			// GIVEN
			HiddenSurfaceDamage hiddenSurfaceDamage = new HiddenSurfaceDamage(
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
			Recommendation actual = underTest.calculateForMinorDamage(hiddenSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		void should_ReturnNONE_When_HiddenSurfacesHasMinorDamageIsFalse() {
			// GIVEN
			HiddenSurfaceDamage hiddenSurfaceDamage = new HiddenSurfaceDamage(
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
			Recommendation actual = underTest.calculateForMinorDamage(hiddenSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class testCalculateForMajorDamage {

		@Test
		void should_ReturnOUTLET_When_HiddenSurfacesHasMajorDamageIsTrue() {
			// GIVEN
			HiddenSurfaceDamage hiddenSurfaceDamage = new HiddenSurfaceDamage(
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
			Recommendation actual = underTest.calculateForMajorDamage(hiddenSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		void should_ReturnNONE_When_HiddenSurfacesHasMajorDamageIsFalse() {
			// GIVEN
			HiddenSurfaceDamage hiddenSurfaceDamage = new HiddenSurfaceDamage(
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
			Recommendation actual = underTest.calculateForMajorDamage(hiddenSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}
}