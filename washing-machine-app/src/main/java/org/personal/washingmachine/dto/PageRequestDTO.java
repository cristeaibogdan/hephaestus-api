package org.personal.washingmachine.dto;

import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.ReturnType;

public record PageRequestDTO(
        Integer pageIndex,
        Integer pageSize,

        String category,
        String manufacturer,

		IdentificationMode identificationMode,
		ReturnType returnType,
		DamageType damageType,

        String serialNumber,
        String model,
        String type,

        Integer damageLevel,
        String recommendation,

        String createdAt
) {
}
