package com.rainett.atbarbsbackend.dto;

import com.rainett.atbarbsbackend.model.enums.AppointmentStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {
    private Long id;
    private Long customerId;
    private String staffFirstName;
    private String staffLastName;
    private String serviceName;
    private LocalDateTime appointmentTime;
    private AppointmentStatus status;
}
