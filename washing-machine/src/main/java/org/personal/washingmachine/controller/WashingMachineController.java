package org.personal.washingmachine.controller;

import lombok.RequiredArgsConstructor;
import org.personal.shared.clients.ProductClient;
import org.personal.washingmachine.entity.dtos.*;
import org.personal.washingmachine.service.DamageCalculator;
import org.personal.washingmachine.service.ReportGenerator;
import org.personal.washingmachine.service.WashingMachineService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/washing-machines")
class WashingMachineController {

	private final WashingMachineService washingMachineService;
	private final DamageCalculator damageCalculator;
	private final ReportGenerator reportGenerator;

	@PostMapping
	Page<WashingMachineSimpleDTO> getPaginatedAndFilteredWashingMachines(@RequestBody PageRequestDTO pageRequestDTO) {
		return washingMachineService.getPaginatedAndFilteredWashingMachines(pageRequestDTO);
	}

	@GetMapping("/{serialNumber}/expanded")
	WashingMachineExpandedDTO getWashingMachineExpanded(@PathVariable String serialNumber) {
		return washingMachineService.getWashingMachineExpanded(serialNumber);
	}

	@PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	void saveWashingMachine(
					@RequestPart WashingMachineDTO washingMachineDTO,
					@RequestPart List<MultipartFile> imageFiles) {
		washingMachineService.saveWashingMachine(washingMachineDTO, imageFiles);
	}

	@PostMapping("/evaluate")
	WashingMachineEvaluationDTO generateWashingMachineDamageEvaluation(
					@RequestBody WashingMachineDetailsDTO washingMachineDetailsDTO) {

		return damageCalculator.generateWashingMachineDamageEvaluation(washingMachineDetailsDTO);
	}

	@GetMapping(value = "/{serialNumber}/report")
	WashingMachineReportDTO getWashingMachineReport(@PathVariable String serialNumber) {
		return reportGenerator.getWashingMachineReport(serialNumber);
	}

	// *****************************************
	// Exception Propagation Test Endpoint
	// *****************************************
	//TODO: To be deleted
	private final ProductClient productClient;

	@GetMapping("/{category}/manufacturers")
	List<String> getManufacturers(@PathVariable String category) {
		return productClient.getManufacturers(category);
	}

//*********************************************************************************************
//******************** ENDPOINTS FOR ASYNC VALIDATORS
//*********************************************************************************************

	@GetMapping("/{serialNumber}/validate")
	boolean isSerialNumberInUse(@PathVariable String serialNumber) {
		return washingMachineService.isSerialNumberInUse(serialNumber);
	}

}
