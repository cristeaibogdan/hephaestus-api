package org.personal.washingmachine.dto;

import java.time.LocalDateTime;

public record WashingMachineDTO(
        String category,
        String manufacturer,

        String damageType,
        String returnType,
        String identificationMode,

        String serialNumber,
        String model,
        String type,

        Integer damageLevel,
        String recommendation,
        LocalDateTime createdAt,

        WashingMachineDetailsDTO washingMachineDetailsDTO
) {
}