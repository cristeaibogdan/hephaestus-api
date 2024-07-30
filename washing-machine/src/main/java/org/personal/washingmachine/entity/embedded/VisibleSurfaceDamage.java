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


    @Column(name = "visible_surfaces_has_small_damage")
    private boolean visibleSurfacesHasSmallDamage;

    @Column(name = "visible_surfaces_small_damage")
    private String visibleSurfacesSmallDamage;


    @Column(name = "visible_surfaces_has_big_damage")
    private boolean visibleSurfacesHasBigDamage;

    @Column(name = "visible_surfaces_big_damage")
    private String visibleSurfacesBigDamage;
}
