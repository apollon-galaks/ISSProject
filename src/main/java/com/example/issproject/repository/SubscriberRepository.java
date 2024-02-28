package com.example.issproject.repository;

import com.example.issproject.entity.SubscriberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SubscriberRepository extends JpaRepository<SubscriberEntity, LocalDateTime> {
    //List<SubscriberEntity> findByStatus();
}
