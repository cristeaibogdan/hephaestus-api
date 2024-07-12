package org.personal.washingmachine.service.calculators;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.personal.washingmachine.entity.dtos.WashingMachineDetailsDTO;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class VisibleSurfacesDamageCalculatorTest {

	private final VisibleSurfacesDamageCalculator underTest = new VisibleSurfacesDamageCalculator();

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

	@Nested
	class testCalculateScratchesDamageLevel {

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

		@ParameterizedTest
		@ValueSource(doubles = {1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5})
		void should_Return2_When_DentsAreUnder5cm(double dentValue) {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.applicableVisibleSurfacesDamage(true)
					.visibleSurfacesHasDents(true)
					.visibleSurfacesDentsDepth(dentValue)
					.build();

			int expected = 2;

			// WHEN
			int actual = underTest.calculateDentsDamage(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {5, 5.5, 6, 6.5, 7, 7.5, 8, 8.5, 9, 9.5, 10})
		void should_Return3_When_DentsAreEqualOrOver5cm(double dentValue) {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.applicableVisibleSurfacesDamage(true)
					.visibleSurfacesHasDents(true)
					.visibleSurfacesDentsDepth(dentValue)
					.build();

			int expected = 3;

			// WHEN
			int actual = underTest.calculateDentsDamage(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}
}