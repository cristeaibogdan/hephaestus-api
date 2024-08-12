package org.personal.washingmachine.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
