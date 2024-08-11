package org.personal.washingmachine.controller;

import lombok.RequiredArgsConstructor;
import org.personal.shared.clients.ProductClient;
import org.personal.washingmachine.facade.WashingMachineFacade;
import org.personal.washingmachine.facade.dtos.*;
import org.personal.washingmachine.service.WashingMachineDamageCalculator;
import org.personal.washingmachine.service.WashingMachineReportGenerator;
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

	private final WashingMachineFacade washingMachineFacade;

	private final WashingMachineService washingMachineService;
	private final WashingMachineDamageCalculator washingMachineDamageCalculator;
	private final WashingMachineReportGenerator washingMachineReportGenerator;

	@PostMapping
	Page<WashingMachineSimpleDTO> loadPaginatedAndFiltered(@RequestBody PageRequestDTO pageRequestDTO) {
		return washingMachineFacade.loadPaginatedAndFiltered(pageRequestDTO);
	}

	@GetMapping("/{serialNumber}/expanded")
	WashingMachineExpandedDTO loadExpanded(@PathVariable String serialNumber) {
		return washingMachineFacade.loadExpanded(serialNumber);
	}

	@PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	void save(@RequestPart WashingMachineDTO washingMachineDTO,
			  @RequestPart List<MultipartFile> imageFiles) {
		washingMachineFacade.save(washingMachineDTO, imageFiles);
	}

	@PostMapping("/evaluate")
	WashingMachineEvaluationDTO getDamageEvaluation(@RequestBody WashingMachineDetailsDTO washingMachineDetailsDTO) {
		return washingMachineDamageCalculator.getDamageEvaluation(washingMachineDetailsDTO);
	}

	@GetMapping(value = "/{serialNumber}/report")
	WashingMachineReportDTO getReport(@PathVariable String serialNumber) {
		return washingMachineReportGenerator.getReport(serialNumber);
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
