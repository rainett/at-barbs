package com.rainett.atbarbsbackend.controller;

import com.rainett.atbarbsbackend.dto.StaffDto;
import com.rainett.atbarbsbackend.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
public class StaffController {
    private final StaffService staffService;

    @GetMapping("/by-service")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Page<StaffDto>> getStaffForService(@RequestParam Long serviceId, Pageable pageable) {
        Page<StaffDto> staff = staffService.getStaffForService(serviceId, pageable);
        return ResponseEntity.ok(staff);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<StaffDto> getStaffById(@PathVariable Long id) {
        StaffDto staff = staffService.getStaffById(id);
        return ResponseEntity.ok(staff);
    }
}
