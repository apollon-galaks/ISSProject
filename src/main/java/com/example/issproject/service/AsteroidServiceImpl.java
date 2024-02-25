package com.example.issproject.service;

import com.example.issproject.entity.AsteroidEntity;
import com.example.issproject.repository.AsteroidRepository;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class AsteroidServiceImpl implements AsteroidService{
    private final AsteroidRepository asteroidRepository;
    private final ObjectMapper objectMapper;
    private static String APIKEY = "WyTnrKVE1lLx8Xaoi8pLTCJ3nJ0pOkggXtLhwav9";
    private final WebClient webClient;
    private String baseUrl = "https://api.nasa.gov/neo/rest/v1/feed";

    @Autowired
    public AsteroidServiceImpl(AsteroidRepository asteroidRepository, ObjectMapper objectMapper, WebClient.Builder webClientBuilder) {
        this.asteroidRepository = asteroidRepository;

        this.objectMapper = objectMapper;
        this.webClient = webClientBuilder.baseUrl(baseUrl)
                .build();

    }

    @Override
    public String getHazardousAsteroids(String startDate, String endDate) {

        String response = null;
        String apiUrl = null;
        try {
            apiUrl = String.format("?start_date=%s&end_date=%s&api_key=%s", startDate, endDate, APIKEY);
            LocalDate startDt = LocalDate.parse(startDate);
            LocalDate endDt = LocalDate.parse(endDate);

            if(startDt.plusDays(7).isBefore(endDt)){
                System.out.println("Validation error: The difference between start and end dates must not be more than 7 days.");
                return "Validation error: The difference between start and end dates must not be more than 7 days.";
            } else {
                response = webClient.get()
                        .uri(apiUrl)
                        .retrieve()
                        .bodyToMono(String.class).block();
                JsonNode jsonNode = objectMapper.readTree(response);

                int n = Integer.parseInt(jsonNode.path("element_count").asText());

                System.out.println(n);

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
                                .asText();

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
                                .path("kilometers_per_second").asText();

                        String astroDistance = asteroid
                                .path("close_approach_data")
                                .path(0)
                                .path("miss_distance")
                                .path("astronomical").asText();

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
                    start = start.plusDays(1);
                }

                System.out.println(asteroids);



                asteroidRepository.saveAll(asteroids);

                return response;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseUrl + apiUrl;
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

        String senderEmail = "yourEmail";

        String senderPassword = "yourPassword";

        String recipientEmail = "recipientEmail";

        List<String> recipientEmails = new ArrayList<>();
        recipientEmails.add("recipientMails");




        List<AsteroidEntity> hazardousAsteroids = asteroidRepository.findByHazardous(hazardous);
        String messageText = "";
        for(AsteroidEntity asteroid : hazardousAsteroids){
            messageText = messageText
                    + "Asteroid name: " + asteroid.getAsteroidName() + "\n"
                    + "Asteroid diameter: " + asteroid.getEstimatedDiameterKm() + " km\n"
                    + "Asteroid speed: " + asteroid.getKilometersPerSecond().substring(0, 4) + " km/sec \n"
                    + "Closest approach date: " + asteroid.getCloseApproachDate() + "\n \n";
        }

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

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setSubject("New potentially hazardous asteroids prognosis for next days ");
            message.setText("Asteroid information: \n \n" + messageText + " \n \n" + "Reference: https://api.nasa.gov");

            for(String mails : recipientEmails){
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mails));
                Transport.send(message);
            }

            System.out.println("Email sent successfully!");
        }
        catch (MessagingException e){
            e.printStackTrace();
        }

        return hazardousAsteroids;
    }

}
