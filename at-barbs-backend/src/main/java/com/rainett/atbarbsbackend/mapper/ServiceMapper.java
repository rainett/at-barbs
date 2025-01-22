package com.rainett.atbarbsbackend.mapper;

import com.rainett.atbarbsbackend.dto.ServiceDto;
import com.rainett.atbarbsbackend.model.Service;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper {
    public ServiceDto toDto(Service service) {
        ServiceDto serviceDto = new ServiceDto();
        serviceDto.setId(service.getId());
        serviceDto.setName(service.getName());
        serviceDto.setDescription(service.getDescription());
        serviceDto.setDurationMinutes(service.getDurationMinutes());
        serviceDto.setPrice(service.getPrice());
        return serviceDto;
    }
}
