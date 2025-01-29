package org.personal.washingmachine.repository;

import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.enums.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface WashingMachineRepository extends JpaRepository<WashingMachine, Long>,
        QuerydslPredicateExecutor<WashingMachine> {

    boolean existsBySerialNumber(String serialNumber);
    Optional<WashingMachine> findBySerialNumber(String serialNumber);

    @Query("""
            SELECT wm.recommendation
            FROM WashingMachine wm
            WHERE wm.serialNumber = ?1
            """)
    Optional<Recommendation> getRecommendation(String serialNumber);

	List<WashingMachine> findAllBySerialNumberIn(Set<String> serialNumbers);
}
