package org.personal.washingmachine.mapper;

import lombok.RequiredArgsConstructor;
import org.personal.washingmachine.dto.SaveWashingMachineRequest;
import org.personal.washingmachine.dto.GetWashingMachineFullResponse;
import org.personal.washingmachine.dto.GetWashingMachineSimpleResponse;
import org.personal.washingmachine.entity.WashingMachine;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class WashingMachineMapper {
	private final WashingMachineDetailMapper washingMachineDetailMapper;
	private final WashingMachineImageMapper washingMachineImageMapper;

	public GetWashingMachineSimpleResponse toGetWashingMachineSimpleResponse(WashingMachine entity) {
		return new GetWashingMachineSimpleResponse(
				entity.getCategory(),
				entity.getManufacturer(),
				entity.getIdentificationMode(),
				entity.getModel(),
				entity.getType(),
				entity.getSerialNumber(),
				entity.getReturnType(),
				entity.getDamageType(),
				entity.getRecommendation(),
				entity.getCreatedAt()
		);
	}

	public GetWashingMachineFullResponse toGetWashingMachineFullResponse(WashingMachine entity) {
		return new GetWashingMachineFullResponse(
				entity.getCategory(),
				entity.getManufacturer(),
				entity.getIdentificationMode(),
				entity.getModel(),
				entity.getType(),
				entity.getSerialNumber(),
				entity.getReturnType(),
				entity.getDamageType(),
				entity.getRecommendation(),
				entity.getCreatedAt(),

				washingMachineDetailMapper.toGetWashingMachineDetailResponse(entity.getWashingMachineDetail()),
				washingMachineImageMapper.toGetWashingMachineImageResponses(entity.getWashingMachineImages())
		);
	}

	public WashingMachine toEntity(SaveWashingMachineRequest dto) {
		return new WashingMachine(
				dto.category(),
				dto.manufacturer(),
				dto.damageType(),
				dto.returnType(),
				dto.identificationMode(),
				dto.serialNumber(),
				dto.model(),
				dto.type(),
				washingMachineDetailMapper.toEntity(dto.saveWashingMachineDetailRequest())
		);
	}
}
