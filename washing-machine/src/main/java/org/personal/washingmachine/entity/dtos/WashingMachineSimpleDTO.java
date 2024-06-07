package org.personal.washingmachine.entity.dtos;

import java.time.LocalDateTime;

public record WashingMachineSimpleDTO(
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
        LocalDateTime createdAt
) {
}
