package com.example.issproject.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "asteroids")
public class AsteroidEntity {
    @Id
    private int id;
    private String asteroidName;
    private String absoluteMagnitudeH;
    private String estimatedDiameterKm;
    private String hazardous;
    private String closeApproachDate;
    private String kilometersPerSecond;
    private String astronomicalDistance;
    private String orbitingBody;
    private String isSentryObject;
    private LocalDate dateCreated;

}
