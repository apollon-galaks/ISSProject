package com.example.issproject.service;


import com.example.issproject.entity.AsteroidEntity;
import com.example.issproject.repository.AsteroidRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Map;

public interface AsteroidService {
    String getHazardousAsteroids(String startDate, String endDate);

    List<AsteroidEntity> getAllAsteroids();
    AsteroidEntity getById(int id);
    AsteroidEntity getByHazardous(String hazardous);

    //List<Map<String, Object>> saveAll(List<Map<String, Object>> asteroids);
}
