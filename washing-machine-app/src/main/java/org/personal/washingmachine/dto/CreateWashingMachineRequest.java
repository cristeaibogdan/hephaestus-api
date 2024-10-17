package org.personal.washingmachine.dto;

import jakarta.validation.Valid;
import lombok.Builder;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.ReturnType;

@Builder(toBuilder = true)
public record CreateWashingMachineRequest(
        String category,
		IdentificationMode identificationMode,
        String manufacturer,

        String model,
        String type,
        String serialNumber,

		ReturnType returnType,
		DamageType damageType,

		@Valid
        WashingMachineDetailDTO washingMachineDetailDTO
) { }