package org.personal.solarpanel.service;

import lombok.RequiredArgsConstructor;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.solarpanel.dto.SaveSolarPanelRequest;
import org.personal.solarpanel.dto.SearchSolarPanelRequest;
import org.personal.solarpanel.dto.SearchSolarPanelResponse;
import org.personal.solarpanel.entity.SolarPanel;
import org.personal.solarpanel.enums.Recommendation;
import org.personal.solarpanel.mapper.SolarPanelMapper;
import org.personal.solarpanel.repository.SolarPanelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

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
				request.pageSize()
		);

		Page<SolarPanel> responsePage = repository.findAll(pageRequest);


		return responsePage.map(solarPanel -> mapper.toSearchSolarPanelResponse(solarPanel));
	}
}
