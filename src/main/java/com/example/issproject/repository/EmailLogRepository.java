package com.example.issproject.repository;

import com.example.issproject.entity.EmailLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface EmailLogRepository extends JpaRepository<EmailLogEntity, LocalDateTime> {
}
