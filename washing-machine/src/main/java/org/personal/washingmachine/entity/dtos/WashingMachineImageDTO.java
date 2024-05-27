package org.personal.washingmachine.entity.dtos;

public record WashingMachineImageDTO (
        String imagePrefix,
        byte[] image
) {
}
