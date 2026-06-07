package com.rental.system.repository;

import com.rental.system.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByCustomerPhone(String phone);
    Optional<Customer> findByCustomerIdNum(String idNum);
    Optional<Customer> findByEmail(String email);
}
