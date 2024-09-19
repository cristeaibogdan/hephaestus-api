package org.personal.washingmachine.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class VisibleSurfaceDamage {

    @Column(name = "visible_surfaces_scratches_length")
    private double visibleSurfacesScratchesLength;

    @Column(name = "visible_surfaces_dents_depth")
    private double visibleSurfacesDentsDepth;

    @Column(name = "visible_surfaces_minor_damage")
    private String visibleSurfacesMinorDamage;


    @Column(name = "visible_surfaces_has_major_damage")
    private boolean visibleSurfacesHasMajorDamage;

    @Column(name = "visible_surfaces_major_damage")
    private String visibleSurfacesMajorDamage;

    public boolean isApplicable() {
        return hasScratches() ||
                hasDents() ||
                hasMinorDamage() ||
                visibleSurfacesHasMajorDamage;
    }

    public boolean hasScratches() { // TODO: Min(0) and Max(10) can be set into DTO validation
        return visibleSurfacesScratchesLength > 0;
    }

    public boolean hasDents() { // TODO: Min(0) and Max(10) can be set into DTO validation
        return visibleSurfacesDentsDepth > 0;
    }

    public boolean hasMinorDamage() { // TODO: @NotNull can be set into DTO validation
        return isNotBlank(visibleSurfacesMinorDamage);
    }

    public static VisibleSurfaceDamageBuilder builder() { //TODO: Replace with lombok @Builder
        return new VisibleSurfaceDamageBuilder();
    }

    public static class VisibleSurfaceDamageBuilder {
        private double visibleSurfacesScratchesLength;
        private double visibleSurfacesDentsDepth;

        private String visibleSurfacesMinorDamage;

        private boolean visibleSurfacesHasMajorDamage;
        private String visibleSurfacesMajorDamage;

        public VisibleSurfaceDamage build() {
            return new VisibleSurfaceDamage(
                    this.visibleSurfacesScratchesLength,
                    this.visibleSurfacesDentsDepth,
                    this.visibleSurfacesMinorDamage,
                    this.visibleSurfacesHasMajorDamage,
                    this.visibleSurfacesMajorDamage
            );
        }

        public VisibleSurfaceDamageBuilder visibleSurfacesScratchesLength(double visibleSurfacesScratchesLength) {
            this.visibleSurfacesScratchesLength = visibleSurfacesScratchesLength;
            return this;
        }

        public VisibleSurfaceDamageBuilder visibleSurfacesDentsDepth (double visibleSurfacesDentsDepth) {
            this.visibleSurfacesDentsDepth = visibleSurfacesDentsDepth;
            return this;
        }

        public VisibleSurfaceDamageBuilder visibleSurfacesMinorDamage (String visibleSurfacesMinorDamage) {
            this.visibleSurfacesMinorDamage = visibleSurfacesMinorDamage;
            return this;
        }

        public VisibleSurfaceDamageBuilder visibleSurfacesHasMajorDamage (boolean visibleSurfacesHasMajorDamage) {
            this.visibleSurfacesHasMajorDamage = visibleSurfacesHasMajorDamage;
            return this;
        }

        public VisibleSurfaceDamageBuilder visibleSurfacesMajorDamage (String visibleSurfacesMajorDamage) {
            this.visibleSurfacesMajorDamage = visibleSurfacesMajorDamage;
            return this;
        }
    }
}
