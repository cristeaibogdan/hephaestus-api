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
public class HiddenSurfaceDamage {
    @Column(name = "applicable_hidden_surfaces_damage")
    private boolean applicableHiddenSurfacesDamage;


    @Column(name = "hidden_surfaces_has_scratches")
    private boolean hiddenSurfacesHasScratches;

    @Column(name = "hidden_surfaces_scratches_length")
    private double hiddenSurfacesScratchesLength;


    @Column(name = "hidden_surfaces_has_dents")
    private boolean hiddenSurfacesHasDents;

    @Column(name = "hidden_surfaces_dents_depth")
    private double hiddenSurfacesDentsDepth;


    @Column(name = "hidden_surfaces_has_small_damage")
    private boolean hiddenSurfacesHasSmallDamage;

    @Column(name = "hidden_surfaces_small_damage")
    private String hiddenSurfacesSmallDamage;


    @Column(name = "hidden_surfaces_has_big_damage")
    private boolean hiddenSurfacesHasBigDamage;

    @Column(name = "hidden_surfaces_big_damage")
    private String hiddenSurfacesBigDamage;
}
