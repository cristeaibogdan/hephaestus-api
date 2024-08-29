package org.personal.washingmachine.service.calculators;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.personal.washingmachine.dto.WashingMachineDetailsDTO;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.personal.washingmachine.service.calculators.DamageLevel.*;

class VisibleSurfacesDamageCalculatorTest {

	private final VisibleSurfacesDamageCalculator underTest = new VisibleSurfacesDamageCalculator();

	@Nested
	class testCalculate {

		@Test
		void should_ReturnNONE_When_ApplicableVisibleSurfacesDamageFalse() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.applicableVisibleSurfacesDamage(false)
					.build();

			DamageLevel expected = NONE;

			// WHEN
			DamageLevel actual = underTest.calculate(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class testCalculateScratchesDamageLevel {

		@Test
		void should_ReturnNONE_When_VisibleSurfacesHasScratchesIsFalse() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.visibleSurfacesHasScratches(false)
					.build();

			DamageLevel expected = NONE;

			// WHEN
			DamageLevel actual = underTest.calculateScratchesDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {0.5, 1, 4, 4.5})
		void should_ReturnRESALE_When_ScratchesAreUnder5cm(double scratchValue) {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.applicableVisibleSurfacesDamage(true)
					.visibleSurfacesHasScratches(true)
					.visibleSurfacesScratchesLength(scratchValue)
					.build();

			DamageLevel expected = RESALE;

			// WHEN
			DamageLevel actual = underTest.calculateScratchesDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {5, 5.5, 10})
		void should_ReturnOUTLET_When_ScratchesAreEqualOrOver5cm(double scratchValue) {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.applicableVisibleSurfacesDamage(true)
					.visibleSurfacesHasScratches(true)
					.visibleSurfacesScratchesLength(scratchValue)
					.build();

			DamageLevel expected = OUTLET;

			// WHEN
			DamageLevel actual = underTest.calculateScratchesDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class testCalculateDentsDamageLevel {

		@Test
		void should_ReturnNONE_When_VisibleSurfacesHasDentsIsFalse() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.visibleSurfacesHasDents(false)
					.build();

			DamageLevel expected = NONE;

			// WHEN
			DamageLevel actual = underTest.calculateDentsDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {0.5, 1, 4, 4.5})
		void should_ReturnRESALE_When_DentsAreUnder5cm(double dentDepth) {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.applicableVisibleSurfacesDamage(true)
					.visibleSurfacesHasDents(true)
					.visibleSurfacesDentsDepth(dentDepth)
					.build();

			DamageLevel expected = RESALE;

			// WHEN
			DamageLevel actual = underTest.calculateDentsDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {5, 5.5, 10})
		void should_ReturnOUTLET_When_DentsAreEqualOrOver5cm(double dentDepth) {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.applicableVisibleSurfacesDamage(true)
					.visibleSurfacesHasDents(true)
					.visibleSurfacesDentsDepth(dentDepth)
					.build();

			DamageLevel expected = OUTLET;

			// WHEN
			DamageLevel actual = underTest.calculateDentsDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class testCalculateSmallDamageLevel {

		@Test
		void should_ReturnRESALE_When_VisibleSurfacesHasMinorDamageIsTrue() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.visibleSurfacesHasMinorDamage(true)
					.build();

			DamageLevel expected = RESALE;

			// WHEN
			DamageLevel actual = underTest.calculateMinorDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		void should_ReturnNONE_When_VisibleSurfacesHasMinorDamageIsFalse() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.visibleSurfacesHasMinorDamage(false)
					.build();

			DamageLevel expected = NONE;

			// WHEN
			DamageLevel actual = underTest.calculateMinorDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class testCalculateBigDamageLevel {

		@Test
		void should_ReturnOUTLET_When_VisibleSurfacesHasMajorDamageIsTrue() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.visibleSurfacesHasMajorDamage(true)
					.build();

			DamageLevel expected = OUTLET;

			// WHEN
			DamageLevel actual = underTest.calculateMajorDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		void should_ReturnNONE_When_VisibleSurfacesHasMajorDamageIsFalse() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.visibleSurfacesHasMajorDamage(false)
					.build();

			DamageLevel expected = NONE;

			// WHEN
			DamageLevel actual = underTest.calculateMajorDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}
}