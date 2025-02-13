package org.personal.washingmachine.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.personal.washingmachine.enums.Recommendation;

import static org.personal.washingmachine.enums.Recommendation.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class PackageDamage {

    @Column(name = "package_damaged")
    private boolean packageDamaged;

    @Column(name = "package_dirty")
    private boolean packageDirty;

    @Column(name = "package_material_available")
    private boolean packageMaterialAvailable;

    public boolean isApplicable() {
        return packageDamaged ||
                packageDirty ||
                packageMaterialAvailable;
    }

    public boolean isNotApplicable() {
        return !isApplicable();
    }

    public Recommendation calculate() {
        if (isNotApplicable()) {
            return NONE;
        }

        return this.packageMaterialAvailable
                ? REPACKAGE
                : RESALE;
    }
}
