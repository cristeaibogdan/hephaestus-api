package org.personal.washingmachine.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.personal.shared.clients.ProductClient;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.dto.*;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineImage;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.mapper.WashingMachineImageMapper;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.personal.washingmachine.dto.GetWashingMachineReportResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import org.personal.washingmachine.mapper.WashingMachineMapper;

@RestController
@RequestMapping("/v1/washing-machines")
@RequiredArgsConstructor
class WashingMachineApplicationService {
	private final WashingMachineService service;
	private final WashingMachineRepository repository;
	private final WashingMachineReportGenerator reportGenerator;

	private final WashingMachineImageMapper washingMachineImageMapper;
	private final WashingMachineMapper washingMachineMapper;

	/**
	 * @deprecated
	 * <p> <b>In the context of</b> providing an API for retrieving a single {@link org.personal.washingmachine.dto.GetWashingMachineFullResponse} by serial number, </p>
	 * <p> <b>facing</b> the concern that clients might call this method in a loop to retrieve multiple DTOs, </p>
	 * <p> <b>we decided</b> to deprecate this method and introduce {@link org.personal.washingmachine.service.WashingMachineApplicationService#loadMany} </p>
	 * <p> <b>to achieve</b> improved performance by reducing the number of network requests, </p>
	 * <p> <b>accepting</b> that clients need to wrap single serial numbers in a list. </p>
	 * <p> Use {@link org.personal.washingmachine.service.WashingMachineApplicationService#loadMany} instead.
	 */
	@Deprecated(since = "2024/11/21")
	@GetMapping("/{serialNumber}")
	public GetWashingMachineFullResponse load(@PathVariable String serialNumber) {
		WashingMachine washingMachine = service.findBySerialNumber(serialNumber);
		return washingMachineMapper.toGetWashingMachineFullResponse(washingMachine);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void create(@Valid @RequestPart CreateWashingMachineRequest createWashingMachineRequest, @RequestPart List<MultipartFile> imageFiles) {

		WashingMachine washingMachine = washingMachineMapper.toEntity(createWashingMachineRequest);

		imageFiles.forEach(image -> {
			WashingMachineImage washingMachineImage = washingMachineImageMapper.toEntity(image);
			washingMachine.addImage(washingMachineImage);
		});

		service.create(washingMachine);
	}

	@GetMapping("/{serialNumber}/recommendation")
	public Recommendation getRecommendation(@PathVariable String serialNumber) {
		return repository.getRecommendation(serialNumber)
				.orElseThrow(() -> new CustomException(ErrorCode.SERIAL_NUMBER_NOT_FOUND, serialNumber));
	}

	@GetMapping(value = "/{serialNumber}/report")
	public GetWashingMachineReportResponse getReport(@PathVariable String serialNumber) {
		WashingMachine washingMachine = service.findBySerialNumber(serialNumber);
		return reportGenerator.getReport(washingMachine);
	}

	// *****************************************
	// Exception Propagation Test Endpoint
	// *****************************************
	//TODO: To be deleted
	private final ProductClient productClient;

//	@Override
//	public List<String> getManufacturers(String category) {
//		return productClient.getManufacturers(category);
//	}

	@GetMapping("/{serialNumber}/validate")
	public boolean isSerialNumberInUse(@PathVariable String serialNumber) {
		return repository.existsBySerialNumber(serialNumber);
	}

	@PostMapping("/many")
	public Map<String, GetWashingMachineFullResponse> loadMany(
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
				.map(wm -> washingMachineMapper.toGetWashingMachineFullResponse(wm))
				.collect(Collectors.toMap(
						wm -> wm.serialNumber(),
						wm -> wm
				));

		serialNumbers.forEach(sn -> result.putIfAbsent(sn, null));
		return result;
	}
}
