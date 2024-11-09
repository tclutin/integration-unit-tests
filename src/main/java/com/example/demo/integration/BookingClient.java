package com.example.demo.integration;

import com.example.demo.config.BookingProperties;
import com.example.demo.config.ChuckProperties;
import com.example.demo.model.BookingRequest;
import com.example.demo.model.BookingResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Optional;

@Service
public class BookingClient {
    private final RestTemplate restTemplate;
    private final BookingProperties properties;

    public BookingClient(RestTemplate restTemplate, BookingProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public Optional<Integer> createBookingAndGetId(BookingRequest dto) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<BookingRequest> requestEntity = new HttpEntity<>(dto, headers);

            ResponseEntity<BookingResponse> response = restTemplate.exchange(
                    properties.getUrl(),
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<>() {
                    });


            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return Optional.of(response.getBody().getBookingid());
            }
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.empty();
    }


}
