package org.personal.washingmachine.mapper;

import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.dto.GetWashingMachineImageResponse;
import org.personal.washingmachine.entity.WashingMachineImage;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public final class WashingMachineImageMapper {

	public List<GetWashingMachineImageResponse> toGetWashingMachineImageResponses(List<WashingMachineImage> entities) {
		return entities.stream()
				.map(entity -> new GetWashingMachineImageResponse(
						entity.getImagePrefix(),
						entity.getImage()))
				.toList();
	}

	public WashingMachineImage toEntity(MultipartFile imageFile) {

		byte[] image;

		try {
			image = imageFile.getBytes();
		} catch (IOException e) {
			throw new CustomException("Could not extract bytes from image: " + imageFile.getName(), e, ErrorCode.GENERAL);
		}

		String imagePrefix = "data:image/" + getImageExtension(imageFile) + ";base64,";

		return new WashingMachineImage(imagePrefix, image);
	}

	private String getImageExtension(MultipartFile imageFile) {
		String extension = StringUtils.getFilenameExtension(imageFile.getOriginalFilename());

		return switch (extension.toLowerCase()) {
			case "png" -> "png";
			case "jpg" -> "jpg";
			case "jpeg" -> "jpeg";
			case "bmp" -> "bmp";
			default -> throw new CustomException("Invalid image extension: " + extension, ErrorCode.GENERAL);
		};
	}
}
