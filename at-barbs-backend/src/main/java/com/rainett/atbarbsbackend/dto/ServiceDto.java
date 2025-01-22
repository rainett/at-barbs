package com.rainett.atbarbsbackend.dto;

import lombok.Data;

@Data
public class ServiceDto {
    private Long id;
    private String name;
    private String description;
    private int durationMinutes;
    private double price;
}
