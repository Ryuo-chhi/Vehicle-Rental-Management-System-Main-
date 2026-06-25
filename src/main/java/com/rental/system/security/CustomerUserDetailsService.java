package com.rental.system.security;

import com.rental.system.model.Customer;
import com.rental.system.repository.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("null")
public class CustomerUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public CustomerUserDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmailOrCustomerPhone(identifier, identifier)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found with email or phone: " + identifier));
        return new CustomerPrincipal(customer);
    }
}
