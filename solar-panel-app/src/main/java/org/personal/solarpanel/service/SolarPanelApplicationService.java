package org.personal.solarpanel.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.solarpanel.dto.SaveSolarPanelRequest;
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
import java.util.Optional;

import static org.personal.solarpanel.entity.QSolarPanel.solarPanel;

@Service
@RestController
@RequiredArgsConstructor
public class SolarPanelApplicationService implements ISolarPanelApplicationService {

	private final SolarPanelRepository repository;
	private final SolarPanelMapper mapper;

	@Override
	public void save(SaveSolarPanelRequest request) {
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
	public Page<SearchSolarPanelResponse> loadPaginated(SearchSolarPanelRequest request) {
		PageRequest pageRequest = PageRequest.of(
				request.pageIndex(),
				request.pageSize(),
				Sort.by(solarPanel.createdAt.getMetadata().getName()).descending()
		);

		BooleanBuilder booleanBuilder = new BooleanBuilder()
				.and(QueryDSLUtils.addStringLikeCondition(solarPanel.manufacturer, request.manufacturer()));
//				.and(QueryDSLUtils.addStringLikeCondition(solarPanel.model, request.model()))
//				.and(QueryDSLUtils.addStringLikeCondition(solarPanel.type, request.type()))
//				.and(QueryDSLUtils.addStringLikeCondition(solarPanel.serialNumber, request.serialNumber()))
//
//				.and(QueryDSLUtils.addEnumEqualCondition(solarPanel.recommendation, request.recommendation()))
//
//				.and(QueryDSLUtils.addTimestampEqualCondition(solarPanel.createdAt, parseToLocalDate(request.createdAt())));

		Page<SolarPanel> responsePage = repository.findAll(booleanBuilder, pageRequest);


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
}
