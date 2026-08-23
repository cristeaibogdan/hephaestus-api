package org.personal.washingmachine.usecase.createwashingmachine;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineDamage;
import org.personal.washingmachine.entity.WashingMachineImage;
import org.personal.washingmachine.entity.embedded.CostAssessment;
import org.personal.washingmachine.entity.embedded.HiddenSurfaceDamage;
import org.personal.washingmachine.entity.embedded.PackageDamage;
import org.personal.washingmachine.entity.embedded.VisibleSurfaceDamage;
import org.personal.washingmachine.service.WashingMachineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
class CreateWashingMachine {
	private final WashingMachineService service;

	static final String PATH = "/v1/washing-machines/create";

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = PATH, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	void handle(@Valid @RequestPart CreateWashingMachineRequest createWashingMachineRequest, @RequestPart List<MultipartFile> imageFiles) {

		WashingMachine washingMachine = toWashingMachineEntity(createWashingMachineRequest);

		imageFiles.forEach(image -> {
			WashingMachineImage washingMachineImage = toWashingMachineImageEntity(image);
			washingMachine.addImage(washingMachineImage);
		});

		service.create(washingMachine);
	}

	private WashingMachine toWashingMachineEntity(CreateWashingMachineRequest request) {
		return new WashingMachine(
				request.category(),
				request.manufacturer(),
				request.damageType(),
				request.returnType(),
				request.identificationMode(),
				request.serialNumber(),
				request.model(),
				request.type(),
				new WashingMachineDamage(
						new PackageDamage(
								request.damage().packageDamaged(),
								request.damage().packageDirty(),
								request.damage().packageMaterialAvailable()
						),
						new VisibleSurfaceDamage(
								request.damage().visibleSurfacesScratchesLength(),
								request.damage().visibleSurfacesDentsDepth(),
								request.damage().visibleSurfacesMinorDamage(),
								request.damage().visibleSurfacesMajorDamage()
						),
						new HiddenSurfaceDamage(
								request.damage().hiddenSurfacesScratchesLength(),
								request.damage().hiddenSurfacesDentsDepth(),
								request.damage().hiddenSurfacesMinorDamage(),
								request.damage().hiddenSurfacesMajorDamage()
						),
						new CostAssessment(
								request.damage().price(),
								request.damage().repairPrice()
						)
				)
		);
	}

	private WashingMachineImage toWashingMachineImageEntity(MultipartFile imageFile) {
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
