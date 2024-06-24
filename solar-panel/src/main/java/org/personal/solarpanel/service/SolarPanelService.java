package org.personal.solarpanel.service;

import lombok.RequiredArgsConstructor;
import org.personal.solarpanel.entity.SolarPanel;
import org.personal.solarpanel.repository.SolarPanelRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class SolarPanelService {

    private final SolarPanelRepository solarPanelRepository;

    @Transactional
    public void saveSolarPanel(SolarPanel solarPanel) {

        boolean existingSerialNumber = solarPanelRepository.existsBySerialNumber(solarPanel.getSerialNumber());
        if (existingSerialNumber) {
            throw new RuntimeException("Serial number is taken");
        }

        solarPanelRepository.save(solarPanel);
    }
}
