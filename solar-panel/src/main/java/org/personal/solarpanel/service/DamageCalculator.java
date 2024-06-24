package org.personal.solarpanel.service;

import org.personal.solarpanel.entity.dtos.SolarPanelDamageDetailsDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DamageCalculator {
    public int calculateSolarPanelIntegrity(SolarPanelDamageDetailsDTO solarPanelDamageDetailsDTO) {
        int integrity = 100;

        if (solarPanelDamageDetailsDTO.hasHotSpots()) {
            integrity -= 20;
        }

        if (solarPanelDamageDetailsDTO.hasMicrocracks()) {
            integrity -= 20;
        }

        if (solarPanelDamageDetailsDTO.hasSnailTrails()) {
            integrity -= 20;
        }

        if (solarPanelDamageDetailsDTO.hasBrokenGlass()) {
            integrity -= 20;
        }

        int integrityForYearOfInstallation = calculateIntegrityForInstallationYear(
                LocalDate.now().getYear(),
                solarPanelDamageDetailsDTO.installationYear());

        return (integrity+integrityForYearOfInstallation);
    }

    int calculateIntegrityForInstallationYear(int currentYear, int installationYear) {
        if (currentYear < 0 || installationYear < 0) {
            throw new RuntimeException("Input parameters can not be null");
        }

        int age = currentYear - installationYear;

        return switch (age) {
            case 0, 1, 2 -> 0;
            case 3, 4, 5, 6, 7, 8, 9, 10 -> 5;
            case 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 -> 15;
            default -> 20;
        };
    }
}
