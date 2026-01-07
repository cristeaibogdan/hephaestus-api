package org.personal.washingmachine.service;

import com.querydsl.core.BooleanBuilder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import org.personal.washingmachine.mapper.WashingMachineMapper;
import static org.personal.washingmachine.entity.QWashingMachine.washingMachine;

@Service
@RestController
@RequestMapping("/v1/washing-machines")
@RequiredArgsConstructor
public class WashingMachineApplicationService {
	private final WashingMachineService service;
	private final WashingMachineRepository repository;
	private final WashingMachineReportGenerator reportGenerator;

	private final WashingMachineImageMapper washingMachineImageMapper;
	private final WashingMachineMapper washingMachineMapper;

	@PostMapping("/search")
	public Page<SearchWashingMachineResponse> search(@Valid @RequestBody SearchWashingMachineRequest request) {

		PageRequest pageRequest = PageRequest.of(
				request.pageIndex(),
				request.pageSize(),
				buildSort(request.sortByField(), request.sortDirection())
		);

		BooleanBuilder searchPredicate = buildSearchPredicate(request);

		Page<WashingMachine> responsePage = repository.findAll(searchPredicate, pageRequest);

		return responsePage.map(wm -> washingMachineMapper.toSearchWashingMachineResponse(wm));
	}

	private BooleanBuilder buildSearchPredicate(SearchWashingMachineRequest request) {
		BooleanBuilder predicate = new BooleanBuilder();

		if (request.identificationMode() != null) {
			predicate.and(washingMachine.identificationMode.eq(request.identificationMode()));
		}
		if (StringUtils.isNotBlank(request.manufacturer())) {
			predicate.and(washingMachine.manufacturer.containsIgnoreCase(request.manufacturer()));
		}
		if (StringUtils.isNotBlank(request.model())) {
			predicate.and(washingMachine.model.containsIgnoreCase(request.model()));
		}
		if (StringUtils.isNotBlank(request.type())) {
			predicate.and(washingMachine.type.containsIgnoreCase(request.type()));
		}
		if (StringUtils.isNotBlank(request.serialNumber())) {
			predicate.and(washingMachine.serialNumber.containsIgnoreCase(request.serialNumber()));
		}
		if (request.returnType() != null) {
			predicate.and(washingMachine.returnType.eq(request.returnType()));
		}
		if (request.damageType() != null) {
			predicate.and(washingMachine.damageType.eq(request.damageType()));
		}
		if (request.recommendation() != null) {
			predicate.and(washingMachine.recommendation.eq(request.recommendation()));
		}
		if (StringUtils.isNotBlank(request.createdAt())) {
			LocalDate searchDate = parseToLocalDate(request.createdAt());
			predicate.and(washingMachine.createdAt.year().eq(searchDate.getYear()))
					.and(washingMachine.createdAt.month().eq(searchDate.getMonthValue()))
					.and(washingMachine.createdAt.dayOfMonth().eq(searchDate.getDayOfMonth()));
		}

		return predicate;
	}

	private LocalDate parseToLocalDate(String dateString) {
		try {
			return LocalDate.parse(dateString);
		} catch (DateTimeParseException e) {
			throw new CustomException("Invalid date provided", ErrorCode.INVALID_DATE, e);
		}
	}

	private Sort buildSort(String sortByField, String sortDirection) {

		if (sortByField == null || sortDirection == null || sortDirection.isBlank()) {
			return buildDefaultSortByCreatedAtDesc();
		}

		Sort.Direction direction = Sort.Direction.fromString(sortDirection);

		return switch (sortByField) {
			case "manufacturer" -> Sort.by(direction, washingMachine.manufacturer.getMetadata().getName());
			case "model" -> Sort.by(direction, washingMachine.model.getMetadata().getName());
			case "type" -> Sort.by(direction, washingMachine.type.getMetadata().getName());
			case "serialNumber" -> Sort.by(direction, washingMachine.serialNumber.getMetadata().getName());
			case "recommendation" -> Sort.by(direction, washingMachine.recommendation.getMetadata().getName());
			case "createdAt" -> Sort.by(direction, washingMachine.createdAt.getMetadata().getName());

			default -> buildDefaultSortByCreatedAtDesc();
		};
	}

	private Sort buildDefaultSortByCreatedAtDesc() {
		return Sort.by(Sort.Direction.DESC, washingMachine.createdAt.getMetadata().getName());
	}

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
