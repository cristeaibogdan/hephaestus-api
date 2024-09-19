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
			HiddenSurfaceDamage hiddenSurfaceDamage = HiddenSurfaceDamage.builder()
					.hiddenSurfacesScratchesLength(0)
					.hiddenSurfacesHasDents(false)
					.hiddenSurfacesDentsDepth(0)
					.hiddenSurfacesHasMinorDamage(false)
					.hiddenSurfacesMinorDamage("")
					.hiddenSurfacesHasMajorDamage(false)
					.hiddenSurfacesMajorDamage("")
					.build();

			Recommendation expected = NONE;

			// WHEN
			Recommendation actual = underTest.calculate(hiddenSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		void should_ReturnRESALE_When_MinorDamageTrue() {
			// GIVEN
			HiddenSurfaceDamage hiddenSurfaceDamage = HiddenSurfaceDamage.builder()
					.hiddenSurfacesHasMinorDamage(true)
					.hiddenSurfacesMinorDamage("testing property")
					.build();

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
		void should_ReturnNONE_When_ScratchesLengthIsZero() {
			// GIVEN
			HiddenSurfaceDamage hiddenSurfaceDamage = HiddenSurfaceDamage.builder()
					.hiddenSurfacesScratchesLength(0)
					.build();

			Recommendation expected = NONE;

			// WHEN
			Recommendation actual = underTest.calculateForScratches(hiddenSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {0.5, 1, 6, 6.5})
		void should_ReturnRESALE_When_ScratchesLengthAreUnder7cm(double scratchLength) {
			// GIVEN
			HiddenSurfaceDamage hiddenSurfaceDamage = HiddenSurfaceDamage.builder()
					.hiddenSurfacesScratchesLength(scratchLength)
					.build();

			Recommendation expected = RESALE;

			// WHEN
			Recommendation actual = underTest.calculateForScratches(hiddenSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {7, 7.5, 10})
		void should_ReturnOUTLET_When_ScratchesLengthAreEqualOrOver7cm(double scratchLength) {
			// GIVEN
			HiddenSurfaceDamage hiddenSurfaceDamage = HiddenSurfaceDamage.builder()
					.hiddenSurfacesScratchesLength(scratchLength)
					.build();

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
			HiddenSurfaceDamage hiddenSurfaceDamage = HiddenSurfaceDamage.builder()
					.hiddenSurfacesHasDents(false)
					.build();

			Recommendation expected = NONE;

			// WHEN
			Recommendation actual = underTest.calculateForDents(hiddenSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {0.5, 1, 6, 6.5})
		void should_ReturnRESALE_When_DentsAreUnder7cm(double dentDepth) {
			// GIVEN
			HiddenSurfaceDamage hiddenSurfaceDamage = HiddenSurfaceDamage.builder()
					.hiddenSurfacesHasDents(true)
					.hiddenSurfacesDentsDepth(dentDepth)
					.build();

			Recommendation expected = RESALE;

			// WHEN
			Recommendation actual = underTest.calculateForDents(hiddenSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {7, 7.5, 10})
		void should_ReturnOUTLET_When_DentsAreEqualOrOver7cm(double dentDepth) {
			// GIVEN
			HiddenSurfaceDamage hiddenSurfaceDamage = HiddenSurfaceDamage.builder()
					.hiddenSurfacesHasDents(true)
					.hiddenSurfacesDentsDepth(dentDepth)
					.build();

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
			HiddenSurfaceDamage hiddenSurfaceDamage = HiddenSurfaceDamage.builder()
					.hiddenSurfacesHasMinorDamage(true)
					.hiddenSurfacesMinorDamage("I'm the tested property")
					.build();

			Recommendation expected = RESALE;

			// WHEN
			Recommendation actual = underTest.calculateForMinorDamage(hiddenSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		void should_ReturnNONE_When_HiddenSurfacesHasMinorDamageIsFalse() {
			// GIVEN
			HiddenSurfaceDamage hiddenSurfaceDamage = HiddenSurfaceDamage.builder()
					.hiddenSurfacesHasMinorDamage(false)
					.build();

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
			HiddenSurfaceDamage hiddenSurfaceDamage = HiddenSurfaceDamage.builder()
					.hiddenSurfacesHasMajorDamage(true)
					.hiddenSurfacesMajorDamage("I'm the tested property")
					.build();

			Recommendation expected = OUTLET;

			// WHEN
			Recommendation actual = underTest.calculateForMajorDamage(hiddenSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		void should_ReturnNONE_When_HiddenSurfacesHasMajorDamageIsFalse() {
			// GIVEN
			HiddenSurfaceDamage hiddenSurfaceDamage = HiddenSurfaceDamage.builder()
					.hiddenSurfacesHasMajorDamage(false)
					.build();

			Recommendation expected = NONE;

			// WHEN
			Recommendation actual = underTest.calculateForMajorDamage(hiddenSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}
}