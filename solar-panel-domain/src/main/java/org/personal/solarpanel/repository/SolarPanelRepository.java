package org.personal.solarpanel.repository;

import org.personal.solarpanel.entity.SolarPanel;
import org.personal.solarpanel.enums.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolarPanelRepository extends JpaRepository<SolarPanel, Long>,
		QuerydslPredicateExecutor<SolarPanel> {
	Optional<SolarPanel> findBySerialNumber(String serialNumber);
	boolean existsBySerialNumber(String serialNumber);

	@Query("""
		SELECT sp.recommendation
		FROM SolarPanel sp
		WHERE sp.serialNumber = ?1
		""")
	Optional<Recommendation> getRecommendation(String serialNumber);
}
