package org.personal.washingmachine.entity.embedded;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.personal.washingmachine.enums.Recommendation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.personal.washingmachine.enums.Recommendation.*;

class HiddenSurfaceDamageTest {

	@Nested
	class testCalculate {

		@Test
		void should_ReturnNONE_When_ValuesAreDefault() {
			// GIVEN
			HiddenSurfaceDamage underTest = new HiddenSurfaceDamage()
					.withHiddenSurfacesScratchesLength(0)
					.withHiddenSurfacesDentsDepth(0)
					.withHiddenSurfacesMinorDamage("")
					.withHiddenSurfacesMajorDamage("");

			// WHEN
			Recommendation actual = underTest.calculate();

			// THEN
			assertThat(actual).isEqualTo(NONE);
		}

		@Test
		void should_ReturnRESALE_When_MinorDamageIsSpecified() {
			// GIVEN
			HiddenSurfaceDamage underTest = new HiddenSurfaceDamage()
					.withHiddenSurfacesMinorDamage("testing property");

			// WHEN
			Recommendation actual = underTest.calculate();

			// THEN
			assertThat(actual).isEqualTo(RESALE);
		}
	}

	@Nested
	class testCalculateForScratches {

		@Test
		void should_ReturnNONE_When_ScratchesLengthIsZero() {
			// GIVEN
			HiddenSurfaceDamage underTest = new HiddenSurfaceDamage()
					.withHiddenSurfacesScratchesLength(0);

			// WHEN
			Recommendation actual = underTest.calculateForScratches();

			// THEN
			assertThat(actual).isEqualTo(NONE);
		}

		@ParameterizedTest
		@ValueSource(doubles = {0.5, 1, 6, 6.5})
		void should_ReturnRESALE_When_ScratchesLengthAreUnder7cm(double scratchLength) {
			// GIVEN
			HiddenSurfaceDamage underTest = new HiddenSurfaceDamage()
					.withHiddenSurfacesScratchesLength(scratchLength);

			// WHEN
			Recommendation actual = underTest.calculateForScratches();

			// THEN
			assertThat(actual).isEqualTo(RESALE);
		}

		@ParameterizedTest
		@ValueSource(doubles = {7, 7.5, 10})
		void should_ReturnOUTLET_When_ScratchesLengthAreEqualOrOver7cm(double scratchLength) {
			// GIVEN
			HiddenSurfaceDamage underTest = new HiddenSurfaceDamage()
					.withHiddenSurfacesScratchesLength(scratchLength);

			// WHEN
			Recommendation actual = underTest.calculateForScratches();

			// THEN
			assertThat(actual).isEqualTo(OUTLET);
		}
	}

	@Nested
	class testCalculateForDents {

		@Test
		void should_ReturnNONE_When_HiddenSurfacesDentsIsZero() {
			// GIVEN
			HiddenSurfaceDamage underTest = new HiddenSurfaceDamage()
					.withHiddenSurfacesDentsDepth(0);

			// WHEN
			Recommendation actual = underTest.calculateForDents();

			// THEN
			assertThat(actual).isEqualTo(NONE);
		}

		@ParameterizedTest
		@ValueSource(doubles = {0.5, 1, 6, 6.5})
		void should_ReturnRESALE_When_DentsAreUnder7cm(double dentDepth) {
			// GIVEN
			HiddenSurfaceDamage underTest = new HiddenSurfaceDamage()
					.withHiddenSurfacesDentsDepth(dentDepth);

			// WHEN
			Recommendation actual = underTest.calculateForDents();

			// THEN
			assertThat(actual).isEqualTo(RESALE);
		}

		@ParameterizedTest
		@ValueSource(doubles = {7, 7.5, 10})
		void should_ReturnOUTLET_When_DentsAreEqualOrOver7cm(double dentDepth) {
			// GIVEN
			HiddenSurfaceDamage underTest = new HiddenSurfaceDamage()
					.withHiddenSurfacesDentsDepth(dentDepth);

			// WHEN
			Recommendation actual = underTest.calculateForDents();

			// THEN
			assertThat(actual).isEqualTo(OUTLET);
		}
	}

	@Nested
	class testCalculateForMinorDamage {

		@Test
		void should_ReturnRESALE_When_HiddenSurfacesHasMinorDamageSpecified() {
			// GIVEN
			HiddenSurfaceDamage underTest = new HiddenSurfaceDamage()
					.withHiddenSurfacesMinorDamage("I'm the tested property");

			// WHEN
			Recommendation actual = underTest.calculateForMinorDamage();

			// THEN
			assertThat(actual).isEqualTo(RESALE);
		}

		@Test
		void should_ReturnNONE_When_HiddenSurfacesHasMinorDamageUnspecified() {
			// GIVEN
			HiddenSurfaceDamage underTest = new HiddenSurfaceDamage()
					.withHiddenSurfacesMinorDamage("");

			// WHEN
			Recommendation actual = underTest.calculateForMinorDamage();

			// THEN
			assertThat(actual).isEqualTo(NONE);
		}
	}

	@Nested
	class testCalculateForMajorDamage {

		@Test
		void should_ReturnOUTLET_When_HiddenSurfacesHasMajorDamageSpecified() {
			// GIVEN
			HiddenSurfaceDamage underTest = new HiddenSurfaceDamage()
					.withHiddenSurfacesMajorDamage("I'm the tested property");

			// WHEN
			Recommendation actual = underTest.calculateForMajorDamage();

			// THEN
			assertThat(actual).isEqualTo(OUTLET);
		}

		@Test
		void should_ReturnNONE_When_HiddenSurfacesHasMajorDamageUnspecified() {
			// GIVEN
			HiddenSurfaceDamage underTest = new HiddenSurfaceDamage()
					.withHiddenSurfacesMajorDamage("");

			// WHEN
			Recommendation actual = underTest.calculateForMajorDamage();

			// THEN
			assertThat(actual).isEqualTo(NONE);
		}
	}
}