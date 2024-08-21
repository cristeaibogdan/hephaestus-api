package org.personal.washingmachine.dto;

import org.personal.washingmachine.enums.ReturnType;

import java.time.LocalDateTime;

public record WashingMachineSimpleDTO(
        String category,
        String manufacturer,

        String damageType,
        ReturnType returnType,
        String identificationMode,

        String serialNumber,
        String model,
        String type,

        Integer damageLevel,
        String recommendation,
        LocalDateTime createdAt
) {
}
