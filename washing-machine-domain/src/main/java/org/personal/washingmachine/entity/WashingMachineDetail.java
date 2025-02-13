package org.personal.washingmachine.entity;

import com.google.common.annotations.VisibleForTesting;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.personal.washingmachine.entity.embedded.PackageDamage;
import org.personal.washingmachine.entity.embedded.HiddenSurfaceDamage;
import org.personal.washingmachine.entity.embedded.VisibleSurfaceDamage;
import org.personal.washingmachine.enums.Recommendation;


import static lombok.AccessLevel.PROTECTED;
import static org.personal.washingmachine.enums.Recommendation.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Entity
@Table(schema = "public", name = "washing_machine_detail")
public class WashingMachineDetail extends BaseEntity {

    @Embedded
    private PackageDamage packageDamage;

    @Embedded
    private VisibleSurfaceDamage visibleSurfaceDamage;

    @Embedded
    private HiddenSurfaceDamage hiddenSurfaceDamage;

    @Column(name = "price")
    private int price;

    @Column(name = "repair_price")
    private int repairPrice;

    @VisibleForTesting
    Recommendation calculate() {

        if (this.price == 0 || this.repairPrice == 0) {
            return NONE;
        }

        boolean repairPriceExceedsHalfTheProductPrice = (this.repairPrice >= this.price * 0.5);

        return repairPriceExceedsHalfTheProductPrice
                ? DISASSEMBLE
                : REPAIR;
    }
}
