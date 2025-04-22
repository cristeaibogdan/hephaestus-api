package org.personal.solarpanel.service;

import jakarta.validation.Valid;
import org.personal.solarpanel.dto.SaveSolarPanelRequest;
import org.personal.solarpanel.dto.SearchSolarPanelRequest;
import org.personal.solarpanel.dto.SearchSolarPanelResponse;
import org.personal.solarpanel.enums.Recommendation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/solar-panels")
public interface ISolarPanelApplicationService {
	@PostMapping("/save")
	@ResponseStatus(HttpStatus.CREATED)
	void save(@RequestBody SaveSolarPanelRequest request);

	@GetMapping("/{serialNumber}/recommendation")
	Recommendation getRecommendation(@PathVariable String serialNumber);

	@PostMapping
	Page<SearchSolarPanelResponse> search(@Valid @RequestBody SearchSolarPanelRequest request);
}
