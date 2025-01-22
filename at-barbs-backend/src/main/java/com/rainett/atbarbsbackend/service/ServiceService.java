package com.rainett.atbarbsbackend.service;

import com.rainett.atbarbsbackend.dto.ServiceDto;
import com.rainett.atbarbsbackend.exception.AppException;
import com.rainett.atbarbsbackend.mapper.ServiceMapper;
import com.rainett.atbarbsbackend.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceService {
    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;

    public Page<ServiceDto> getAllServices(Pageable pageable) {
        return serviceRepository.findAll(pageable).map(serviceMapper::toDto);
    }

    public ServiceDto getServiceById(Long id) {
        return serviceRepository.findById(id).map(serviceMapper::toDto)
                .orElseThrow(() -> new AppException("Service not found", HttpStatus.NOT_FOUND));
    }
}
