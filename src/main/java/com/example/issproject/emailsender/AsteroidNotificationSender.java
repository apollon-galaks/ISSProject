package com.example.issproject.emailsender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AsteroidNotificationSender {

    @Value("${app.email.sender-email}")
    private String emailAdress;
    @Value("${app.email.sender-password}")
    private String password;

    public void emailNotificationSender(){

    }

}
