package org.personal.washingmachine.dto;

import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.enums.ReturnType;

import java.time.LocalDateTime;
import java.util.List;

public record GetWashingMachineFullResponse(
		String category,

		String manufacturer,
		IdentificationMode identificationMode,

		String model,
		String type,
		String serialNumber,

		ReturnType returnType,
		DamageType damageType,

		Recommendation recommendation,
		LocalDateTime createdAt,

        GetWashingMachineDetailResponse washingMachineDetail,
        List<GetWashingMachineImageResponse> washingMachineImages
) {}