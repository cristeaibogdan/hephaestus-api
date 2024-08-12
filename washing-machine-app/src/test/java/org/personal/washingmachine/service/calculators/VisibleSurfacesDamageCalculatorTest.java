package org.personal.washingmachine.service.calculators;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.personal.washingmachine.dto.WashingMachineDetailsDTO;
import org.personal.washingmachine.facade.calculators.VisibleSurfacesDamageCalculator;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class VisibleSurfacesDamageCalculatorTest {

	private final VisibleSurfacesDamageCalculator underTest = new VisibleSurfacesDamageCalculator();

	@Nested
	class testCalculate {

		@Test
		void should_Return0_When_ApplicableVisibleSurfacesDamageFalse() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.applicableVisibleSurfacesDamage(false)
					.build();

			int expected = 0;

			// WHEN
			int actual = underTest.calculate(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class testCalculateScratchesDamageLevel {

		@Test
		void should_Return0_When_VisibleSurfacesHasScratchesIsFalse() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.visibleSurfacesHasScratches(false)
					.build();

			int expected = 0;

			// WHEN
			int actual = underTest.calculateScratchesDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5})
		void should_Return2_When_ScratchesAreUnder5cm(double scratchValue) {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.applicableVisibleSurfacesDamage(true)
					.visibleSurfacesHasScratches(true)
					.visibleSurfacesScratchesLength(scratchValue)
					.build();

			int expected = 2;

			// WHEN
			int actual = underTest.calculateScratchesDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {5, 5.5, 6, 6.5, 7, 7.5, 8, 8.5, 9, 9.5, 10})
		void should_Return3_When_ScratchesAreEqualOrOver5cm(double scratchValue) {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.applicableVisibleSurfacesDamage(true)
					.visibleSurfacesHasScratches(true)
					.visibleSurfacesScratchesLength(scratchValue)
					.build();

			int expected = 3;

			// WHEN
			int actual = underTest.calculateScratchesDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class testCalculateDentsDamageLevel {

		@Test
		void should_Return0_When_VisibleSurfacesHasDentsIsFalse() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.visibleSurfacesHasDents(false)
					.build();

			int expected = 0;

			// WHEN
			int actual = underTest.calculateDentsDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5})
		void should_Return2_When_DentsAreUnder5cm(double dentDepth) {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.applicableVisibleSurfacesDamage(true)
					.visibleSurfacesHasDents(true)
					.visibleSurfacesDentsDepth(dentDepth)
					.build();

			int expected = 2;

			// WHEN
			int actual = underTest.calculateDentsDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {5, 5.5, 6, 6.5, 7, 7.5, 8, 8.5, 9, 9.5, 10})
		void should_Return3_When_DentsAreEqualOrOver5cm(double dentDepth) {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.applicableVisibleSurfacesDamage(true)
					.visibleSurfacesHasDents(true)
					.visibleSurfacesDentsDepth(dentDepth)
					.build();

			int expected = 3;

			// WHEN
			int actual = underTest.calculateDentsDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class testCalculateSmallDamageLevel {

		@Test
		void should_Return2_When_VisibleSurfacesHasSmallDamageIsTrue() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.visibleSurfacesHasMinorDamage(true)
					.build();

			int expected = 2;

			// WHEN
			int actual = underTest.calculateSmallDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		void should_Return0_When_VisibleSurfacesHasSmallDamageIsFalse() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.visibleSurfacesHasMinorDamage(false)
					.build();

			int expected = 0;

			// WHEN
			int actual = underTest.calculateSmallDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class testCalculateBigDamageLevel {

		@Test
		void should_Return3_When_VisibleSurfacesHasBigDamageIsTrue() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.visibleSurfacesHasMajorDamage(true)
					.build();

			int expected = 3;

			// WHEN
			int actual = underTest.calculateBigDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		void should_Return0_When_VisibleSurfacesHasBigDamageIsFalse() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.visibleSurfacesHasMajorDamage(false)
					.build();

			int expected = 0;

			// WHEN
			int actual = underTest.calculateBigDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}
}