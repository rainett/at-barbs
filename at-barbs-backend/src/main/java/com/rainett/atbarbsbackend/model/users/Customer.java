package com.rainett.atbarbsbackend.model.users;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customers")
@Getter
@Setter
public class Customer extends User {
    public Customer() {
        setRole("ROLE_CUSTOMER");
    }
}
