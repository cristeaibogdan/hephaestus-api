package org.personal.solarpanel.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.personal.solarpanel.dto.GetSolarPanelFullResponse;
import org.personal.solarpanel.dto.SaveSolarPanelRequest;
import org.personal.solarpanel.dto.SearchSolarPanelRequest;
import org.personal.solarpanel.dto.SearchSolarPanelResponse;
import org.personal.solarpanel.enums.Recommendation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RequestMapping("/v1/solar-panels")
public interface ISolarPanelApplicationService {
	@PostMapping("/save")
	@ResponseStatus(HttpStatus.CREATED)
	void save(@RequestBody SaveSolarPanelRequest request);

	@GetMapping("/{serialNumber}/recommendation")
	Recommendation getRecommendation(@PathVariable String serialNumber);

	@PostMapping
	Page<SearchSolarPanelResponse> search(@Valid @RequestBody SearchSolarPanelRequest request);

	@PostMapping("/many")
	Map<String, GetSolarPanelFullResponse> loadMany(
			@RequestBody
			@NotEmpty(message = "{LIST_NOT_EMPTY}")
			Set<@NotBlank(message = "{FIELD_NOT_BLANK}") String> serialNumbers
	);
}
