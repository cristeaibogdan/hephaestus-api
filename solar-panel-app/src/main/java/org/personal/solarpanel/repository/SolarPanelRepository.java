package org.personal.solarpanel.repository;

import org.personal.solarpanel.dto.SolarPanelDTO;

public interface SolarPanelRepository {
	void save(String solarPanel, String red, int i);

	SolarPanelDTO findById(Long id);
}
