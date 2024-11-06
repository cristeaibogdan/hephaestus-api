package org.personal.solarpanel.service;

public interface SolarPanelRepository {
	void save(SolarPanel solarPanel);

	void findBySerialNumber(String serialNumber);
}
