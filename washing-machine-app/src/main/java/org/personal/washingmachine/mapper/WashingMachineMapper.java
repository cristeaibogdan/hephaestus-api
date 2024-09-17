package org.personal.washingmachine.mapper;

import lombok.RequiredArgsConstructor;
import org.personal.washingmachine.dto.*;
import org.personal.washingmachine.entity.WashingMachine;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WashingMachineMapper {
	private final WashingMachineImageMapper washingMachineImageMapper;
	private final WashingMachineDetailMapper washingMachineDetailMapper;

	public WashingMachineSimpleDTO toSimpleDTO(WashingMachine entity) {
		return new WashingMachineSimpleDTO(
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

	public WashingMachineExpandedDTO toExpandedDTO(WashingMachine entity) {

		WashingMachineDetailDTO washingMachineDetailDTO = washingMachineDetailMapper.toDTO(entity.getWashingMachineDetail());
		List<WashingMachineImageDTO> washingMachineImageDTOs = washingMachineImageMapper.toDTO(entity.getWashingMachineImages());

		return new WashingMachineExpandedDTO(
				washingMachineDetailDTO,
				washingMachineImageDTOs
		);
	}

	public WashingMachine toEntity(WashingMachineDTO dto) {
		return new WashingMachine(
				dto.category(),
				dto.manufacturer(),
				dto.damageType(),
				dto.returnType(),
				dto.identificationMode(),
				dto.serialNumber(),
				dto.model(),
				dto.type(),
				dto.recommendation(),
				new WashingMachineDetailMapper().toEntity(dto.washingMachineDetailDTO())
		);
	}
}
