package org.personal.washingmachine.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.personal.washingmachine.entity.WashingMachine;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Mapper {

	public static class WashingMachineMapper {

		public static GetWashingMachineSimpleResponse toGetWashingMachineSimpleResponse(WashingMachine entity) {
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

		public static GetWashingMachineFullResponse toGetWashingMachineFullResponse(WashingMachine entity) {
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

					new WashingMachineDetailMapper().toGetWashingMachineDetailResponse(entity.getWashingMachineDetail()),
					new WashingMachineImageMapper().toGetWashingMachineImageResponses(entity.getWashingMachineImages())
			);
		}

		public static WashingMachine toEntity(CreateWashingMachineRequest dto) {
			return new WashingMachine(
					dto.category(),
					dto.manufacturer(),
					dto.damageType(),
					dto.returnType(),
					dto.identificationMode(),
					dto.serialNumber(),
					dto.model(),
					dto.type(),
					null, //TODO: What to do with this?
					new WashingMachineDetailMapper().toEntity(dto.createWashingMachineDetailRequest())
			);
		}
	}

}
