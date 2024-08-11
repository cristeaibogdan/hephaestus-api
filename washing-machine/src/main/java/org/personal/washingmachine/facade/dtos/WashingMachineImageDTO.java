package org.personal.washingmachine.facade.dtos;

public record WashingMachineImageDTO (
        String imagePrefix,
        byte[] image
) {
}
