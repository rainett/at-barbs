package com.rainett.atbarbsbackend.repository;

import com.rainett.atbarbsbackend.dto.AppointmentDto;
import com.rainett.atbarbsbackend.dto.AppointmentIdDto;
import com.rainett.atbarbsbackend.model.Appointment;
import com.rainett.atbarbsbackend.model.enums.AppointmentStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("""
                SELECT a.appointmentTime
                FROM Appointment a
                WHERE a.staff.id = :staffId
                  AND FUNCTION('DATE', a.appointmentTime) = :date
                  AND a.status = :status
            """)
    List<LocalDateTime> findBookedHoursByStaffAndDate(
            @Param("staffId") Long staffId,
            @Param("date") LocalDate date,
            @Param("status") AppointmentStatus status
    );

    @Query("SELECT new com.rainett.atbarbsbackend.dto.AppointmentDto(" +
           "a.id, " +
           "a.customer.id, " +
           "a.staff.firstName, " +
           "a.staff.lastName, " +
           "a.service.name, " +
           "a.appointmentTime, " +
           "a.status" +
           ") " +
           "FROM Appointment a " +
           "WHERE a.customer.id = :userId")
    Page<AppointmentDto> findAppointmentsByCustomerId(Long userId, Pageable pageable);

    @Query("SELECT new com.rainett.atbarbsbackend.dto.AppointmentIdDto(" +
           "a.id, " +
           "a.customer.id, " +
           "a.staff.id, " +
           "a.service.id, " +
           "a.appointmentTime, " +
           "a.status)" +
           "FROM Appointment a " +
           "WHERE a.id = :id")
    Optional<AppointmentIdDto> findAppointmentById(Long id);
}
