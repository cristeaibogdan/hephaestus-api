package org.personal.washingmachine.entity.dtos;

import lombok.Builder;

@Builder
public record WashingMachineDetailsDTO(
        // PACKAGE
        boolean applicablePackageDamage,

        boolean packageDamaged,
        boolean packageDirty,
        boolean packageMaterialAvailable,

        // VISIBLE SURFACES
        boolean applicableVisibleSurfacesDamage,

        boolean visibleSurfacesHasScratches,
        double visibleSurfacesScratchesLength,

        boolean visibleSurfacesHasDents,
        double visibleSurfacesDentsDepth,

        boolean visibleSurfacesHasSmallDamage,
        String visibleSurfacesSmallDamage,

        boolean visibleSurfacesHasBigDamage,
        String visibleSurfacesBigDamage,

        // HIDDEN SURFACES
        boolean applicableHiddenSurfacesDamage,

        boolean hiddenSurfacesHasScratches,
        double hiddenSurfacesScratchesLength,

        boolean hiddenSurfacesHasDents,
        double hiddenSurfacesDentsDepth,

        boolean hiddenSurfacesHasSmallDamage,
        String hiddenSurfacesSmallDamage,

        boolean hiddenSurfacesHasBigDamage,
        String hiddenSurfacesBigDamage,

        // PRICING
        Integer price,
        Integer repairPrice
) {}
