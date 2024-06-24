package org.personal.solarpanel.controller;

import lombok.RequiredArgsConstructor;
import org.personal.solarpanel.service.SolarPanelService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/solar-panels")
@RestController
public class SolarPanelController {

    private final SolarPanelService solarPanelService;
}
