package org.personal.washingmachine.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.personal.washingmachine.entity.WashingMachine;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Mapper {

	public static class WashingMachineMapper {

		public static WashingMachineSimpleDTO toSimpleDTO(WashingMachine entity) {
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

		public static WashingMachineExpandedDTO toExpandedDTO(WashingMachine entity) {

			WashingMachineDetailDTO washingMachineDetailDTO = new WashingMachineDetailDTO(entity.getWashingMachineDetail());
			List<WashingMachineImageDTO> washingMachineImageDTOs = entity.getWashingMachineImages().stream()
					.map(image -> new WashingMachineImageDTO(image))
					.toList();

			return new WashingMachineExpandedDTO(
					washingMachineDetailDTO,
					washingMachineImageDTOs
			);
		}

		public static WashingMachine toEntity(WashingMachineDTO dto) {
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
					dto.washingMachineDetailDTO().toEntity()
			);
		}
	}

}
