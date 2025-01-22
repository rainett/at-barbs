package com.rainett.atbarbsbackend.service;

import com.rainett.atbarbsbackend.dto.AppointmentDto;
import com.rainett.atbarbsbackend.dto.AppointmentIdDto;
import com.rainett.atbarbsbackend.dto.AppointmentRequest;
import com.rainett.atbarbsbackend.dto.TimeSlotDto;
import com.rainett.atbarbsbackend.exception.AppException;
import com.rainett.atbarbsbackend.model.Appointment;
import com.rainett.atbarbsbackend.model.enums.AppointmentStatus;
import com.rainett.atbarbsbackend.model.users.Customer;
import com.rainett.atbarbsbackend.model.users.Staff;
import com.rainett.atbarbsbackend.repository.AppointmentRepository;
import com.rainett.atbarbsbackend.repository.CustomerRepository;
import com.rainett.atbarbsbackend.repository.ServiceRepository;
import com.rainett.atbarbsbackend.repository.StaffRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final StaffRepository staffRepository;
    private final ServiceRepository serviceRepository;
    private final CustomerRepository customerRepository;

    public List<TimeSlotDto> getAvailableSlots(Long staffId, LocalDate date) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new AppException("Staff not found", HttpStatus.NOT_FOUND));
        LocalTime startTime = getStartTime(date, staff);
        List<Integer> bookedHours =
                appointmentRepository.findBookedHoursByStaffAndDate(staffId, date,
                                AppointmentStatus.BOOKED).stream()
                        .map(LocalDateTime::getHour)
                        .toList();
        return getTimeSlotDtos(startTime, staff.getEndTime(), bookedHours);
    }

    private static LocalTime getStartTime(LocalDate date, Staff staff) {
        boolean isToday = date.equals(LocalDate.now());
        LocalTime nextHour = LocalTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS);
        return isToday
                ? maxTime(nextHour, staff.getStartTime())
                : staff.getStartTime();
    }

    private static List<TimeSlotDto> getTimeSlotDtos(LocalTime start, LocalTime end,
                                                     List<Integer> bookedHours) {
        List<TimeSlotDto> availableSlots = new ArrayList<>();
        LocalTime current = start;
        while (!current.isAfter(end.minusHours(1))) {
            int hour = current.getHour();
            if (!bookedHours.contains(hour)) {
                TimeSlotDto dto = new TimeSlotDto();
                dto.setTime(current);
                availableSlots.add(dto);
            }
            current = current.plusHours(1);
        }
        return availableSlots;
    }

    private static LocalTime maxTime(LocalTime time1, LocalTime time2) {
        return time1.isAfter(time2) ? time1 : time2;
    }

    public void createAppointment(Long customerId, AppointmentRequest request) {
        com.rainett.atbarbsbackend.model.Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new AppException("Service not found", HttpStatus.NOT_FOUND));
        Staff staff = staffRepository.findById(request.getStaffId())
                .orElseThrow(() -> new AppException("Staff not found", HttpStatus.NOT_FOUND));
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AppException("Customer not found", HttpStatus.NOT_FOUND));
        LocalDateTime appointmentTime = LocalDateTime.of(request.getDate(), request.getTimeSlot());
        validateStaffAvailability(staff, appointmentTime);
        Appointment appointment = new Appointment();
        appointment.setCustomer(customer);
        appointment.setStaff(staff);
        appointment.setService(service);
        appointment.setAppointmentTime(appointmentTime);
        appointment.setStatus(AppointmentStatus.BOOKED);
        appointmentRepository.save(appointment);
    }

    private void validateStaffAvailability(Staff staff, LocalDateTime appointmentTime) {
        List<LocalDateTime> bookedHoursByStaffAndDate =
                appointmentRepository.findBookedHoursByStaffAndDate(staff.getId(),
                        appointmentTime.toLocalDate(), AppointmentStatus.BOOKED);
        if (bookedHoursByStaffAndDate.contains(appointmentTime)) {
            throw new AppException("Staff is not available at this time", HttpStatus.BAD_REQUEST);
        }
    }

    public Page<AppointmentDto> getCustomerAppointments(Long userId, Pageable pageable) {
        return appointmentRepository.findAppointmentsByCustomerId(userId, pageable);
    }

    public AppointmentIdDto getAppointment(Long id) {
        return appointmentRepository.findAppointmentById(id)
                .orElseThrow(() -> new AppException("Appointment not found", HttpStatus.NOT_FOUND));
    }

    public void cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException("Appointment not found", HttpStatus.NOT_FOUND));
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }
}
