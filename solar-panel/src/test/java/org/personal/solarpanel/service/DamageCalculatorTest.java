package org.personal.solarpanel.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.personal.solarpanel.entity.dtos.SolarPanelDamageDetailsDTO;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

//@ExtendWith(MockitoExtension.class) - not needed if no dependency injected
class DamageCalculatorTest {

//    @InjectMocks - not needed if no dependency injected
    private final DamageCalculator underTest = new DamageCalculator();

    @Nested
    class testCalculateSolarPanelIntegrity {

        @Test
        void should_ReturnSolarPanelIntegrityEighty() {
            // GIVEN
            SolarPanelDamageDetailsDTO solarPanelDamageDetailsDTO = new SolarPanelDamageDetailsDTO(
                    2024,
                    true,
                    false,
                    false,
                    false);

            int expected = 80;

            // WHEN
            int solarPanelIntegrity = underTest.calculateSolarPanelIntegrity(solarPanelDamageDetailsDTO);

            // THEN
            assertThat(solarPanelIntegrity).isEqualTo(expected);
        }

        @Test
        void should_ReturnSolarPanelIntegritySixty() {
            // GIVEN
            SolarPanelDamageDetailsDTO solarPanelDamageDetailsDTO = new SolarPanelDamageDetailsDTO(
                    2024,
                    true,
                    true,
                    false,
                    false);

            int expected = 60;

            // WHEN
            int solarPanelIntegrity = underTest.calculateSolarPanelIntegrity(solarPanelDamageDetailsDTO);

            // THEN
            assertThat(solarPanelIntegrity).isEqualTo(expected);
        }

        @Test
        void should_ReturnSolarPanelIntegrityForty() {
            // GIVEN
            SolarPanelDamageDetailsDTO solarPanelDamageDetailsDTO = new SolarPanelDamageDetailsDTO(
                    2024,
                    true,
                    true,
                    true,
                    false);

            int expected = 40;

            // WHEN
            int solarPanelIntegrity = underTest.calculateSolarPanelIntegrity(solarPanelDamageDetailsDTO);

            // THEN
            assertThat(solarPanelIntegrity).isEqualTo(expected);
        }

        @Test
        void should_ReturnSolarPanelIntegrityTwenty() {
            // GIVEN
            SolarPanelDamageDetailsDTO solarPanelDamageDetailsDTO = new SolarPanelDamageDetailsDTO(
                    2024,
                    true,
                    true,
                    true,
                    true);

            int expected = 20;

            // WHEN
            int solarPanelIntegrity = underTest.calculateSolarPanelIntegrity(solarPanelDamageDetailsDTO);

            // THEN
            assertThat(solarPanelIntegrity).isEqualTo(expected);
        }
    }

    @Nested
    class testCalculateIntegrityForInstallationYear {

        @ParameterizedTest
        @ValueSource(ints = {2024, 2023, 2022})
        void should_Return0_When_DifferenceIsBetween0And2Inclusive(int installationYear) {
            // GIVEN
            int currentYear = 2024;
            int expected = 0;

            // WHEN
            int actual = underTest.calculateIntegrityForInstallationYear(currentYear, installationYear);

            // THEN
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @ValueSource(ints = {2021, 2017, 2016, 2015, 2014})
        void should_Return5_When_DifferenceIsBetween2And10Inclusive(int installationYear) {
            // GIVEN
            int currentYear = 2024;
            int expected = 5;

            // WHEN
            int actual = underTest.calculateIntegrityForInstallationYear(currentYear, installationYear);

            // THEN
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @ValueSource(ints = {2013, 2012, 2011, 2010, 2009, 2008, 2004})
        void should_Return15_When_DifferenceIsBetween10And20Inclusive(int installationYear) {
            // GIVEN
            int currentYear = 2024;
            int expected = 15;

            // WHEN
            int actual = underTest.calculateIntegrityForInstallationYear(currentYear, installationYear);

            // THEN
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @ValueSource(ints = {2000, 2002, 2003})
        void should_Return20_When_DifferenceIsOver20(int installationYear) {
            // GIVEN
            int currentYear = 2024;
            int expected = 20;

            // WHEN
            int actual = underTest.calculateIntegrityForInstallationYear(currentYear, installationYear);

            // THEN
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, -2, -5})
        void should_ThrowRuntimeException_When_DifferenceNegative(int installationYear) {
            // GIVEN
            int currentYear = -2;

            // WHEN & THEN
            assertThatThrownBy(() -> underTest.calculateIntegrityForInstallationYear(currentYear, installationYear))
                    .isInstanceOf(RuntimeException.class);
        }
    }
}