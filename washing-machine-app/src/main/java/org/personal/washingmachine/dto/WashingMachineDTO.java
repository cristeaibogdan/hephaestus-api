package org.personal.washingmachine.dto;

import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.ReturnType;

import java.time.LocalDateTime;

public record WashingMachineDTO(
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
        LocalDateTime createdAt,

        WashingMachineDetailsDTO washingMachineDetailsDTO
) {
}
