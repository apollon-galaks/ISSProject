package com.example.issproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AsteroidServiceImpl implements AsteroidService{
    private final ObjectMapper objectMapper;
    private static String APIKEY = "WyTnrKVE1lLx8Xaoi8pLTCJ3nJ0pOkggXtLhwav9";
    private final WebClient webClient;
    private String baseUrl = "https://api.nasa.gov/neo/rest/v1/feed";

    @Autowired
    public AsteroidServiceImpl(ObjectMapper objectMapper, WebClient.Builder webClientBuilder) {

        this.objectMapper = objectMapper;
        this.webClient = webClientBuilder.baseUrl(baseUrl)
                .build();

    }

    @Override
    public String getHazardousAsteroids(String startDate, String endDate) {

        List<String> potentiallyHazardousAsteroidList = new ArrayList<>();

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

                potentiallyHazardousAsteroidList.add(jsonNode.path("is_potentially_hazardous_asteroid").asText());

                return response;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseUrl + apiUrl;
    }

}
