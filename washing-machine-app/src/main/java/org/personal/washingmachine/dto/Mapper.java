package org.personal.washingmachine.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.personal.washingmachine.entity.User;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineDetails;
import org.personal.washingmachine.entity.WashingMachineImage;
import org.personal.washingmachine.entity.embedded.HiddenSurfaceDamage;
import org.personal.washingmachine.entity.embedded.PackageDamage;
import org.personal.washingmachine.entity.embedded.VisibleSurfaceDamage;

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
					entity.getPackageDamage().isApplicablePackageDamage(),
					entity.getPackageDamage().isPackageDamaged(),
					entity.getPackageDamage().isPackageDirty(),
					entity.getPackageDamage().isPackageMaterialAvailable(),

					entity.getVisibleSurfaceDamage().isApplicableVisibleSurfacesDamage(),
					entity.getVisibleSurfaceDamage().isVisibleSurfacesHasScratches(),
					entity.getVisibleSurfaceDamage().getVisibleSurfacesScratchesLength(),
					entity.getVisibleSurfaceDamage().isVisibleSurfacesHasDents(),
					entity.getVisibleSurfaceDamage().getVisibleSurfacesDentsDepth(),
					entity.getVisibleSurfaceDamage().isVisibleSurfacesHasMinorDamage(),
					entity.getVisibleSurfaceDamage().getVisibleSurfacesMinorDamage(),
					entity.getVisibleSurfaceDamage().isVisibleSurfacesHasMajorDamage(),
					entity.getVisibleSurfaceDamage().getVisibleSurfacesMajorDamage(),

					entity.getHiddenSurfaceDamage().isApplicableHiddenSurfacesDamage(),
					entity.getHiddenSurfaceDamage().isHiddenSurfacesHasScratches(),
					entity.getHiddenSurfaceDamage().getHiddenSurfacesScratchesLength(),
					entity.getHiddenSurfaceDamage().isHiddenSurfacesHasDents(),
					entity.getHiddenSurfaceDamage().getHiddenSurfacesDentsDepth(),
					entity.getHiddenSurfaceDamage().isHiddenSurfacesHasMinorDamage(),
					entity.getHiddenSurfaceDamage().getHiddenSurfacesMinorDamage(),
					entity.getHiddenSurfaceDamage().isHiddenSurfacesHasMajorDamage(),
					entity.getHiddenSurfaceDamage().getHiddenSurfacesMajorDamage(),

					entity.getPrice(),
					entity.getRepairPrice()
			);
		}

		public static WashingMachineDetails toEntity(WashingMachineDetailsDTO dto) {
			return new WashingMachineDetails(
					new PackageDamage(
							dto.applicablePackageDamage(),
							dto.packageDamaged(),
							dto.packageDirty(),
							dto.packageMaterialAvailable()
					),

					new VisibleSurfaceDamage(
							dto.applicableVisibleSurfacesDamage(),
							dto.visibleSurfacesHasScratches(),
							dto.visibleSurfacesScratchesLength(),
							dto.visibleSurfacesHasDents(),
							dto.visibleSurfacesDentsDepth(),
							dto.visibleSurfacesHasMinorDamage(),
							dto.visibleSurfacesMinorDamage(),
							dto.visibleSurfacesHasMajorDamage(),
							dto.visibleSurfacesMajorDamage()
					),

					new HiddenSurfaceDamage(
							dto.applicableHiddenSurfacesDamage(),
							dto.hiddenSurfacesHasScratches(),
							dto.hiddenSurfacesScratchesLength(),
							dto.hiddenSurfacesHasDents(),
							dto.hiddenSurfacesDentsDepth(),
							dto.hiddenSurfacesHasMinorDamage(),
							dto.hiddenSurfacesMinorDamage(),
							dto.hiddenSurfacesHasMajorDamage(),
							dto.hiddenSurfacesMajorDamage()
					),

					dto.price(),
					dto.repairPrice()
			);
		}
	}

	public static class WashingMachineMapper {

		public static WashingMachineSimpleDTO toSimpleDTO(WashingMachine entity) {
			return new WashingMachineSimpleDTO(
					entity.getCategory(),
					entity.getManufacturer(),
					entity.getIdentificationMode(),
					entity.getModel(),
					entity.getType(),
					entity.getSerialNumber(),
					entity.getReturnType(),
					entity.getDamageType(),
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
					dto.recommendation(),
					WashingMachineDetailsMapper.toEntity(dto.washingMachineDetailsDTO())
			);
		}
	}

}
