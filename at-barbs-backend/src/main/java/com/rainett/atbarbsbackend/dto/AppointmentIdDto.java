package com.rainett.atbarbsbackend.dto;

import com.rainett.atbarbsbackend.model.enums.AppointmentStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentIdDto {
    private Long id;
    private Long customerId;
    private Long staffId;
    private Long serviceId;
    private LocalDateTime appointmentTime;
    private AppointmentStatus status;
}
