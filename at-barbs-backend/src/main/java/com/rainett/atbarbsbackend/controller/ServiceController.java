package com.rainett.atbarbsbackend.controller;

import com.rainett.atbarbsbackend.dto.ServiceDto;
import com.rainett.atbarbsbackend.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceService serviceService;

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<Page<ServiceDto>> getAllServices(Pageable pageable) {
        Page<ServiceDto> services = serviceService.getAllServices(pageable);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ServiceDto> getServiceById(@PathVariable Long id) {
        ServiceDto serviceDto = serviceService.getServiceById(id);
        return ResponseEntity.ok(serviceDto);
    }
}
