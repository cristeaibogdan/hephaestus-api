package org.personal.washingmachine.dto;

import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.ReturnType;

public record PageRequestDTO(
        Integer pageIndex,
        Integer pageSize,

		IdentificationMode identificationMode,
		String manufacturer,

		String model,
		String type,
		String serialNumber,

		ReturnType returnType,
		DamageType damageType,

        Integer damageLevel,
        String recommendation,

        String createdAt
) {
}
