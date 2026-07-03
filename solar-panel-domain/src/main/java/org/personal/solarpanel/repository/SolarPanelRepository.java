package org.personal.solarpanel.repository;

import org.personal.solarpanel.entity.SolarPanel;
import org.personal.solarpanel.enums.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SolarPanelRepository extends JpaRepository<SolarPanel, Long>, QuerydslPredicateExecutor<SolarPanel> {

	Optional<SolarPanel> findBySerialNumber(String serialNumber);
	boolean existsBySerialNumber(String serialNumber);
	List<SolarPanel> findAllBySerialNumberIn(Set<String> serialNumbers);

	@Query("""
		SELECT sp.recommendation
		FROM SolarPanel sp
		WHERE sp.serialNumber = ?1
		""")
	Optional<Recommendation> getRecommendation(String serialNumber);

	/**
	 * Derived delete/update queries aren't auto-transactional like save()/deleteById() —
	 * without this, Hibernate has no EntityManager transaction to run the remove() in.
	 */
	@Transactional
	void deleteBySerialNumber(String serialNumber);
}
