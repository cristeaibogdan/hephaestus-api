package org.personal.solarpanel.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.solarpanel.dto.GetSolarPanelFullResponse;
import org.personal.solarpanel.dto.CreateSolarPanelRequest;
import org.personal.solarpanel.dto.SearchSolarPanelRequest;
import org.personal.solarpanel.dto.SearchSolarPanelResponse;
import org.personal.solarpanel.entity.SolarPanel;
import org.personal.solarpanel.enums.Recommendation;
import org.personal.solarpanel.mapper.SolarPanelMapper;
import org.personal.solarpanel.repository.SolarPanelRepository;
import org.personal.solarpanel.service.utils.QueryDSLUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.personal.solarpanel.entity.QSolarPanel.solarPanel;

@Service
@RestController
@RequiredArgsConstructor
public class SolarPanelApplicationService implements ISolarPanelApplicationService {

	private final SolarPanelRepository repository;
	private final SolarPanelMapper mapper;

	@Override
	public void create(CreateSolarPanelRequest request) {
		SolarPanel solarPanel = mapper.toEntity(request);

		boolean existingSerialNumber = repository.existsBySerialNumber(solarPanel.getSerialNumber());
		if (existingSerialNumber) {
			throw new CustomException(ErrorCode.SERIAL_NUMBER_ALREADY_TAKEN);
		}

		repository.save(solarPanel);
	}

	@Override
	public Recommendation getRecommendation(String serialNumber) {
		return repository.getRecommendation(serialNumber)
				.orElseThrow(() -> new CustomException(ErrorCode.SERIAL_NUMBER_NOT_FOUND, serialNumber));
	}

	@Override
	public Page<SearchSolarPanelResponse> search(SearchSolarPanelRequest request) {

		PageRequest pageRequest = PageRequest.of(
				request.pageIndex(),
				request.pageSize(),
				buildSort(request.sortByField(), request.sortDirection())
		);

		BooleanBuilder searchFilters = new BooleanBuilder()
				.and(QueryDSLUtils.addStringLikeCondition(solarPanel.manufacturer, request.manufacturer()))
				.and(QueryDSLUtils.addStringLikeCondition(solarPanel.model, request.model()))
				.and(QueryDSLUtils.addStringLikeCondition(solarPanel.type, request.type()))
				.and(QueryDSLUtils.addStringLikeCondition(solarPanel.serialNumber, request.serialNumber()))
				.and(QueryDSLUtils.addEnumEqualCondition(solarPanel.recommendation, request.recommendation()))
				.and(QueryDSLUtils.addTimestampEqualCondition(solarPanel.createdAt, parseToLocalDate(request.createdAt())));

		Page<SolarPanel> responsePage = repository.findAll(searchFilters, pageRequest);

		return responsePage.map(solarPanel -> mapper.toSearchSolarPanelResponse(solarPanel));
	}

	private Optional<LocalDate> parseToLocalDate(String dateString) { //TODO: Duplicated code.
		try {
			return Optional.ofNullable(dateString)
					.filter(s -> StringUtils.isNotBlank(s))
					.map(s -> LocalDate.parse(s.trim()));
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
			case "manufacturer" -> Sort.by(direction, solarPanel.manufacturer.getMetadata().getName());
			case "model" -> Sort.by(direction, solarPanel.model.getMetadata().getName());
			case "type" -> Sort.by(direction, solarPanel.type.getMetadata().getName());
			case "serialNumber" -> Sort.by(direction, solarPanel.serialNumber.getMetadata().getName());
			case "recommendation" -> Sort.by(direction, solarPanel.recommendation.getMetadata().getName());
			case "createdAt" -> Sort.by(direction, solarPanel.createdAt.getMetadata().getName());

			default -> buildDefaultSortByCreatedAtDesc();
		};
	}

	private Sort buildDefaultSortByCreatedAtDesc() {
		return Sort.by(Sort.Direction.DESC, solarPanel.createdAt.getMetadata().getName());
	}

	@Override
	public Map<String, GetSolarPanelFullResponse> loadMany(Set<String> serialNumbers) {

		List<SolarPanel> solarPanels = repository.findAllBySerialNumberIn(serialNumbers);
		if(solarPanels.isEmpty()) {
			throw new CustomException(ErrorCode.SERIAL_NUMBERS_NOT_FOUND, serialNumbers);
		}

		return buildResponseMap(solarPanels, serialNumbers);
	}

	private Map<String, GetSolarPanelFullResponse> buildResponseMap(List<SolarPanel> solarPanels, Set<String> serialNumbers) {
		Map<String, GetSolarPanelFullResponse> result = solarPanels.stream()
				.map(solarpanel -> mapper.toGetSolarPanelFullResponse(solarpanel))
				.collect(Collectors.toMap(
						getSolarPanelFullResponse -> getSolarPanelFullResponse.serialNumber(),
						getSolarPanelFullResponse -> getSolarPanelFullResponse
				));

		serialNumbers.forEach(serialNumber -> result.putIfAbsent(serialNumber, null));
		return result;
	}
}
