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
		void should_ReturnNONE_When_ValuesAreDefault() {
			// GIVEN
			VisibleSurfaceDamage visibleSurfaceDamage = VisibleSurfaceDamage.builder()
					.visibleSurfacesScratchesLength(0)
					.visibleSurfacesHasDents(false)
					.visibleSurfacesDentsDepth(0)
					.visibleSurfacesHasMinorDamage(false)
					.visibleSurfacesMinorDamage("")
					.visibleSurfacesHasMajorDamage(false)
					.visibleSurfacesMajorDamage("")
					.build();

			Recommendation expected = NONE;

			// WHEN
			Recommendation actual = underTest.calculate(visibleSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		void should_ReturnRESALE_When_MajorDamageTrue() {
			// GIVEN
			VisibleSurfaceDamage visibleSurfaceDamage = VisibleSurfaceDamage.builder()
					.visibleSurfacesHasMajorDamage(true)
					.visibleSurfacesMajorDamage("I'm the tested property")
					.build();

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
		void should_ReturnNONE_When_VisibleSurfacesScratchesLengthIsZero() {
			// GIVEN
			VisibleSurfaceDamage visibleSurfaceDamage = VisibleSurfaceDamage.builder()
					.visibleSurfacesScratchesLength(0)
					.build();

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
			VisibleSurfaceDamage visibleSurfaceDamage = VisibleSurfaceDamage.builder()
					.visibleSurfacesScratchesLength(scratchValue)
					.build();

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
			VisibleSurfaceDamage visibleSurfaceDamage =VisibleSurfaceDamage.builder()
					.visibleSurfacesScratchesLength(scratchValue)
					.build();

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
			VisibleSurfaceDamage visibleSurfaceDamage = VisibleSurfaceDamage.builder()
					.visibleSurfacesHasDents(false)
					.build();

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
			VisibleSurfaceDamage visibleSurfaceDamage = VisibleSurfaceDamage.builder()
					.visibleSurfacesHasDents(true)
					.visibleSurfacesDentsDepth(dentDepth)
					.build();

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
			VisibleSurfaceDamage visibleSurfaceDamage = VisibleSurfaceDamage.builder()
					.visibleSurfacesHasDents(true)
					.visibleSurfacesDentsDepth(dentDepth)
					.build();

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
			VisibleSurfaceDamage visibleSurfaceDamage = VisibleSurfaceDamage.builder()
					.visibleSurfacesHasMinorDamage(true)
					.visibleSurfacesMinorDamage("I'm the tested property")
					.build();

			Recommendation expected = RESALE;

			// WHEN
			Recommendation actual = underTest.calculateForMinorDamage(visibleSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		void should_ReturnNONE_When_VisibleSurfacesHasMinorDamageIsFalse() {
			// GIVEN
			VisibleSurfaceDamage visibleSurfaceDamage = VisibleSurfaceDamage.builder()
					.visibleSurfacesHasMinorDamage(false)
					.build();

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
			VisibleSurfaceDamage visibleSurfaceDamage = VisibleSurfaceDamage.builder()
					.visibleSurfacesHasMajorDamage(true)
					.visibleSurfacesMajorDamage("I'm the tested property")
					.build();

			Recommendation expected = OUTLET;

			// WHEN
			Recommendation actual = underTest.calculateForMajorDamage(visibleSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		void should_ReturnNONE_When_VisibleSurfacesHasMajorDamageIsFalse() {
			// GIVEN
			VisibleSurfaceDamage visibleSurfaceDamage = VisibleSurfaceDamage.builder()
					.visibleSurfacesHasMajorDamage(false)
					.build();

			Recommendation expected = NONE;

			// WHEN
			Recommendation actual = underTest.calculateForMajorDamage(visibleSurfaceDamage);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}
}