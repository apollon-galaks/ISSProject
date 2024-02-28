package com.example.issproject.controller;

import com.example.issproject.entity.AsteroidEntity;
import com.example.issproject.service.asteroidService.AsteroidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/allAsteroids")
    public List<AsteroidEntity> getAllAsteroids(){
        return asteroidService.getAllAsteroids();
    }

    @GetMapping("/asteroid/{id}")
    public AsteroidEntity getAsteroidById(@PathVariable int id){
        return asteroidService.getById(id);
    }
    @GetMapping("/hazardousAsteroid/{hazardous}")
    public List<AsteroidEntity> getAsteroidByHazardous(@PathVariable String hazardous){
        return asteroidService.getByHazardous(hazardous);
    }


}
