package org.personal.washingmachine.repository;

import org.personal.washingmachine.entity.WashingMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WashingMachineRepository extends JpaRepository<WashingMachine, Long>,
        JpaSpecificationExecutor<WashingMachine> {

    boolean existsBySerialNumber(String serialNumber);
    Optional<WashingMachine> findBySerialNumber(String serialNumber);
}
