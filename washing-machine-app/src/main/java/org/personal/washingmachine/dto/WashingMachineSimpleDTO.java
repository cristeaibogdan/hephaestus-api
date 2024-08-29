package org.personal.washingmachine.dto;

import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.enums.ReturnType;

import java.time.LocalDateTime;

public record WashingMachineSimpleDTO(
        String category,

        String manufacturer,
		IdentificationMode identificationMode,

        String model,
        String type,
        String serialNumber,

		ReturnType returnType,
		DamageType damageType,

        Recommendation recommendation,
        LocalDateTime createdAt
) {
}
