package org.personal.solarpanel.repository;

import org.personal.solarpanel.entity.SolarPanel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolarPanelRepository extends JpaRepository<SolarPanel, Long> {
	Optional<SolarPanel> findBySerialNumber(String serialNumber);
}
