package org.personal.washingmachine.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.personal.washingmachine.entity.dtos.WashingMachineDTO;
import org.personal.washingmachine.entity.dtos.WashingMachineExpandedDTO;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(schema = "public", name = "washing_machine")
public class WashingMachine extends BaseEntity {

    @Column(name = "category")
    private String category;

    @Column(name = "manufacturer")
    private String manufacturer;


    @Column(name = "damage_type")
    private String damageType;

    @Column(name = "return_type")
    private String returnType;

    @Column(name = "identification_mode")
    private String identificationMode;


    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "model")
    private String model;

    @Column(name = "type")
    private String type;

    @Column(name = "damage_level")
    private Integer damageLevel;

    @Column(name = "recommendation")
    private String recommendation;

    @Setter(NONE)
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "washing_machine_details_id")
    private WashingMachineDetails washingMachineDetails;

    @JsonManagedReference
    @OneToMany(mappedBy = "washingMachine", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WashingMachineImage> washingMachineImages = new ArrayList<>();

    public void setWashingMachineImages(List<WashingMachineImage> washingMachineImages) {
        washingMachineImages.forEach(image -> {
            image.setWashingMachine(this);
            this.washingMachineImages.add(image);
        });
    }

    public WashingMachineDTO toWashingMachineDTO() {
        return new WashingMachineDTO(
                this.category,
                this.manufacturer,

                this.damageType,
                this.returnType,
                this.identificationMode,

                this.serialNumber,
                this.model,
                this.type,
                this.damageLevel,
                this.recommendation,

                this.createdAt
        );
    }

    public WashingMachineExpandedDTO toWashingMachineExpandedDTO() {
        return new WashingMachineExpandedDTO(
                this.washingMachineDetails.toWashingMachineDetailsDTO(),
                this.washingMachineImages.stream()
                        .map(washingMachineImage -> washingMachineImage.toWashingMachineImageDTO())
                        .toList()
        );
    }
}
