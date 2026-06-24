package com.rental.system.repository;

import com.rental.system.model.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;

@Repository
public interface RentRepository extends JpaRepository<Rent, Integer> {
    
    @EntityGraph(attributePaths = {"vehicle", "customer", "staff", "payment"})
    List<Rent> findByStatus(boolean status);

    @EntityGraph(attributePaths = {"vehicle", "customer", "staff", "payment"})
    List<Rent> findAll();
}
