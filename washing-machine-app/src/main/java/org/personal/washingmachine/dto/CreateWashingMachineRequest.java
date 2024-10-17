package org.personal.washingmachine.dto;

import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.enums.ReturnType;

public record CreateWashingMachineRequest(
        String category,
		IdentificationMode identificationMode,
        String manufacturer,

        String model,
        String type,
        String serialNumber,

		ReturnType returnType,
		DamageType damageType,

        Recommendation recommendation,

        WashingMachineDetailDTO washingMachineDetailDTO
) { }