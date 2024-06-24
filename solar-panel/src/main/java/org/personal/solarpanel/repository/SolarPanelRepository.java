package org.personal.solarpanel.repository;

import org.personal.solarpanel.entity.SolarPanel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolarPanelRepository extends JpaRepository<SolarPanel, Long> {
    boolean existsBySerialNumber(String serialNumber);
}
