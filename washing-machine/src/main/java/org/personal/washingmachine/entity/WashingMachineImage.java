package org.personal.washingmachine.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.personal.washingmachine.entity.dtos.WashingMachineImageDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(schema = "public", name = "washing_machine_image")
public class WashingMachineImage extends BaseEntity {

    @Column(name = "image_prefix")
    private String imagePrefix;

    @Column(name = "image")
    private byte[] image;

    public WashingMachineImage(String imagePrefix, byte[] image) {
        this.imagePrefix = imagePrefix;
        this.image = image;
    }

    public WashingMachineImageDTO toWashingMachineImageDTO() {
        return new WashingMachineImageDTO(
                this.imagePrefix,
                this.image
        );
    }


}
