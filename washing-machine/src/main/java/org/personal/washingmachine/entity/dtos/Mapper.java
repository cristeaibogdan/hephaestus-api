package org.personal.washingmachine.entity.dtos;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.personal.washingmachine.entity.User;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineDetails;
import org.personal.washingmachine.entity.WashingMachineImage;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Mapper {

    public static class UserMapper {

        public static UserDTO toDTO(User entity) {
            return new UserDTO(
                    entity.getCode(),
                    entity.getOrganization(),
                    entity.getCountry(),
                    entity.getEmail(),
                    entity.getUsername(),
                    null
            );
        }

        public static User toEntity(UserDTO dto) {
            return new User(
                    dto.code(),
                    dto.organization(),
                    dto.country(),
                    dto.email(),
                    dto.username(),
                    dto.password()
            );
        }
    }

    public static class WashingMachineImageMapper {

        public static List<WashingMachineImageDTO> toDTO(List<WashingMachineImage> entities) {
            return entities.stream()
                    .map(entity -> new WashingMachineImageDTO(
                            entity.getImagePrefix(),
                            entity.getImage()))
                    .toList();
        }

    }

    public static class WashingMachineDetailsMapper {

        public static WashingMachineDetailsDTO toDTO(WashingMachineDetails entity) {
            return new WashingMachineDetailsDTO(
                    entity.isApplicablePackageDamage(),
                    entity.isPackageDamaged(),
                    entity.isPackageDirty(),
                    entity.isPackageMaterialAvailable(),

                    entity.isApplicableVisibleSurfacesDamage(),
                    entity.isVisibleSurfacesHasScratches(),
                    entity.getVisibleSurfacesScratchesLength(),
                    entity.isVisibleSurfacesHasDents(),
                    entity.getVisibleSurfacesDentsDepth(),
                    entity.isVisibleSurfacesHasSmallDamage(),
                    entity.getVisibleSurfacesSmallDamage(),
                    entity.isVisibleSurfacesHasBigDamage(),
                    entity.getVisibleSurfacesBigDamage(),

                    entity.isApplicableHiddenSurfacesDamage(),
                    entity.isHiddenSurfacesHasScratches(),
                    entity.getHiddenSurfacesScratchesLength(),
                    entity.isHiddenSurfacesHasDents(),
                    entity.getHiddenSurfacesDentsDepth(),
                    entity.isHiddenSurfacesHasSmallDamage(),
                    entity.getHiddenSurfacesSmallDamage(),
                    entity.isHiddenSurfacesHasBigDamage(),
                    entity.getHiddenSurfacesBigDamage(),

                    entity.getPrice(),
                    entity.getRepairPrice()
            );
        }

        public static WashingMachineDetails toEntity(WashingMachineDetailsDTO dto) {
            return new WashingMachineDetails(
                    dto.applicablePackageDamage(),
                    dto.packageDamaged(),
                    dto.packageDirty(),
                    dto.packageMaterialAvailable(),

                    dto.applicableVisibleSurfacesDamage(),
                    dto.visibleSurfacesHasScratches(),
                    dto.visibleSurfacesScratchesLength(),
                    dto.visibleSurfacesHasDents(),
                    dto.visibleSurfacesDentsDepth(),
                    dto.visibleSurfacesHasSmallDamage(),
                    dto.visibleSurfacesSmallDamage(),
                    dto.visibleSurfacesHasBigDamage(),
                    dto.visibleSurfacesBigDamage(),

                    dto.applicableHiddenSurfacesDamage(),
                    dto.hiddenSurfacesHasScratches(),
                    dto.hiddenSurfacesScratchesLength(),
                    dto.hiddenSurfacesHasDents(),
                    dto.hiddenSurfacesDentsDepth(),
                    dto.hiddenSurfacesHasSmallDamage(),
                    dto.hiddenSurfacesSmallDamage(),
                    dto.hiddenSurfacesHasBigDamage(),
                    dto.hiddenSurfacesBigDamage(),

                    dto.price(),
                    dto.repairPrice()
            );
        }

        public static class WashingMachineMapper {

            public static WashingMachineSimpleDTO toSimpleDTO(WashingMachine entity) {
                return new WashingMachineSimpleDTO(
                        entity.getCategory(),
                        entity.getManufacturer(),
                        entity.getDamageType(),
                        entity.getReturnType(),
                        entity.getIdentificationMode(),
                        entity.getSerialNumber(),
                        entity.getModel(),
                        entity.getType(),
                        entity.getDamageLevel(),
                        entity.getRecommendation(),
                        entity.getCreatedAt()
                );
            }

            public static WashingMachineExpandedDTO toExpandedDTO(WashingMachine entity) {

                WashingMachineDetailsDTO washingMachineDetailsDTO = WashingMachineDetailsMapper.toDTO(entity.getWashingMachineDetails());
                List<WashingMachineImageDTO> washingMachineImageDTOs = WashingMachineImageMapper.toDTO(entity.getWashingMachineImages());

                return new WashingMachineExpandedDTO(
                        washingMachineDetailsDTO,
                        washingMachineImageDTOs
                );
            }

            public static WashingMachine toEntity(WashingMachineDTO dto) {
                return new WashingMachine(
                        dto.category(),
                        dto.manufacturer(),
                        dto.damageType(),
                        dto.returnType(),
                        dto.identificationMode(),
                        dto.serialNumber(),
                        dto.model(),
                        dto.type(),
                        dto.damageLevel(),
                        dto.recommendation(),
                        WashingMachineDetailsMapper.toEntity(dto.washingMachineDetailsDTO())
                );
            }
        }
    }
}
