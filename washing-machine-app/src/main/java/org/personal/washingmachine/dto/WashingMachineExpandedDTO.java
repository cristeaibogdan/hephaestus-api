package org.personal.washingmachine.dto;

import org.personal.washingmachine.entity.WashingMachine;

import java.util.List;

public record WashingMachineExpandedDTO(
        WashingMachineDetailDTO washingMachineDetail,
        List<WashingMachineImageDTO> washingMachineImages
) {

	public WashingMachineExpandedDTO(WashingMachine entity) {

//		WashingMachineDetailDTO washingMachineDetailDTO = new WashingMachineDetailDTO(entity.getWashingMachineDetail());
//		List<WashingMachineImageDTO> washingMachineImageDTOs = entity.getWashingMachineImages().stream()
//				.map(image -> new WashingMachineImageDTO(image))
//				.toList();

		this(
				new WashingMachineDetailDTO(entity.getWashingMachineDetail()),
				entity.getWashingMachineImages().stream()
						.map(image -> new WashingMachineImageDTO(image))
						.toList()
		);
	}
}
