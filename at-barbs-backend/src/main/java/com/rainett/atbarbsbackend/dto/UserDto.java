package com.rainett.atbarbsbackend.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String token;
}
