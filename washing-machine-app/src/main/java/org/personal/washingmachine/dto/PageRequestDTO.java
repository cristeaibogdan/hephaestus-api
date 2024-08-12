package org.personal.washingmachine.dto;

public record PageRequestDTO(
        Integer pageIndex,
        Integer pageSize,

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

        String createdAt
) {
}