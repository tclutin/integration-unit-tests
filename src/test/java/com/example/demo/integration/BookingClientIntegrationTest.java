package com.example.demo.integration;

import com.example.demo.config.ChuckProperties;
import com.example.demo.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@WireMockTest(httpPort = 8081)
public class BookingClientIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void checkIdCorrect() throws JsonProcessingException {
        int id = 1;
        BookingResponse response = new BookingResponse();
        response.setBookingid(id);


        stubFor(WireMock
                .post("/booking")
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_OK)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(response))
                )
        );

        Student created = new Student("Aleg", "admin@mail.ru", Gender.FEMALE);
        restTemplate.postForObject(
                "/api/v1/students",
                created,
                Student.class
        );

        Student exportedStudent = restTemplate.getForObject(
                "/api/v1/students/1",
                Student.class
        );

        assertEquals(id, exportedStudent.getBookingId());
    }
}
