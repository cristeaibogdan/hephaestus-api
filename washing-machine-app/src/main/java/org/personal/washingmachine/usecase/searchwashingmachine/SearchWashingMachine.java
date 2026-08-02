package org.personal.washingmachine.usecase.searchwashingmachine;

import com.querydsl.core.BooleanBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static org.personal.washingmachine.entity.QWashingMachine.washingMachine;

@RestController
@RequiredArgsConstructor
class SearchWashingMachine {
	private final WashingMachineRepository repository;

	@PostMapping("/v1/washing-machines/search")
	Page<SearchWashingMachineResponse> handle(@Valid @RequestBody SearchWashingMachineRequest request) {

		PageRequest pageRequest = PageRequest.of(
				request.pageIndex(),
				request.pageSize(),
				buildSort(request.sortByField(), request.sortDirection())
		);

		BooleanBuilder searchPredicate = buildSearchPredicate(request);

		Page<WashingMachine> responsePage = repository.findAll(searchPredicate, pageRequest);

		return responsePage.map(wm -> toSearchWashingMachineResponse(wm));
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
			throw new CustomException("Invalid date provided", e, ErrorCode.INVALID_DATE);
		}
	}

	private Sort buildSort(String sortByField, String sortDirection) {

		if (StringUtils.isBlank(sortByField) || StringUtils.isBlank(sortDirection)) {
			return buildDefaultSortByCreatedAtDesc();
		}

		Sort.Direction direction = Sort.Direction.fromString(sortDirection);

		return switch (sortByField) {
			case "createdAt" -> Sort.by(direction, washingMachine.createdAt.getMetadata().getName());
			case "identificationMode" -> Sort.by(direction, washingMachine.identificationMode.getMetadata().getName());
			case "manufacturer" -> Sort.by(direction, washingMachine.manufacturer.getMetadata().getName());
			case "model" -> Sort.by(direction, washingMachine.model.getMetadata().getName());
			case "type" -> Sort.by(direction, washingMachine.type.getMetadata().getName());
			case "serialNumber" -> Sort.by(direction, washingMachine.serialNumber.getMetadata().getName());
			case "returnType" -> Sort.by(direction, washingMachine.returnType.getMetadata().getName());
			case "damageType" -> Sort.by(direction, washingMachine.damageType.getMetadata().getName());
			case "recommendation" -> Sort.by(direction, washingMachine.recommendation.getMetadata().getName());

			default -> buildDefaultSortByCreatedAtDesc();
		};
	}

	private Sort buildDefaultSortByCreatedAtDesc() {
		return Sort.by(Sort.Direction.DESC, washingMachine.createdAt.getMetadata().getName());
	}

	private SearchWashingMachineResponse toSearchWashingMachineResponse(WashingMachine entity) {
		return new SearchWashingMachineResponse(
				entity.getCategory(),
				entity.getManufacturer(),
				entity.getIdentificationMode(),
				entity.getModel(),
				entity.getType(),
				entity.getSerialNumber(),
				entity.getReturnType(),
				entity.getDamageType(),
				entity.getRecommendation(),
				entity.getCreatedAt()
		);
	}
}
