package org.personal.washingmachine.entity;

import com.google.common.collect.ImmutableList;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.enums.ReturnType;

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


    @Enumerated(EnumType.STRING)
    @Column(name = "identification_mode")
    private IdentificationMode identificationMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "return_type")
    private ReturnType returnType;

    @Enumerated(EnumType.STRING)
    @Column(name = "damage_type")
    private DamageType damageType;


    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "model")
    private String model;

    @Column(name = "type")
    private String type;

    @Enumerated(EnumType.STRING)
    @Column(name = "recommendation")
    private Recommendation recommendation;

    @Setter(NONE)
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @JoinColumn(name = "washing_machine_detail_id")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private WashingMachineDetail washingMachineDetail;

    // TODO: Consider adding nonNull argument in constructor argument.
    @Setter(NONE) @Getter(NONE)
    @JoinColumn(name = "washing_machine_id")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WashingMachineImage> washingMachineImages = new ArrayList<>();

    public WashingMachine(String category, String manufacturer, DamageType damageType, ReturnType returnType, IdentificationMode identificationMode, String serialNumber, String model, String type, Recommendation recommendation, WashingMachineDetail washingMachineDetail) {
        this.category = category;
        this.manufacturer = manufacturer;
        this.damageType = damageType;
        this.returnType = returnType;
        this.identificationMode = identificationMode;
        this.serialNumber = serialNumber;
        this.model = model;
        this.type = type;
        this.recommendation = recommendation;
        this.washingMachineDetail = washingMachineDetail;
    }

    // TODO: Should return a copy
    public WashingMachineDetail getWashingMachineDetail() {
        return washingMachineDetail;
    }

    public ImmutableList<WashingMachineImage> getWashingMachineImages() {
        return ImmutableList.copyOf(washingMachineImages);
    }

    public void addImage(WashingMachineImage washingMachineImage) {
        this.washingMachineImages.add(washingMachineImage);
    }

    public boolean removeImage(Long washingMachineImageId) {
        return washingMachineImages.stream()
                .filter(image -> image.getId().equals(washingMachineImageId))
                .findFirst()
                .filter(image -> washingMachineImages.remove(image))
                .isPresent();
    }
}
