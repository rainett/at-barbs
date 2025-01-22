package com.rainett.atbarbsbackend.model.users;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "admins")
@Getter
@Setter
public class Admin extends User {
    public Admin() {
        setRole("ROLE_ADMIN");
    }
}
