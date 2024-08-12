package org.personal.washingmachine.dto;

public record WashingMachineImageDTO (
        String imagePrefix,
        byte[] image
) {
}
