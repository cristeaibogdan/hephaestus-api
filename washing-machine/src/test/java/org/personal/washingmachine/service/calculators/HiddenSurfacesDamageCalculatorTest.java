package org.personal.washingmachine.service.calculators;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.personal.washingmachine.entity.dtos.WashingMachineDetailsDTO;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HiddenSurfacesDamageCalculatorTest {
	private final HiddenSurfacesDamageCalculator underTest = new HiddenSurfacesDamageCalculator();

	@Nested
	class testCalculate {

		@Test
		void should_Return0_When_HiddenSurfacesDamageFalse() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.applicableHiddenSurfacesDamage(false)
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
		void should_Return0_When_HiddenSurfacesHasScratchesIsFalse() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.hiddenSurfacesHasScratches(false)
					.build();

			int expected = 0;

			// WHEN
			int actual = underTest.calculateScratchesDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5, 5, 6, 6.5})
		void should_Return2_When_ScratchesAreUnder7cm(double scratchLength) {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.applicableHiddenSurfacesDamage(true)
					.hiddenSurfacesHasScratches(true)
					.hiddenSurfacesScratchesLength(scratchLength)
					.build();

			int expected = 2;

			// WHEN
			int actual = underTest.calculateScratchesDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {7, 7.5, 8, 8.5, 9, 9.5, 10})
		void should_Return3_When_ScratchesAreEqualOrOver7cm(double scratchLength) {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.applicableHiddenSurfacesDamage(true)
					.hiddenSurfacesHasScratches(true)
					.hiddenSurfacesScratchesLength(scratchLength)
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
		void should_Return0_When_HiddenSurfacesHasDentsIsFalse() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.hiddenSurfacesHasScratches(false)
					.build();

			int expected = 0;

			// WHEN
			int actual = underTest.calculateDentsDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5, 5, 6, 6.5})
		void should_Return2_When_DentsAreUnder7cm(double dentValue) {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.applicableHiddenSurfacesDamage(true)
					.hiddenSurfacesHasDents(true)
					.hiddenSurfacesDentsDepth(dentValue)
					.build();

			int expected = 2;

			// WHEN
			int actual = underTest.calculateDentsDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {7, 7.5, 8, 8.5, 9, 9.5, 10})
		void should_Return3_When_DentsAreEqualOrOver7cm(double dentValue) {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.applicableHiddenSurfacesDamage(true)
					.hiddenSurfacesHasDents(true)
					.hiddenSurfacesDentsDepth(dentValue)
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
		void should_Return2_When_HiddenSurfacesHasSmallDamageIsTrue() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.hiddenSurfacesHasMinorDamage(true)
					.build();

			int expected = 2;

			// WHEN
			int actual = underTest.calculateSmallDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		void should_Return0_When_HiddenSurfacesHasSmallDamageIsFalse() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.hiddenSurfacesHasMinorDamage(false)
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
		void should_Return3_When_HiddenSurfacesHasBigDamageIsTrue() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.hiddenSurfacesHasMajorDamage(true)
					.build();

			int expected = 3;

			// WHEN
			int actual = underTest.calculateBigDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		void should_Return0_When_HiddenSurfacesHasBigDamageIsFalse() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.hiddenSurfacesHasMajorDamage(false)
					.build();

			int expected = 0;

			// WHEN
			int actual = underTest.calculateBigDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}
}