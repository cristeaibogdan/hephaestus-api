package org.personal.washingmachine.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Mapper {

	public static class WashingMachineImageMapper {

		public static List<GetWashingMachineImageResponse> toGetWashingMachineImageResponses(List<WashingMachineImage> entities) {
			return entities.stream()
					.map(entity -> new GetWashingMachineImageResponse(
							entity.getImagePrefix(),
							entity.getImage()))
					.toList();
		}

		public static WashingMachineImage toEntity(MultipartFile imageFile) {

			byte[] image;

			try {
				image = imageFile.getBytes();
			} catch (IOException e) {
				throw new CustomException("Could not extract bytes from image: " + imageFile.getName(), e, ErrorCode.GENERAL);
			}

			String imagePrefix = "data:image/" + getImageExtension(imageFile) + ";base64,";

			return new WashingMachineImage(imagePrefix, image);
		}

		private static String getImageExtension(MultipartFile imageFile) {
			String extension = org.springframework.util.StringUtils.getFilenameExtension(imageFile.getOriginalFilename());

			return switch (extension.toLowerCase()) {
				case "png" -> "png";
				case "jpg" -> "jpg";
				case "jpeg" -> "jpeg";
				case "bmp" -> "bmp";
				default -> throw new CustomException("Invalid image extension: " + extension, ErrorCode.GENERAL);
			};
		}
	}

	public static class WashingMachineMapper {

		public static GetWashingMachineSimpleResponse toGetWashingMachineSimpleResponse(WashingMachine entity) {
			return new GetWashingMachineSimpleResponse(
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

		public static GetWashingMachineFullResponse toGetWashingMachineFullResponse(WashingMachine entity) {
			return new GetWashingMachineFullResponse(
					entity.getCategory(),
					entity.getManufacturer(),
					entity.getIdentificationMode(),
					entity.getModel(),
					entity.getType(),
					entity.getSerialNumber(),
					entity.getReturnType(),
					entity.getDamageType(),
					entity.getRecommendation(),
					entity.getCreatedAt(),

					new WashingMachineDetailMapper().toGetWashingMachineDetailResponse(entity.getWashingMachineDetail()),
					WashingMachineImageMapper.toGetWashingMachineImageResponses(entity.getWashingMachineImages())
			);
		}

		public static WashingMachine toEntity(CreateWashingMachineRequest dto) {
			return new WashingMachine(
					dto.category(),
					dto.manufacturer(),
					dto.damageType(),
					dto.returnType(),
					dto.identificationMode(),
					dto.serialNumber(),
					dto.model(),
					dto.type(),
					null, //TODO: What to do with this?
					new WashingMachineDetailMapper().toEntity(dto.createWashingMachineDetailRequest())
			);
		}
	}

}
