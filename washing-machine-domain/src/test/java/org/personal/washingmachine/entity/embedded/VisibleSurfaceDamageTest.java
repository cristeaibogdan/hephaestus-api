package org.personal.washingmachine.entity.embedded;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.personal.washingmachine.enums.Recommendation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.personal.washingmachine.enums.Recommendation.*;

class VisibleSurfaceDamageTest {

	@Nested
	class testCalculate {

		@Test
		void should_ReturnNONE_When_ValuesAreDefault() {
			// GIVEN
			VisibleSurfaceDamage underTest = VisibleSurfaceDamage.builder()
					.visibleSurfacesScratchesLength(0)
					.visibleSurfacesDentsDepth(0)
					.visibleSurfacesMinorDamage("")
					.visibleSurfacesMajorDamage("")
					.build();

			// WHEN
			Recommendation actual = underTest.calculate();

			// THEN
			assertThat(actual).isEqualTo(NONE);
		}

		@Test
		void should_ReturnRESALE_When_MajorDamageIsSpecified() {
			// GIVEN
			VisibleSurfaceDamage underTest = VisibleSurfaceDamage.builder()
					.visibleSurfacesMajorDamage("I'm the tested property")
					.build();

			// WHEN
			Recommendation actual = underTest.calculate();

			// THEN
			assertThat(actual).isEqualTo(OUTLET);
		}
	}

	@Nested
	class testCalculateForScratches {

		@Test
		void should_ReturnNONE_When_VisibleSurfacesScratchesLengthIsZero() {
			// GIVEN
			VisibleSurfaceDamage underTest = VisibleSurfaceDamage.builder()
					.visibleSurfacesScratchesLength(0)
					.build();

			// WHEN
			Recommendation actual = underTest.calculateForScratches();

			// THEN
			assertThat(actual).isEqualTo(NONE);
		}

		@ParameterizedTest
		@ValueSource(doubles = {0.5, 1, 4, 4.5})
		void should_ReturnRESALE_When_ScratchesAreUnder5cm(double scratchValue) {
			// GIVEN
			VisibleSurfaceDamage underTest = VisibleSurfaceDamage.builder()
					.visibleSurfacesScratchesLength(scratchValue)
					.build();

			// WHEN
			Recommendation actual = underTest.calculateForScratches();

			// THEN
			assertThat(actual).isEqualTo(RESALE);
		}

		@ParameterizedTest
		@ValueSource(doubles = {5, 5.5, 10})
		void should_ReturnOUTLET_When_ScratchesAreEqualOrOver5cm(double scratchValue) {
			// GIVEN
			VisibleSurfaceDamage underTest = VisibleSurfaceDamage.builder()
					.visibleSurfacesScratchesLength(scratchValue)
					.build();

			// WHEN
			Recommendation actual = underTest.calculateForScratches();

			// THEN
			assertThat(actual).isEqualTo(OUTLET);
		}
	}

	@Nested
	class testCalculateForDents {

		@Test
		void should_ReturnNONE_When_VisibleSurfacesDentsIsZero() {
			// GIVEN
			VisibleSurfaceDamage underTest = VisibleSurfaceDamage.builder()
					.visibleSurfacesDentsDepth(0)
					.build();

			// WHEN
			Recommendation actual = underTest.calculateForDents();

			// THEN
			assertThat(actual).isEqualTo(NONE);
		}

		@ParameterizedTest
		@ValueSource(doubles = {0.5, 1, 4, 4.5})
		void should_ReturnRESALE_When_DentsAreUnder5cm(double dentDepth) {
			// GIVEN
			VisibleSurfaceDamage underTest = VisibleSurfaceDamage.builder()
					.visibleSurfacesDentsDepth(dentDepth)
					.build();

			// WHEN
			Recommendation actual = underTest.calculateForDents();

			// THEN
			assertThat(actual).isEqualTo(RESALE);
		}

		@ParameterizedTest
		@ValueSource(doubles = {5, 5.5, 10})
		void should_ReturnOUTLET_When_DentsAreEqualOrOver5cm(double dentDepth) {
			// GIVEN
			VisibleSurfaceDamage underTest = VisibleSurfaceDamage.builder()
					.visibleSurfacesDentsDepth(dentDepth)
					.build();

			// WHEN
			Recommendation actual = underTest.calculateForDents();

			// THEN
			assertThat(actual).isEqualTo(OUTLET);
		}
	}

	@Nested
	class testCalculateForMinorDamage {

		@Test
		void should_ReturnRESALE_When_VisibleSurfacesHasMinorDamageSpecified() {
			// GIVEN
			VisibleSurfaceDamage underTest = VisibleSurfaceDamage.builder()
					.visibleSurfacesMinorDamage("I'm the tested property")
					.build();

			// WHEN
			Recommendation actual = underTest.calculateForMinorDamage();

			// THEN
			assertThat(actual).isEqualTo(RESALE);
		}

		@Test
		void should_ReturnNONE_When_VisibleSurfacesHasMinorDamageUnspecified() {
			// GIVEN
			VisibleSurfaceDamage underTest = VisibleSurfaceDamage.builder()
					.visibleSurfacesMinorDamage("")
					.build();

			// WHEN
			Recommendation actual = underTest.calculateForMinorDamage();

			// THEN
			assertThat(actual).isEqualTo(NONE);
		}
	}

	@Nested
	class testCalculateForMajorDamage {

		@Test
		void should_ReturnOUTLET_When_VisibleSurfacesHasMajorDamageIsSpecified() {
			// GIVEN
			VisibleSurfaceDamage underTest = VisibleSurfaceDamage.builder()
					.visibleSurfacesMajorDamage("I'm the tested property")
					.build();

			// WHEN
			Recommendation actual = underTest.calculateForMajorDamage();

			// THEN
			assertThat(actual).isEqualTo(OUTLET);
		}

		@Test
		void should_ReturnNONE_When_VisibleSurfacesMajorDamageIsUnspecified() {
			// GIVEN
			VisibleSurfaceDamage underTest = VisibleSurfaceDamage.builder()
					.visibleSurfacesMajorDamage("")
					.build();

			// WHEN
			Recommendation actual = underTest.calculateForMajorDamage();

			// THEN
			assertThat(actual).isEqualTo(NONE);
		}
	}
}