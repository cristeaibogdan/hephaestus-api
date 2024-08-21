package org.personal.washingmachine.dto;

import org.personal.washingmachine.enums.ReturnType;

public record PageRequestDTO(
        Integer pageIndex,
        Integer pageSize,

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

        String createdAt
) {
}
