package com.example.demo.integration;

import com.example.demo.config.ChuckProperties;
import com.example.demo.model.ChuckResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChuckClient {
    private final RestTemplate restTemplate;
    private final ChuckProperties properties;

    public ChuckClient(RestTemplate restTemplate, ChuckProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public ChuckResponse getJoke() {
        ResponseEntity<ChuckResponse> response = restTemplate.exchange(
                properties.getUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        return response.getBody();

    }
}
