package com.rainett.atbarbsbackend.model;

import com.rainett.atbarbsbackend.model.enums.AppointmentStatus;
import com.rainett.atbarbsbackend.model.users.Customer;
import com.rainett.atbarbsbackend.model.users.Staff;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer; // The customer booking the appointment

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff; // The staff member handling the appointment

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service; // The service being booked

    @Column(nullable = false)
    private LocalDateTime appointmentTime; // Scheduled time for the appointment

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.BOOKED;

}
