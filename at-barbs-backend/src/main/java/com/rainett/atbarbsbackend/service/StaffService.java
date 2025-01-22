package com.rainett.atbarbsbackend.service;

import com.rainett.atbarbsbackend.dto.StaffDto;
import com.rainett.atbarbsbackend.exception.AppException;
import com.rainett.atbarbsbackend.mapper.StaffMapper;
import com.rainett.atbarbsbackend.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final StaffRepository staffRepository;
    private final StaffMapper staffMapper;

    public Page<StaffDto> getStaffForService(Long serviceId, Pageable pageable) {
        return staffRepository.findStaffByServiceId(serviceId, pageable);
    }

    public StaffDto getStaffById(Long id) {
        return staffRepository.findById(id)
                .map(staffMapper::toDto)
                .orElseThrow(() -> new AppException("Staff not found", HttpStatus.NOT_FOUND));
    }
}
