package org.personal.washingmachine.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.personal.washingmachine.entity.embedded.HiddenSurfaceDamage;
import org.personal.washingmachine.entity.embedded.PackageDamage;
import org.personal.washingmachine.entity.embedded.VisibleSurfaceDamage;


import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Entity
@Table(schema = "public", name = "washing_machine_details")
public class WashingMachineDetails extends BaseEntity {

    @Embedded
    private PackageDamage packageDamage;

    @Embedded
    private VisibleSurfaceDamage visibleSurfaceDamage;

    @Embedded
    private HiddenSurfaceDamage hiddenSurfaceDamage;

    @Column(name = "price")
    private Integer price;

    @Column(name = "repair_price")
    private Integer repairPrice;
}
