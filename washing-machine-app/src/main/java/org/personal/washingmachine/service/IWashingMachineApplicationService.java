package org.personal.washingmachine.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.personal.washingmachine.dto.*;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.dto.GetWashingMachineReportResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RequestMapping("/api/v1/washing-machines")
public interface IWashingMachineApplicationService {
	@PostMapping
	Page<SearchWashingMachineResponse> loadPaginatedAndFiltered(@Valid @RequestBody SearchWashingMachineRequest searchWashingMachineRequest);

	@GetMapping("/{serialNumber}")
	GetWashingMachineFullResponse load(@PathVariable String serialNumber);

	@PostMapping("/many")
	Map<String, GetWashingMachineFullResponse> loadMany(
			@RequestBody
			@NotEmpty(message = "{LIST_NOT_EMPTY}")
			Set<@NotBlank(message = "{FIELD_NOT_BLANK}") String> serialNumbers
	);

	@PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	void save(@Valid @RequestPart CreateWashingMachineRequest createWashingMachineRequest,
			  @RequestPart List<MultipartFile> imageFiles);

	@GetMapping("/{serialNumber}/recommendation")
	Recommendation getRecommendation(@PathVariable String serialNumber);

	@GetMapping(value = "/{serialNumber}/report")
	GetWashingMachineReportResponse getReport(@PathVariable String serialNumber);

//	@GetMapping("/{category}/manufacturers")
//	List<String> getManufacturers(@PathVariable String category);

	@GetMapping("/{serialNumber}/validate")
	boolean isSerialNumberInUse(@PathVariable String serialNumber);
}
