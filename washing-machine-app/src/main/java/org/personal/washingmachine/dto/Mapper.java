package org.personal.washingmachine.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.personal.washingmachine.entity.User;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineDetail;
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

	public static class WashingMachineDetailMapper {

		public static WashingMachineDetailDTO toDTO(WashingMachineDetail entity) {
			return new WashingMachineDetailDTO(
					entity.getPackageDamage().isApplicable(),
					entity.getPackageDamage().isPackageDamaged(),
					entity.getPackageDamage().isPackageDirty(),
					entity.getPackageDamage().isPackageMaterialAvailable(),

					entity.getVisibleSurfaceDamage().isApplicable(),
					entity.getVisibleSurfaceDamage().hasScratches(),
					entity.getVisibleSurfaceDamage().getVisibleSurfacesScratchesLength(),
					entity.getVisibleSurfaceDamage().hasDents(),
					entity.getVisibleSurfaceDamage().getVisibleSurfacesDentsDepth(),
					entity.getVisibleSurfaceDamage().hasMinorDamage(),
					entity.getVisibleSurfaceDamage().getVisibleSurfacesMinorDamage(),
					entity.getVisibleSurfaceDamage().hasMajorDamage(),
					entity.getVisibleSurfaceDamage().getVisibleSurfacesMajorDamage(),

					entity.getHiddenSurfaceDamage().isApplicable(),
					entity.getHiddenSurfaceDamage().hasScratches(),
					entity.getHiddenSurfaceDamage().getHiddenSurfacesScratchesLength(),
					entity.getHiddenSurfaceDamage().hasDents(),
					entity.getHiddenSurfaceDamage().getHiddenSurfacesDentsDepth(),
					entity.getHiddenSurfaceDamage().hasMinorDamage(),
					entity.getHiddenSurfaceDamage().getHiddenSurfacesMinorDamage(),
					entity.getHiddenSurfaceDamage().hasMajorDamage(),
					entity.getHiddenSurfaceDamage().getHiddenSurfacesMajorDamage(),

					entity.getPrice(),
					entity.getRepairPrice()
			);
		}

		public static WashingMachineDetail toEntity(WashingMachineDetailDTO dto) {
			return new WashingMachineDetail(
					new PackageDamage(
							dto.packageDamaged(),
							dto.packageDirty(),
							dto.packageMaterialAvailable()
					),

					new VisibleSurfaceDamage(
							dto.visibleSurfacesScratchesLength(),
							dto.visibleSurfacesDentsDepth(),
							dto.visibleSurfacesMinorDamage(),
							dto.visibleSurfacesMajorDamage()
					),

					new HiddenSurfaceDamage(
							dto.hiddenSurfacesScratchesLength(),
							dto.hiddenSurfacesDentsDepth(),
							dto.hiddenSurfacesMinorDamage(),
							dto.hiddenSurfacesMajorDamage()
					),

					dto.price(),
					dto.repairPrice()
			);
		}
	}

	public static class WashingMachineMapper {

		public static GetWashingMachineSimpleResponseDTO toSimpleDTO(WashingMachine entity) {
			return new GetWashingMachineSimpleResponseDTO(
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

		public static GetWashingMachineExpandedResponseDTO toExpandedDTO(WashingMachine entity) {

			WashingMachineDetailDTO washingMachineDetailDTO = WashingMachineDetailMapper.toDTO(entity.getWashingMachineDetail());
			List<WashingMachineImageDTO> washingMachineImageDTOs = WashingMachineImageMapper.toDTO(entity.getWashingMachineImages());

			return new GetWashingMachineExpandedResponseDTO(
					washingMachineDetailDTO,
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
					WashingMachineDetailMapper.toEntity(dto.washingMachineDetailDTO())
			);
		}
	}

}
