package com.example.issproject.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "asteroidss")

public class AsteroidEntity {
    @Id
    private Integer id;

    @Column(name = "asteroid_name")
    private String asteroidName;

    @Column(name = "absolute_magnitude_h")
    private String absoluteMagnitudeH;

    @Column(name = "estimated_diameter_km")
    private String estimatedDiameterKm;

    @Column(name = "hazardous")
    private String hazardous;

    @Column(name = "close_approach_date")
    private String closeApproachDate;

    @Column(name = "kilometers_per_second")
    private String kilometersPerSecond;

    @Column(name = "astronomical_distance")
    private String astronomicalDistance;

    @Column(name = "orbiting_body")
    private String orbitingBody;

    @Column(name = "is_sentry_object")
    private String isSentryObject;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

}
