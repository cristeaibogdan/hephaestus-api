package org.personal.washingmachine.service.calculators;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.personal.washingmachine.dto.WashingMachineDetailDTO;
import org.personal.washingmachine.enums.Recommendation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.personal.washingmachine.enums.Recommendation.*;

class VisibleSurfacesRecommendationCalculatorTest {

	private final VisibleSurfacesRecommendationCalculator underTest = new VisibleSurfacesRecommendationCalculator();

	@Nested
	class testCalculate {

		@Test
		void should_ReturnNONE_When_ApplicableVisibleSurfacesDamageFalse() {
			// GIVEN
			WashingMachineDetailDTO dto = WashingMachineDetailDTO.builder()
					.applicableVisibleSurfacesDamage(false)
					.build();

			Recommendation expected = NONE;

			// WHEN
			Recommendation actual = underTest.calculate(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		void should_ReturnRESALE_When_MajorDamageTrue() {
			// GIVEN
			WashingMachineDetailDTO dto = WashingMachineDetailDTO.builder()
					.applicableVisibleSurfacesDamage(true)
					.visibleSurfacesHasMajorDamage(true)
					.build();

			Recommendation expected = OUTLET;

			// WHEN
			Recommendation actual = underTest.calculate(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class testCalculateForScratches {

		@Test
		void should_ReturnNONE_When_VisibleSurfacesHasScratchesIsFalse() {
			// GIVEN
			WashingMachineDetailDTO dto = WashingMachineDetailDTO.builder()
					.visibleSurfacesHasScratches(false)
					.build();

			Recommendation expected = NONE;

			// WHEN
			Recommendation actual = underTest.calculateForScratches(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {0.5, 1, 4, 4.5})
		void should_ReturnRESALE_When_ScratchesAreUnder5cm(double scratchValue) {
			// GIVEN
			WashingMachineDetailDTO dto = WashingMachineDetailDTO.builder()
					.applicableVisibleSurfacesDamage(true)
					.visibleSurfacesHasScratches(true)
					.visibleSurfacesScratchesLength(scratchValue)
					.build();

			Recommendation expected = RESALE;

			// WHEN
			Recommendation actual = underTest.calculateForScratches(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {5, 5.5, 10})
		void should_ReturnOUTLET_When_ScratchesAreEqualOrOver5cm(double scratchValue) {
			// GIVEN
			WashingMachineDetailDTO dto = WashingMachineDetailDTO.builder()
					.applicableVisibleSurfacesDamage(true)
					.visibleSurfacesHasScratches(true)
					.visibleSurfacesScratchesLength(scratchValue)
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
		void should_ReturnNONE_When_VisibleSurfacesHasDentsIsFalse() {
			// GIVEN
			WashingMachineDetailDTO dto = WashingMachineDetailDTO.builder()
					.visibleSurfacesHasDents(false)
					.build();

			Recommendation expected = NONE;

			// WHEN
			Recommendation actual = underTest.calculateForDents(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {0.5, 1, 4, 4.5})
		void should_ReturnRESALE_When_DentsAreUnder5cm(double dentDepth) {
			// GIVEN
			WashingMachineDetailDTO dto = WashingMachineDetailDTO.builder()
					.applicableVisibleSurfacesDamage(true)
					.visibleSurfacesHasDents(true)
					.visibleSurfacesDentsDepth(dentDepth)
					.build();

			Recommendation expected = RESALE;

			// WHEN
			Recommendation actual = underTest.calculateForDents(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(doubles = {5, 5.5, 10})
		void should_ReturnOUTLET_When_DentsAreEqualOrOver5cm(double dentDepth) {
			// GIVEN
			WashingMachineDetailDTO dto = WashingMachineDetailDTO.builder()
					.applicableVisibleSurfacesDamage(true)
					.visibleSurfacesHasDents(true)
					.visibleSurfacesDentsDepth(dentDepth)
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
		void should_ReturnRESALE_When_VisibleSurfacesHasMinorDamageIsTrue() {
			// GIVEN
			WashingMachineDetailDTO dto = WashingMachineDetailDTO.builder()
					.visibleSurfacesHasMinorDamage(true)
					.build();

			Recommendation expected = RESALE;

			// WHEN
			Recommendation actual = underTest.calculateForMinorDamage(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		void should_ReturnNONE_When_VisibleSurfacesHasMinorDamageIsFalse() {
			// GIVEN
			WashingMachineDetailDTO dto = WashingMachineDetailDTO.builder()
					.visibleSurfacesHasMinorDamage(false)
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
		void should_ReturnOUTLET_When_VisibleSurfacesHasMajorDamageIsTrue() {
			// GIVEN
			WashingMachineDetailDTO dto = WashingMachineDetailDTO.builder()
					.visibleSurfacesHasMajorDamage(true)
					.build();

			Recommendation expected = OUTLET;

			// WHEN
			Recommendation actual = underTest.calculateForMajorDamage(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		void should_ReturnNONE_When_VisibleSurfacesHasMajorDamageIsFalse() {
			// GIVEN
			WashingMachineDetailDTO dto = WashingMachineDetailDTO.builder()
					.visibleSurfacesHasMajorDamage(false)
					.build();

			Recommendation expected = NONE;

			// WHEN
			Recommendation actual = underTest.calculateForMajorDamage(dto);

			// THEN
			assertThat(actual).isEqualTo(expected);
		}
	}
}