package com.rainett.atbarbsbackend.controller;

import com.rainett.atbarbsbackend.dto.AppointmentDto;
import com.rainett.atbarbsbackend.dto.AppointmentIdDto;
import com.rainett.atbarbsbackend.dto.AppointmentRequest;
import com.rainett.atbarbsbackend.dto.TimeSlotDto;
import com.rainett.atbarbsbackend.dto.UserDto;
import com.rainett.atbarbsbackend.service.AppointmentService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping("/available-time-slots")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TimeSlotDto>> getAvailableTimeSlots(@RequestParam Long staffId,
                                                                   @RequestParam LocalDate date) {
        List<TimeSlotDto> timeSlotDtos = appointmentService.getAvailableSlots(staffId, date);
        return ResponseEntity.ok(timeSlotDtos);
    }

    @GetMapping("/customer")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<AppointmentDto>> getCustomerAppointments(
            @AuthenticationPrincipal UserDto userDto,
            Pageable pageable) {
        Page<AppointmentDto> appointments = appointmentService.getCustomerAppointments(
                userDto.getId(), pageable);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasRole('ROLE_ADMIN') or returnObject.body.customerId == authentication.principal.id")
    public ResponseEntity<AppointmentIdDto> getAppointment(@PathVariable Long id) {
        AppointmentIdDto appointments = appointmentService.getAppointment(id);
        return ResponseEntity.ok(appointments);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> createAppointment(@AuthenticationPrincipal UserDto userDto,
                                                  @RequestBody
                                                  AppointmentRequest appointmentRequest) {
        appointmentService.createAppointment(userDto.getId(), appointmentRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/cancel/{id}")
    @PostAuthorize("permitAll()")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.ok().build();
    }
}
