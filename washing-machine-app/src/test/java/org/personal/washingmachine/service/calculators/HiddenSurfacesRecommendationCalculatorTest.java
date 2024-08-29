package org.personal.washingmachine.service.calculators;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.personal.washingmachine.dto.WashingMachineDetailsDTO;
import org.personal.washingmachine.enums.Recommendation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.personal.washingmachine.enums.Recommendation.*;

class HiddenSurfacesRecommendationCalculatorTest {
	private final HiddenSurfacesRecommendationCalculator underTest = new HiddenSurfacesRecommendationCalculator();

	@Nested
	class testCalculate {

		@Test
		void should_ReturnNONE_When_HiddenSurfacesDamageFalse() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.applicableHiddenSurfacesDamage(false)
					.build();

			Recommendation expected = NONE;

			// WHEN
			Recommendation actual = underTest.calculate(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class testCalculateForScratches {

		@Test
		void should_ReturnNONE_When_HiddenSurfacesHasScratchesIsFalse() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.hiddenSurfacesHasScratches(false)
					.build();

			Recommendation expected = NONE;

			// WHEN
			Recommendation actual = underTest.calculateForScratches(dto);

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

			Recommendation expected = RESALE;

			// WHEN
			Recommendation actual = underTest.calculateForScratches(dto);

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

			Recommendation expected = OUTLET;

			// WHEN
			Recommendation actual = underTest.calculateForScratches(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class testCalculateForDents {

		@Test
		void should_ReturnNONE_When_HiddenSurfacesHasDentsIsFalse() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.hiddenSurfacesHasScratches(false)
					.build();

			Recommendation expected = NONE;

			// WHEN
			Recommendation actual = underTest.calculateForDents(dto);

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

			Recommendation expected = RESALE;

			// WHEN
			Recommendation actual = underTest.calculateForDents(dto);

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

			Recommendation expected = OUTLET;

			// WHEN
			Recommendation actual = underTest.calculateForDents(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class testCalculateForMinorDamage {

		@Test
		void should_ReturnRESALE_When_HiddenSurfacesHasMinorDamageIsTrue() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.hiddenSurfacesHasMinorDamage(true)
					.build();

			Recommendation expected = RESALE;

			// WHEN
			Recommendation actual = underTest.calculateForMinorDamage(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		void should_ReturnNONE_When_HiddenSurfacesHasMinorDamageIsFalse() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.hiddenSurfacesHasMinorDamage(false)
					.build();

			Recommendation expected = NONE;

			// WHEN
			Recommendation actual = underTest.calculateForMinorDamage(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class testCalculateForMajorDamage {

		@Test
		void should_ReturnOUTLET_When_HiddenSurfacesHasMajorDamageIsTrue() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.hiddenSurfacesHasMajorDamage(true)
					.build();

			Recommendation expected = OUTLET;

			// WHEN
			Recommendation actual = underTest.calculateForMajorDamage(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		void should_ReturnNONE_When_HiddenSurfacesHasMajorDamageIsFalse() {
			// GIVEN
			WashingMachineDetailsDTO dto = WashingMachineDetailsDTO.builder()
					.hiddenSurfacesHasMajorDamage(false)
					.build();

			Recommendation expected = NONE;

			// WHEN
			Recommendation actual = underTest.calculateForMajorDamage(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}
}