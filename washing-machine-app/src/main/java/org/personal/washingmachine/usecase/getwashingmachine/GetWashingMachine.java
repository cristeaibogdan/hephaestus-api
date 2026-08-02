package org.personal.washingmachine.usecase.getwashingmachine;

import lombok.RequiredArgsConstructor;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineImage;
import org.personal.washingmachine.service.WashingMachineService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @deprecated
 * <p> <b>In the context of</b> providing an API for retrieving a single {@link GetWashingMachineResponse} by serial number, </p>
 * <p> <b>facing</b> the concern that clients might call this method in a loop to retrieve multiple DTOs, </p>
 * <p> <b>we decided</b> to deprecate this method and introduce {@link org.personal.washingmachine.usecase.batchgetwashingmachine.BatchGetWashingMachine#handle} </p>
 * <p> <b>to achieve</b> improved performance by reducing the number of network requests, </p>
 * <p> <b>accepting</b> that clients need to wrap single serial numbers in a list. </p>
 * <p> Use {@link org.personal.washingmachine.usecase.batchgetwashingmachine.BatchGetWashingMachine#handle} instead.
 */
@Deprecated(since = "2024/11/21")
@RestController
@RequiredArgsConstructor
class GetWashingMachine {
	private final WashingMachineService service;

	@GetMapping("/v1/washing-machines/{serialNumber}")
	GetWashingMachineResponse handle(@PathVariable String serialNumber) {
		WashingMachine washingMachine = service.findBySerialNumber(serialNumber);
		return toGetWashingMachineResponse(washingMachine);
	}

	private GetWashingMachineResponse toGetWashingMachineResponse(WashingMachine entity) {
		return new GetWashingMachineResponse(
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

				new GetWashingMachineResponse.Damage(
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

	private List<GetWashingMachineResponse.Image> toGetWashingMachineImageResponses(List<WashingMachineImage> entities) {
		return entities.stream()
				.map(entity -> new GetWashingMachineResponse.Image(
						entity.getImagePrefix(),
						entity.getImage()))
				.toList();
	}
}
