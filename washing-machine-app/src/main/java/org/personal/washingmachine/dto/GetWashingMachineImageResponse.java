package org.personal.washingmachine.dto;

public record GetWashingMachineImageResponse(
        String imagePrefix,
        byte[] image
) { }