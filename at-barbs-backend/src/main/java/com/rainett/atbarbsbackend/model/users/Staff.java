package com.rainett.atbarbsbackend.model.users;

import com.rainett.atbarbsbackend.model.Service;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "staffs")
@Getter
@Setter
public class Staff extends User {
    public Staff() {
        setRole("ROLE_STAFF");
    }

    @Column(nullable = false)
    private String specialization;

    @ManyToMany
    @JoinTable(
            name = "staff_services",
            joinColumns = @JoinColumn(name = "staff_id", foreignKey = @ForeignKey(name = "staff_services_staff_id_fk")),
            inverseJoinColumns = @JoinColumn(name = "service_id", foreignKey = @ForeignKey(name = "staff_services_service_id_fk"))
    )
    private List<Service> services = new ArrayList<>();

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;
}
