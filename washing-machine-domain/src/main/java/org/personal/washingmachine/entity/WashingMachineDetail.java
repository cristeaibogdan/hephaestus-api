package org.personal.washingmachine.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.entity.embedded.PackageDamage;
import org.personal.washingmachine.entity.embedded.HiddenSurfaceDamage;
import org.personal.washingmachine.entity.embedded.CostAssessment;
import org.personal.washingmachine.entity.embedded.VisibleSurfaceDamage;
import org.personal.washingmachine.enums.Recommendation;


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

    @Embedded
    private CostAssessment costAssessment;

    public WashingMachineDetail(WashingMachineDetail washingMachineDetail) {
        this(
            washingMachineDetail.getPackageDamage(),
            washingMachineDetail.getVisibleSurfaceDamage(),
            washingMachineDetail.getHiddenSurfaceDamage(),
            washingMachineDetail.getCostAssessment()
        );
    }

    public Recommendation getRecommendation() {
        Recommendation recommendationForPackage = packageDamage.calculate();
        Recommendation recommendationForVisibleSurfaces = visibleSurfaceDamage.calculate();
        Recommendation recommendationForHiddenSurfaces = hiddenSurfaceDamage.calculate();
        Recommendation recommendationForCost = costAssessment.calculate();

        Recommendation result = Collections.max(List.of(
                recommendationForPackage,
                recommendationForVisibleSurfaces,
                recommendationForHiddenSurfaces,
                recommendationForCost
        ));

        if (result == NONE) {
            throw new CustomException("Invalid recommendation issued " + NONE, ErrorCode.GENERAL);
        }

        return result;
    }
}
