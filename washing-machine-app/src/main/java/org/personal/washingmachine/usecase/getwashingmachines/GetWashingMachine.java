package org.personal.washingmachine.usecase.getwashingmachines;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineImage;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
class GetWashingMachine {
	private final WashingMachineRepository repository;

	@PostMapping("/v1/washing-machines/many")
	Map<String, GetWashingMachineFullResponse> handle(
			@RequestBody
			@NotEmpty(message = "{LIST_NOT_EMPTY}")
			@Size(max = 10, message = "{LIST_MAX_SIZE}")
			Set<@NotBlank(message = "{FIELD_NOT_BLANK}") String> serialNumbers
	) {
		List<WashingMachine> foundWashingMachines = repository.findAllBySerialNumberIn(serialNumbers);
		if (foundWashingMachines.isEmpty()) {
			throw new CustomException(ErrorCode.SERIAL_NUMBERS_NOT_FOUND, serialNumbers);
		}

		return buildResponseMap(foundWashingMachines, serialNumbers);
	}

	private Map<String, GetWashingMachineFullResponse> buildResponseMap(List<WashingMachine> foundWashingMachines, Set<String> serialNumbers) {
		Map<String, GetWashingMachineFullResponse> result = foundWashingMachines.stream()
				.map(wm -> toGetWashingMachineFullResponse(wm))
				.collect(Collectors.toMap(
						wm -> wm.serialNumber(),
						wm -> wm
				));

		serialNumbers.forEach(sn -> result.putIfAbsent(sn, null));
		return result;
	}

	private GetWashingMachineFullResponse toGetWashingMachineFullResponse(WashingMachine entity) {
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

				new GetWashingMachineFullResponse.Damage(
						entity.getWashingMachineDamage().getPackageDamage().isApplicable(),
						entity.getWashingMachineDamage().getPackageDamage().isPackageDamaged(),
						entity.getWashingMachineDamage().getPackageDamage().isPackageDirty(),
						entity.getWashingMachineDamage().getPackageDamage().isPackageMaterialAvailable(),

						entity.getWashingMachineDamage().getVisibleSurfaceDamage().isApplicable(),
						entity.getWashingMachineDamage().getVisibleSurfaceDamage().hasScratches(),
						entity.getWashingMachineDamage().getVisibleSurfaceDamage().getVisibleSurfacesScratchesLength(),
						entity.getWashingMachineDamage().getVisibleSurfaceDamage().hasDents(),
						entity.getWashingMachineDamage().getVisibleSurfaceDamage().getVisibleSurfacesDentsDepth(),
						entity.getWashingMachineDamage().getVisibleSurfaceDamage().hasMinorDamage(),
						entity.getWashingMachineDamage().getVisibleSurfaceDamage().getVisibleSurfacesMinorDamage(),
						entity.getWashingMachineDamage().getVisibleSurfaceDamage().hasMajorDamage(),
						entity.getWashingMachineDamage().getVisibleSurfaceDamage().getVisibleSurfacesMajorDamage(),

						entity.getWashingMachineDamage().getHiddenSurfaceDamage().isApplicable(),
						entity.getWashingMachineDamage().getHiddenSurfaceDamage().hasScratches(),
						entity.getWashingMachineDamage().getHiddenSurfaceDamage().getHiddenSurfacesScratchesLength(),
						entity.getWashingMachineDamage().getHiddenSurfaceDamage().hasDents(),
						entity.getWashingMachineDamage().getHiddenSurfaceDamage().getHiddenSurfacesDentsDepth(),
						entity.getWashingMachineDamage().getHiddenSurfaceDamage().hasMinorDamage(),
						entity.getWashingMachineDamage().getHiddenSurfaceDamage().getHiddenSurfacesMinorDamage(),
						entity.getWashingMachineDamage().getHiddenSurfaceDamage().hasMajorDamage(),
						entity.getWashingMachineDamage().getHiddenSurfaceDamage().getHiddenSurfacesMajorDamage(),

						entity.getWashingMachineDamage().getCostAssessment().getPrice(),
						entity.getWashingMachineDamage().getCostAssessment().getRepairPrice()
				),
				toGetWashingMachineImageResponses(entity.getWashingMachineImages())
		);
	}

	private List<GetWashingMachineFullResponse.Image> toGetWashingMachineImageResponses(List<WashingMachineImage> entities) {
		return entities.stream()
				.map(entity -> new GetWashingMachineFullResponse.Image(
						entity.getImagePrefix(),
						entity.getImage()))
				.toList();
	}
}
