package org.personal.washingmachine.service.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.entity.dtos.WashingMachineDetailsDTO;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class DamageCalculator {

    public static int calculateDamageLevelForPackage(WashingMachineDetailsDTO washingMachineDetailsDTO) {
        int damageLevelForPackage = 0;

        if (!washingMachineDetailsDTO.applicablePackageDamage()) {
            return damageLevelForPackage;
        }

        damageLevelForPackage = washingMachineDetailsDTO.packageMaterialAvailable()
                ? 1
                : 2;

        return damageLevelForPackage;
    }

    public static int calculateDamageLevelForVisibleSurfaces(WashingMachineDetailsDTO washingMachineDetailsDTO) {
        int damageLevelForVisibleSurfaces = 0;

        if (!washingMachineDetailsDTO.applicableVisibleSurfacesDamage()) {
            return damageLevelForVisibleSurfaces;
        }

        // SCRATCHES
        if (washingMachineDetailsDTO.visibleSurfacesHasScratches()) {

            if (washingMachineDetailsDTO.visibleSurfacesScratchesLength() < 5) {
                damageLevelForVisibleSurfaces = Math.max(2, damageLevelForVisibleSurfaces);
            } else {
                damageLevelForVisibleSurfaces = Math.max(3, damageLevelForVisibleSurfaces);
            }
        }

        // DENTS
        if (washingMachineDetailsDTO.visibleSurfacesHasDents()) {

            if (washingMachineDetailsDTO.visibleSurfacesDentsDepth() < 5) {
                damageLevelForVisibleSurfaces = Math.max(2, damageLevelForVisibleSurfaces);
            } else {
                damageLevelForVisibleSurfaces = Math.max(3, damageLevelForVisibleSurfaces);
            }
        }

        // SMALL DAMAGE
        if (washingMachineDetailsDTO.visibleSurfacesHasSmallDamage()) {
            damageLevelForVisibleSurfaces = Math.max(2, damageLevelForVisibleSurfaces);
        }

        // BIG DAMAGE
        if (washingMachineDetailsDTO.visibleSurfacesHasBigDamage()) {
            damageLevelForVisibleSurfaces = Math.max(3, damageLevelForVisibleSurfaces);
        }

        return damageLevelForVisibleSurfaces;
    }

    public static int calculateDamageLevelForHiddenSurfaces(WashingMachineDetailsDTO washingMachineDetailsDTO) {
        int damageLevelForHiddenSurfaces = 0;

        if (!washingMachineDetailsDTO.applicableHiddenSurfacesDamage()) {
            return damageLevelForHiddenSurfaces;
        }

        // SCRATCHES
        if (washingMachineDetailsDTO.hiddenSurfacesHasScratches()) {

            if (washingMachineDetailsDTO.hiddenSurfacesScratchesLength() < 7) {
                damageLevelForHiddenSurfaces = Math.max(2, damageLevelForHiddenSurfaces);
            } else {
                damageLevelForHiddenSurfaces = Math.max(3, damageLevelForHiddenSurfaces);
            }
        }

        // DENTS
        if (washingMachineDetailsDTO.hiddenSurfacesHasDents()) {

            if (washingMachineDetailsDTO.hiddenSurfacesDentsDepth() < 7) {
                damageLevelForHiddenSurfaces = Math.max(2, damageLevelForHiddenSurfaces);
            } else {
                damageLevelForHiddenSurfaces = Math.max(3, damageLevelForHiddenSurfaces);
            }
        }

        // SMALL DAMAGE
        if (washingMachineDetailsDTO.hiddenSurfacesHasSmallDamage()) {
            damageLevelForHiddenSurfaces = Math.max(2, damageLevelForHiddenSurfaces);
        }

        // BIG DAMAGE
        if (washingMachineDetailsDTO.hiddenSurfacesHasBigDamage()) {
            damageLevelForHiddenSurfaces = Math.max(3, damageLevelForHiddenSurfaces);
        }

        return damageLevelForHiddenSurfaces;
    }

    public static int calculateDamageLevelForPricing(WashingMachineDetailsDTO washingMachineDetailsDTO) {
        int damageLevelForPricing = 0;

        if (washingMachineDetailsDTO.price() == null || washingMachineDetailsDTO.repairPrice() == null) {
            return damageLevelForPricing;
        }

        if (washingMachineDetailsDTO.price() <= 0 && washingMachineDetailsDTO.repairPrice() <= 0) {
            return damageLevelForPricing;
        }

        boolean repairPriceExceedsHalfTheProductPrice =
                (washingMachineDetailsDTO.repairPrice() >= washingMachineDetailsDTO.price() * 0.5);

        damageLevelForPricing = (repairPriceExceedsHalfTheProductPrice)
                ? 5
                : 4;

        return damageLevelForPricing;
    }

    public static String getRecommendation(int damageLevel) {
        return switch (damageLevel) {
            case 1 -> "REPACKAGE";
            case 2, 3 -> "RESALE";
            case 4 -> "REPAIR";
            case 5 -> "DISASSEMBLE";
            default -> throw new CustomException(ErrorCode.E_1009, "Invalid damage level");
        };
    }
}
