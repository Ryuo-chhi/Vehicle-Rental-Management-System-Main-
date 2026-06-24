package com.rental.system.repository;

import com.rental.system.model.RentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.EntityGraph;
import java.util.List;

@Repository
public interface RentRecordRepository extends JpaRepository<RentRecord, Integer> {
    
    @EntityGraph(attributePaths = {"rent", "rent.vehicle", "rent.customer", "rent.staff", "rent.payment"})
    List<RentRecord> findAll();
}
