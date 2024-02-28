package com.example.issproject.service.issService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import static java.lang.Math.sqrt;

@Service
public class ISSLocationServiceImpl implements ISSLocationService {
    private String url = "http://api.open-notify.org/iss-now.json";
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    @Autowired
    @Lazy
    public ISSLocationServiceImpl(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl(url).build();
        this.objectMapper = objectMapper;
    }

    @Override
    public String getLocationOfISS() {
        try {
            String response = webClient.get().retrieve().bodyToMono(String.class).block();
            JsonNode jsonNode = objectMapper.readTree(response);

            String longitude = jsonNode.path("iss_position").path("longitude").asText();
            String latitude = jsonNode.path("iss_position").path("latitude").asText();

            return "Current ISS Location:\nLatitude: " + latitude + "\nLongitude: " + longitude;


        } catch (Exception e){
            e.printStackTrace();
            return "Failed to get ISS location";
        }
    }

    @Override
    public String getTrackOfISS() throws InterruptedException {
        String longitudeOne;
        String latitudeOne;
        try {
            String response = webClient.get().retrieve().bodyToMono(String.class).block();
            JsonNode jsonNode = objectMapper.readTree(response);

            longitudeOne = jsonNode.path("iss_position").path("longitude").asText();
            latitudeOne = jsonNode.path("iss_position").path("latitude").asText();


        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to get ISS location";
        }

        Thread.sleep(3000);


        String longitudeTwo;
        String latitudeTwo;
        try {
            String response = webClient.get().retrieve().bodyToMono(String.class).block();
            JsonNode jsonNode = objectMapper.readTree(response);

            longitudeTwo = jsonNode.path("iss_position").path("longitude").asText();
            latitudeTwo = jsonNode.path("iss_position").path("latitude").asText();


        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to get ISS location";
        }

        double deltaLong = Double.parseDouble(longitudeTwo) - Double.parseDouble(longitudeOne);
        double deltaLat = Double.parseDouble(latitudeTwo) - Double.parseDouble(latitudeOne);


        return "ISS Track is: " + sqrt(Math.pow(deltaLong, 2)+Math.pow(deltaLat, 2));

    }
}
