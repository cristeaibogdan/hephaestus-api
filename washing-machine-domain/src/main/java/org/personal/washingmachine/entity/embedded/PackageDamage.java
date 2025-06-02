package org.personal.washingmachine.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.personal.washingmachine.enums.Recommendation;

import static org.personal.washingmachine.enums.Recommendation.*;

@Getter
@With
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class PackageDamage implements Damage {

    @Column(name = "package_damaged")
    private boolean packageDamaged;

    @Column(name = "package_dirty")
    private boolean packageDirty;

    @Column(name = "package_material_available")
    private boolean packageMaterialAvailable;

    @Override
    public boolean isApplicable() {
        return packageDamaged ||
                packageDirty ||
                packageMaterialAvailable;
    }

    @Override
    public Recommendation calculate() {
        if (isNotApplicable()) {
            return NONE;
        }

        return this.packageMaterialAvailable
                ? REPACKAGE
                : RESALE;
    }
}
