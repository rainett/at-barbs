package com.rainett.atbarbsbackend.repository;

import com.rainett.atbarbsbackend.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}
