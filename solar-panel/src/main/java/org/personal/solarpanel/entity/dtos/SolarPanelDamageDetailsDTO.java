package org.personal.solarpanel.entity.dtos;

public record SolarPanelDamageDetailsDTO(
        int installationYear,
        boolean hasHotSpots,
        boolean hasMicrocracks,
        boolean hasSnailTrails,
        boolean hasBrokenGlass
) {
}
