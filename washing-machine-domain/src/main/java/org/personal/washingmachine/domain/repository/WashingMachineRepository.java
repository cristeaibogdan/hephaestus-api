package org.personal.washingmachine.domain.repository;

import org.personal.washingmachine.domain.entity.WashingMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WashingMachineRepository extends JpaRepository<WashingMachine, Long>,
        QuerydslPredicateExecutor<WashingMachine> {

    boolean existsBySerialNumber(String serialNumber);
    Optional<WashingMachine> findBySerialNumber(String serialNumber);
}