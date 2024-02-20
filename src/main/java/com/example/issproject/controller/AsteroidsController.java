package com.example.issproject.controller;

import com.example.issproject.service.AsteroidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/asteroids")
public class AsteroidsController {

    private final AsteroidService asteroidService;

    @Autowired
    public AsteroidsController(AsteroidService asteroidService) {
        this.asteroidService = asteroidService;
    }



    @GetMapping()
    public String getHazardousAsteroids(@RequestParam String startDate, @RequestParam String endDate){


        return asteroidService.getHazardousAsteroids(startDate, endDate);

    }


}
