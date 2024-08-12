package org.personal.washingmachine.domain.entity.embedded;

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
public class VisibleSurfaceDamage {
    @Column(name = "applicable_visible_surfaces_damage")
    private boolean applicableVisibleSurfacesDamage;

    @Column(name = "visible_surfaces_has_scratches")
    private boolean visibleSurfacesHasScratches;

    @Column(name = "visible_surfaces_scratches_length")
    private double visibleSurfacesScratchesLength;


    @Column(name = "visible_surfaces_has_dents")
    private boolean visibleSurfacesHasDents;

    @Column(name = "visible_surfaces_dents_depth")
    private double visibleSurfacesDentsDepth;


    @Column(name = "visible_surfaces_has_minor_damage")
    private boolean visibleSurfacesHasMinorDamage;

    @Column(name = "visible_surfaces_minor_damage")
    private String visibleSurfacesMinorDamage;


    @Column(name = "visible_surfaces_has_major_damage")
    private boolean visibleSurfacesHasMajorDamage;

    @Column(name = "visible_surfaces_major_damage")
    private String visibleSurfacesMajorDamage;
}
