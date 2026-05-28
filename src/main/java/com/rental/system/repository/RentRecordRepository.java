package com.rental.system.repository;

import com.rental.system.model.RentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentRecordRepository extends JpaRepository<RentRecord, Integer> {
}
