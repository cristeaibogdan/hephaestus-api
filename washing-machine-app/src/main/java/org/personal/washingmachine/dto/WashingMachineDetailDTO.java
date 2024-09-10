package org.personal.washingmachine.dto;

import lombok.Builder;

@Builder
public record WashingMachineDetailDTO(
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

        boolean visibleSurfacesHasMinorDamage,
        String visibleSurfacesMinorDamage,

        boolean visibleSurfacesHasMajorDamage,
        String visibleSurfacesMajorDamage,

        // HIDDEN SURFACES
        boolean applicableHiddenSurfacesDamage,

        boolean hiddenSurfacesHasScratches,
        double hiddenSurfacesScratchesLength,

        boolean hiddenSurfacesHasDents,
        double hiddenSurfacesDentsDepth,

        boolean hiddenSurfacesHasMinorDamage,
        String hiddenSurfacesMinorDamage,

        boolean hiddenSurfacesHasMajorDamage,
        String hiddenSurfacesMajorDamage,

        // PRICING
        Integer price,
        Integer repairPrice
) {}
