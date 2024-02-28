package com.example.issproject.service.asteroidService;

import com.example.issproject.entity.AsteroidEntity;
import com.example.issproject.entity.EmailLogEntity;
import com.example.issproject.parser.AsteroidParser;
import com.example.issproject.repository.AsteroidRepository;
import com.example.issproject.repository.EmailLogRepository;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class AsteroidServiceImpl implements AsteroidService{
    private static String APIKEY = "WyTnrKVE1lLx8Xaoi8pLTCJ3nJ0pOkggXtLhwav9";
    private static  String BASEURL = "https://api.nasa.gov/neo/rest/v1/feed";
    private final EmailLogRepository emailLogRepository;
    private final AsteroidRepository asteroidRepository;
    private final AsteroidParser asteroidParser;
    private final WebClient webClient;

    @Autowired
    public AsteroidServiceImpl(EmailLogRepository emailLogRepository, AsteroidRepository asteroidRepository, AsteroidParser asteroidParser, WebClient.Builder webClientBuilder) {
        this.emailLogRepository = emailLogRepository;
        this.asteroidRepository = asteroidRepository;
        this.asteroidParser = asteroidParser;
        this.webClient = webClientBuilder.baseUrl(BASEURL).build();
    }


    @Override
    public String getHazardousAsteroids(String startDate, String endDate) {


        String apiUrl = null;
        try {
            apiUrl = String.format("?start_date=%s&end_date=%s&api_key=%s", startDate, endDate, APIKEY);
            LocalDate startDt = LocalDate.parse(startDate);
            LocalDate endDt = LocalDate.parse(endDate);

            if(startDt.plusDays(7).isBefore(endDt)){
                System.out.println("Validation error: The difference between start and end dates must not be more than 7 days.");
                return "Validation error: The difference between start and end dates must not be more than 7 days.";
            } else {
              String response = webClient.get()
                        .uri(apiUrl)
                        .retrieve()
                        .bodyToMono(String.class).block();

                log.info("Url response returned successfully");

                List<AsteroidEntity> asteroids = asteroidParser
                        .responseParser(response, startDate, endDate);

                log.info("Data parsed and ready for posting to DB");

                asteroidRepository.saveAll(asteroids);

                log.info("Data \n" + asteroids + "\n were sent to DB");

                return response;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("Something went wrong");
        return BASEURL + apiUrl;
    }

    @Override
    public List<AsteroidEntity> getAllAsteroids() {
        return asteroidRepository.findAll();
    }

    @Override
    public AsteroidEntity getById(int id) {
        AsteroidEntity asteroid = null;
        Optional<AsteroidEntity> optional = asteroidRepository.findById(id);
        if(optional.isPresent()){
            asteroid = optional.get();
        }

        System.out.println(asteroid);
        return asteroid;
    }

    @Override
    public List<AsteroidEntity> getByHazardous(String hazardous) {

        String senderEmail = "";

        String senderPassword = "";

        List<String> recipientEmails = new ArrayList<>();
        recipientEmails.add("");


        List<AsteroidEntity> hazardousAsteroids = asteroidRepository.findByHazardous(hazardous);
        log.info("Hazardous asteroids list was returned from DB");

        String messageText = "";
        for(AsteroidEntity asteroid : hazardousAsteroids){
            messageText = messageText
                    + "Asteroid name: " + asteroid.getAsteroidName() + "\n"
                    + "Asteroid diameter: " + asteroid.getEstimatedDiameterKm() + " km\n"
                    + "Asteroid speed: " + asteroid.getKilometersPerSecond() + " km/sec \n"
                    + "Asteroid visibility: " + asteroid.getAbsoluteMagnitudeH() + "\n"
                    + "Closest approach date: " + asteroid.getCloseApproachDate() + "\n \n";
        }

        log.info("Message was created");

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        log.info("Authentication was successful");

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setSubject("New potentially hazardous asteroids prognosis for next days ");
            message.setText("Asteroid information: \n \n" + messageText + " \n \n" + "Reference: https://api.nasa.gov");

            for(String mails : recipientEmails){
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mails));
                Transport.send(message);
                log.info("Message was sent to "+ mails);

                EmailLogEntity messageLog = new EmailLogEntity();
                messageLog.setInsertDate(LocalDateTime.now());
                messageLog.setRecipientEmail(mails);
                messageLog.setAsteroidInfoMessage("Hazardous asteroids data");
                emailLogRepository.save(messageLog);
                log.info("Email notification logging saved for " + mails);
            }

        }
        catch (MessagingException e){
            e.printStackTrace();
        }

        return hazardousAsteroids;
    }

}
