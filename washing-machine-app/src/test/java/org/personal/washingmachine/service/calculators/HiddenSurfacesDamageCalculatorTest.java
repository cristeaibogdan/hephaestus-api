package org.personal.washingmachine.service.calculators;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.personal.washingmachine.dto.WashingMachineDetailsDTO;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.personal.washingmachine.service.calculators.DamageLevel.*;

class HiddenSurfacesDamageCalculatorTest {
	private final HiddenSurfacesDamageCalculator underTest = new HiddenSurfacesDamageCalculator();

	@Nested
	class testCalculate {

		@Test
		void should_ReturnNONE_When_HiddenSurfacesDamageFalse() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.applicableHiddenSurfacesDamage(false)
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
		void should_ReturnNONE_When_HiddenSurfacesHasScratchesIsFalse() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.hiddenSurfacesHasScratches(false)
					.build();

			DamageLevel expected = NONE;

			// WHEN
			DamageLevel actual = underTest.calculateScratchesDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {0.5, 1, 6, 6.5})
		void should_ReturnRESALE_When_ScratchesAreUnder7cm(double scratchLength) {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.applicableHiddenSurfacesDamage(true)
					.hiddenSurfacesHasScratches(true)
					.hiddenSurfacesScratchesLength(scratchLength)
					.build();

			DamageLevel expected = RESALE;

			// WHEN
			DamageLevel actual = underTest.calculateScratchesDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {7, 7.5, 10})
		void should_ReturnOUTLET_When_ScratchesAreEqualOrOver7cm(double scratchLength) {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.applicableHiddenSurfacesDamage(true)
					.hiddenSurfacesHasScratches(true)
					.hiddenSurfacesScratchesLength(scratchLength)
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
		void should_ReturnNONE_When_HiddenSurfacesHasDentsIsFalse() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.hiddenSurfacesHasScratches(false)
					.build();

			DamageLevel expected = NONE;

			// WHEN
			DamageLevel actual = underTest.calculateDentsDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {0.5, 1, 6, 6.5})
		void should_ReturnRESALE_When_DentsAreUnder7cm(double dentValue) {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.applicableHiddenSurfacesDamage(true)
					.hiddenSurfacesHasDents(true)
					.hiddenSurfacesDentsDepth(dentValue)
					.build();

			DamageLevel expected = RESALE;

			// WHEN
			DamageLevel actual = underTest.calculateDentsDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {7, 7.5, 10})
		void should_ReturnOUTLET_When_DentsAreEqualOrOver7cm(double dentValue) {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.applicableHiddenSurfacesDamage(true)
					.hiddenSurfacesHasDents(true)
					.hiddenSurfacesDentsDepth(dentValue)
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
		void should_ReturnRESALE_When_HiddenSurfacesHasMinorDamageIsTrue() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.hiddenSurfacesHasMinorDamage(true)
					.build();

			DamageLevel expected = RESALE;

			// WHEN
			DamageLevel actual = underTest.calculateMinorDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		void should_ReturnNONE_When_HiddenSurfacesHasMinorDamageIsFalse() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.hiddenSurfacesHasMinorDamage(false)
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
		void should_ReturnOUTLET_When_HiddenSurfacesHasMajorDamageIsTrue() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.hiddenSurfacesHasMajorDamage(true)
					.build();

			DamageLevel expected = OUTLET;

			// WHEN
			DamageLevel actual = underTest.calculateMajorDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		void should_ReturnNONE_When_HiddenSurfacesHasMajorDamageIsFalse() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.hiddenSurfacesHasMajorDamage(false)
					.build();

			DamageLevel expected = NONE;

			// WHEN
			DamageLevel actual = underTest.calculateMajorDamageLevel(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}
}