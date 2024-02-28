package com.example.issproject.repository;

import com.example.issproject.entity.AsteroidEntity;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@NonNullApi
public interface AsteroidRepository extends JpaRepository<AsteroidEntity, Integer> {
    List<AsteroidEntity> findByHazardous(String hazardous);


}
