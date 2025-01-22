package com.rainett.atbarbsbackend.repository;

import com.rainett.atbarbsbackend.model.users.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
