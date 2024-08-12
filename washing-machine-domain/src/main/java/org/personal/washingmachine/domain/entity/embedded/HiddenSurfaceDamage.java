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


    @Column(name = "hidden_surfaces_has_minor_damage")
    private boolean hiddenSurfacesHasMinorDamage;

    @Column(name = "hidden_surfaces_minor_damage")
    private String hiddenSurfacesMinorDamage;


    @Column(name = "hidden_surfaces_has_major_damage")
    private boolean hiddenSurfacesHasMajorDamage;

    @Column(name = "hidden_surfaces_major_damage")
    private String hiddenSurfacesMajorDamage;
}
