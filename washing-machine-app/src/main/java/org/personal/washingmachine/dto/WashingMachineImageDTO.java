package org.personal.washingmachine.dto;

import org.personal.washingmachine.entity.WashingMachineImage;

public record WashingMachineImageDTO (
        String imagePrefix,
        byte[] image
) {

	public WashingMachineImageDTO(WashingMachineImage entity) {
		 this(
				 entity.getImagePrefix(),
				 entity.getImage()
		 );
	}

	public WashingMachineImage toEntity() {
		return new WashingMachineImage(
				this.imagePrefix,
				this.image
		);
	}
}
