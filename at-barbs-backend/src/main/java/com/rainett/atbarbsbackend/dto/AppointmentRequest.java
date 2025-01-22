package com.rainett.atbarbsbackend.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Data
public class AppointmentRequest {
    private Long serviceId;
    private Long staffId;
    private LocalDate date;
    private LocalTime timeSlot;
}
