package org.personal.solarpanel.service;

import org.personal.solarpanel.dto.SaveSolarPanelRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("/v1/solar-panels")
public interface ISolarPanelApplicationService {
	@PostMapping("/save")
	@ResponseStatus(HttpStatus.CREATED)
	void save(@RequestBody SaveSolarPanelRequest request);
}
