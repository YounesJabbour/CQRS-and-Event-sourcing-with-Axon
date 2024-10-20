package com.axon_springboot.axon_springboot.query.repository;

import com.axon_springboot.axon_springboot.query.entities.Account;
import com.axon_springboot.axon_springboot.query.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface OperationRepository extends JpaRepository<Operation, Long> {
}
