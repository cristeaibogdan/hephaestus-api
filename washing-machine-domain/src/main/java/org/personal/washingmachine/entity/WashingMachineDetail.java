package org.personal.washingmachine.entity;

import com.google.common.annotations.VisibleForTesting;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.entity.embedded.PackageDamage;
import org.personal.washingmachine.entity.embedded.HiddenSurfaceDamage;
import org.personal.washingmachine.entity.embedded.VisibleSurfaceDamage;
import org.personal.washingmachine.enums.Recommendation;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    public WashingMachineDetail(WashingMachineDetail washingMachineDetail) {
        this.packageDamage = washingMachineDetail.getPackageDamage();
        this.visibleSurfaceDamage = washingMachineDetail.getVisibleSurfaceDamage();
        this.hiddenSurfaceDamage = washingMachineDetail.getHiddenSurfaceDamage();
        this.price = washingMachineDetail.getPrice();
        this.repairPrice = washingMachineDetail.getRepairPrice();
    }

    public Recommendation getRecommendation() {
        Recommendation recommendationForPackage = packageDamage.calculate();
        Recommendation recommendationForVisibleSurfaces = visibleSurfaceDamage.calculate();
        Recommendation recommendationForHiddenSurfaces = hiddenSurfaceDamage.calculate();
        Recommendation recommendationForPricing = calculate(); // TODO: Refactor for a better name

        Recommendation result = Collections.max(List.of(
                recommendationForPackage,
                recommendationForVisibleSurfaces,
                recommendationForHiddenSurfaces,
                recommendationForPricing
        ));

        if (result == NONE) {
            throw new CustomException("Invalid recommendation issued " + NONE, ErrorCode.GENERAL);
        }

        return result;
    }

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
