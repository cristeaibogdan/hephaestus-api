package org.personal.washingmachine.service.utils;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.personal.shared.exception.CustomException;
import org.personal.washingmachine.entity.dtos.WashingMachineDetailsDTO;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


class DamageCalculatorTest {

    @Nested
    class testCalculateDamageLevelForPackage {

        @Test
        void should_Return0_When_ApplicablePackageDamageFalse() {
            // GIVEN
            WashingMachineDetailsDTO washingMachineDetailsDTO = WashingMachineDetailsDTO.builder()
                    .applicablePackageDamage(false)
                    .build();

            int expected = 0;

            // WHEN
            int actual = DamageCalculator.calculateDamageLevelForPackage(washingMachineDetailsDTO);

            // THEN
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource({
                "true, true",
                "true, false",
                "false, true",
                "false, false",
        })
        void should_Return1_When_PackageMaterialAvailableTrue(boolean isDamaged, boolean isDirty) {
            // GIVEN
            WashingMachineDetailsDTO washingMachineDetailsDTO = WashingMachineDetailsDTO.builder()
                    .applicablePackageDamage(true)
                    .packageDamaged(isDamaged)
                    .packageDirty(isDirty)
                    .packageMaterialAvailable(true)
                    .build();

            int expected = 1;

            // WHEN
            int actual = DamageCalculator.calculateDamageLevelForPackage(washingMachineDetailsDTO);

            // THEN
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource({
                "true, true",
                "true, false",
                "false, true",
                "false, false",
        })
        void should_Return2_When_PackageMaterialAvailableFalse(boolean isDamaged, boolean isDirty) {
            // GIVEN
            WashingMachineDetailsDTO washingMachineDetailsDTO = WashingMachineDetailsDTO.builder()
                    .applicablePackageDamage(true)
                    .packageDamaged(isDamaged)
                    .packageDirty(isDirty)
                    .packageMaterialAvailable(false)
                    .build();

            int expected = 2;

            // WHEN
            int actual = DamageCalculator.calculateDamageLevelForPackage(washingMachineDetailsDTO);

            // THEN
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class testCalculateDamageLevelForVisibleSurfaces {

        @Test
        void should_Return0_When_ApplicableVisibleSurfacesDamageFalse() {
            // GIVEN
            WashingMachineDetailsDTO washingMachineDetailsDTO = WashingMachineDetailsDTO.builder()
                    .applicableVisibleSurfacesDamage(false)
                    .build();

            int expected = 0;

            // WHEN
            int actual = DamageCalculator.calculateDamageLevelForVisibleSurfaces(washingMachineDetailsDTO);

            // THEN
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @ValueSource(doubles = {1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5})
        void should_Return2_When_VisibleSurfaceScratchesAreUnder5cm(double scratchValue) {
            // GIVEN
            WashingMachineDetailsDTO washingMachineDetailsDTO = WashingMachineDetailsDTO.builder()
                    .applicableVisibleSurfacesDamage(true)
                    .visibleSurfacesHasScratches(true)
                    .visibleSurfacesScratchesLength(scratchValue)
                    .build();

            int expected = 2;

            // WHEN
            int actual = DamageCalculator.calculateDamageLevelForVisibleSurfaces(washingMachineDetailsDTO);

            // THEN
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @ValueSource(doubles = {5, 5.5, 6, 6.5, 7, 7.5, 8, 8.5, 9, 9.5, 10})
        void should_Return3_When_VisibleSurfaceScratchesAreEqualOrOver5cm(double scratchValue) {
            // GIVEN
            WashingMachineDetailsDTO washingMachineDetailsDTO = WashingMachineDetailsDTO.builder()
                    .applicableVisibleSurfacesDamage(true)
                    .visibleSurfacesHasScratches(true)
                    .visibleSurfacesScratchesLength(scratchValue)
                    .build();

            int expected = 3;

            // WHEN
            int actual = DamageCalculator.calculateDamageLevelForVisibleSurfaces(washingMachineDetailsDTO);

            // THEN
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @ValueSource(doubles = {1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5})
        void should_Return2_When_VisibleSurfaceDentsAreUnder5cm(double dentValue) {
            // GIVEN
            WashingMachineDetailsDTO washingMachineDetailsDTO = WashingMachineDetailsDTO.builder()
                    .applicableVisibleSurfacesDamage(true)
                    .visibleSurfacesHasDents(true)
                    .visibleSurfacesDentsDepth(dentValue)
                    .build();

            int expected = 2;

            // WHEN
            int actual = DamageCalculator.calculateDamageLevelForVisibleSurfaces(washingMachineDetailsDTO);

            // THEN
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class testGetRecommendation {

        @ParameterizedTest(name = "For damageLevel = {0} should return recommendation = {1}")
        @CsvSource({
                "1, REPACKAGE",
                "2, RESALE",
                "3, RESALE",
                "4, REPAIR",
                "5, DISASSEMBLE"
        })
        void should_ReturnRecommendation(int damageLevel, String expected) {
            // GIVEN

            // WHEN
            String actual = DamageCalculator.getRecommendation(damageLevel);

            // THEN
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest(name = "Damage level = {0} is not supported")
        @ValueSource(ints = {-2, -1, 0, 6, 7})
        void should_ThrowCustomException(int damageLevel) {
            // GIVEN

            // WHEN & THEN
            assertThatThrownBy(() -> DamageCalculator.getRecommendation(damageLevel))
                    .isInstanceOf(CustomException.class);
        }
    }
}