package com.example.issproject.parser;

import com.example.issproject.entity.AsteroidEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Comments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AsteroidParser {

    @Autowired
    private final ObjectMapper objectMapper;

    public List<AsteroidEntity> responseParser(String response, String startDate, String endDate) throws JsonProcessingException {

        JsonNode jsonNode = objectMapper.readTree(response);

        int n = Integer.parseInt(jsonNode.path("element_count").asText());

        List<AsteroidEntity> asteroids = new ArrayList<>(n);

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        while(!start.isAfter(end)){

            JsonNode asteroidIds = jsonNode.path("near_earth_objects").path(start.toString());

            for(JsonNode asteroid : asteroidIds){

                AsteroidEntity asteroidRequest = new AsteroidEntity();

                Integer id = asteroid.path("id").asInt();

                String asteroidName = asteroid
                        .path("name").asText();

                String absoluteMagnitudeH = asteroid
                        .path("absolute_magnitude_h").asText();

                String estimatedDiameter = asteroid
                        .path("estimated_diameter")
                        .path("kilometers")
                        .path("estimated_diameter_min")
                        .asText().substring(0, 5);

                String hazardous = asteroid
                        .path("is_potentially_hazardous_asteroid").asText();

                String closeApproachDate = asteroid
                        .path("close_approach_data")
                        .path(0)
                        .path("close_approach_date_full").asText();

                String speed = asteroid
                        .path("close_approach_data")
                        .path(0)
                        .path("relative_velocity")
                        .path("kilometers_per_second")
                        .asText().substring(0, 5);

                String astroDistance = asteroid
                        .path("close_approach_data")
                        .path(0)
                        .path("miss_distance")
                        .path("astronomical").asText().substring(0, 5);

                String orbitingBody = asteroid
                        .path("close_approach_data")
                        .path(0)
                        .path("orbiting_body").asText();

                String isSentry = asteroid
                        .path("is_sentry_object").asText();


                asteroidRequest.setId(id);
                asteroidRequest.setAsteroidName(asteroidName);
                asteroidRequest.setAbsoluteMagnitudeH(absoluteMagnitudeH);
                asteroidRequest.setEstimatedDiameterKm(estimatedDiameter);
                asteroidRequest.setHazardous(hazardous);
                asteroidRequest.setCloseApproachDate(closeApproachDate);
                asteroidRequest.setKilometersPerSecond(speed);
                asteroidRequest.setAstronomicalDistance(astroDistance);
                asteroidRequest.setOrbitingBody(orbitingBody);
                asteroidRequest.setIsSentryObject(isSentry);
                asteroidRequest.setDateCreated(LocalDateTime.now());


                asteroids.add(asteroidRequest);

            }
            log.info("Data were parsed for date " + start);
            start = start.plusDays(1);

        }

        return asteroids;
    }
}
