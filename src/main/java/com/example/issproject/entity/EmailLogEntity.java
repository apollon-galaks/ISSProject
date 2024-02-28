package com.example.issproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "email_log")
public class EmailLogEntity {
    @Id
    @Column(name = "insert_date")
    private LocalDateTime insertDate;
    @Column(name = "recipient_email")
    private String recipientEmail;
    @Column(name = "asteroid_info_message")
    private String asteroidInfoMessage;
}
