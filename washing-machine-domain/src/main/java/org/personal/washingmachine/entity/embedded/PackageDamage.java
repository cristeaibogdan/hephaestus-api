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

    public static PackageDamageBuilder builder() { //TODO: Replace with lombok @Builder
        return new PackageDamageBuilder();
    }

    public static class PackageDamageBuilder {
        private boolean packageDamaged;
        private boolean packageDirty;
        private boolean packageMaterialAvailable;

        public PackageDamageBuilder packageDamaged(boolean packageDamaged) {
            this.packageDamaged = packageDamaged;
            return this;
        }

        public PackageDamageBuilder packageDirty(boolean packageDirty) {
            this.packageDirty = packageDirty;
            return this;
        }

        public PackageDamageBuilder packageMaterialAvailable(boolean packageMaterialAvailable) {
            this.packageMaterialAvailable = packageMaterialAvailable;
            return this;
        }

        public PackageDamage build() {
            return new PackageDamage(this.packageDamaged, this.packageDirty, this.packageMaterialAvailable);
        }
    }
}
