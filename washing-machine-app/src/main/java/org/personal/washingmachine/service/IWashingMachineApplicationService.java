package org.personal.washingmachine.service;

import jakarta.validation.Valid;
import org.personal.washingmachine.dto.*;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.dto.GetWashingMachineReportResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/v1/washing-machines")
public interface IWashingMachineApplicationService {
	@PostMapping
	Page<GetWashingMachineSimpleResponseDTO> loadPaginatedAndFiltered(@Valid @RequestBody SearchWashingMachineRequestDTO searchWashingMachineRequestDTO);

	@GetMapping("/{serialNumber}/expanded")
	GetWashingMachineExpandedResponseDTO loadExpanded(@PathVariable String serialNumber);

	@PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	void save(@RequestPart CreateWashingMachineRequestDTO createWashingMachineRequestDTO,
			  @RequestPart List<MultipartFile> imageFiles);

	@PostMapping("/recommendation")
	Recommendation getRecommendation(@Valid @RequestBody WashingMachineDetailDTO washingMachineDetailDTO);

	@GetMapping(value = "/{serialNumber}/report")
	GetWashingMachineReportResponse getReport(@PathVariable String serialNumber);

	@GetMapping("/{category}/manufacturers")
	List<String> getManufacturers(@PathVariable String category);

	@GetMapping("/{serialNumber}/validate")
	boolean isSerialNumberInUse(@PathVariable String serialNumber);
}
