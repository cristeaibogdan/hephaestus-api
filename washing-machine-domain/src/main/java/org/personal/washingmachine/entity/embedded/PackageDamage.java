package org.personal.washingmachine.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
}
