package org.personal.washingmachine.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.personal.washingmachine.entity.embedded.HiddenSurfaceDamage;
import org.personal.washingmachine.entity.embedded.PackageDamage;
import org.personal.washingmachine.entity.embedded.VisibleSurfaceDamage;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

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
