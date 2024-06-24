package org.personal.solarpanel.entity.dtos;

public record SolarPanelEvaluationDTO(
        int solarPanelIntegrity,
        String recommendation,
        String explanation
) {
}
