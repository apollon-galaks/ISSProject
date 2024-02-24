package com.example.issproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AsteroidEntityRequest {
    @NotNull(message = "Asteroid ID cannot be blank")
    private Integer id;
    @NotBlank(message = "Asteroid asteroidName cannot be blank")
    private String asteroidName;
    @NotBlank(message = "Asteroid absoluteMagnitudeH cannot be blank")
    private String absoluteMagnitudeH;
    @NotBlank(message = "Asteroid estimatedDiameterKm cannot be blank")
    private String estimatedDiameterKm;
    @NotBlank(message = "Asteroid hazardous cannot be blank")
    private String hazardous;
    @NotBlank(message = "Asteroid closeApproachDate cannot be blank")
    private String closeApproachDate;
    @NotBlank(message = "Asteroid kilometersPerSecond cannot be blank")
    private String kilometersPerSecond;
    @NotBlank(message = "Asteroid astronomicalDistance cannot be blank")
    private String astronomicalDistance;
    @NotBlank(message = "Asteroid orbitingBody cannot be blank")
    private String orbitingBody;
    @NotBlank(message = "Asteroid isSentryObject cannot be blank")
    private String isSentryObject;
    private LocalDateTime dateCreated;
}
