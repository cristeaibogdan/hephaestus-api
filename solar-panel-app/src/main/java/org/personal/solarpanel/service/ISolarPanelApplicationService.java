package org.personal.solarpanel.service;

import org.personal.solarpanel.dto.SaveSolarPanelRequest;
import org.personal.solarpanel.enums.Recommendation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/solar-panels")
public interface ISolarPanelApplicationService {
	@PostMapping("/save")
	@ResponseStatus(HttpStatus.CREATED)
	void save(@RequestBody SaveSolarPanelRequest request);

	@GetMapping("/{serialNumber}/recommendation")
	Recommendation getRecommendation(@PathVariable String serialNumber);
}
