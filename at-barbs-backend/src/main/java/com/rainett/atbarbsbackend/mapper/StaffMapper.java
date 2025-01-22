package com.rainett.atbarbsbackend.mapper;

import com.rainett.atbarbsbackend.dto.StaffDto;
import com.rainett.atbarbsbackend.model.users.Staff;
import org.springframework.stereotype.Component;

@Component
public class StaffMapper {
    public StaffDto toDto(Staff staff) {
        StaffDto staffDto = new StaffDto();
        staffDto.setId(staff.getId());
        staffDto.setFirstName(staff.getFirstName());
        staffDto.setLastName(staff.getLastName());
        staffDto.setEmail(staff.getEmail());
        staffDto.setRole(staff.getRole());
        staffDto.setCreatedAt(staff.getCreatedAt());
        staffDto.setUpdatedAt(staff.getUpdatedAt());
        staffDto.setSpecialization(staff.getSpecialization());
        staffDto.setStartTime(staff.getStartTime());
        staffDto.setEndTime(staff.getEndTime());
        return staffDto;
    }
}
