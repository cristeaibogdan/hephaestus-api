package org.personal.washingmachine.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Entity
@Table(schema = "public", name = "washing_machine_details")
public class WashingMachineDetails extends BaseEntity {

    //TODO: Think about using @Embeddable to separate these properties: Package, Visible surfaces ...

    // PACKAGE
    @Column(name = "applicable_package_damage")
    private boolean applicablePackageDamage;

    @Column(name = "package_damaged")
    private boolean packageDamaged;

    @Column(name = "package_dirty")
    private boolean packageDirty;

    @Column(name = "package_material_available")
    private boolean packageMaterialAvailable;


    // VISIBLE SURFACES
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


    // HIDDEN SURFACES
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


    // PRICING
    @Column(name = "price")
    private Integer price;

    @Column(name = "repair_price")
    private Integer repairPrice;
}
