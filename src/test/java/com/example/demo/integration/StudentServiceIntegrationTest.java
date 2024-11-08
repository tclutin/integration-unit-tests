package com.example.demo.integration;

import com.example.demo.config.ChuckProperties;
import com.example.demo.core.StudentService;
import com.example.demo.model.ChuckResponse;
import com.example.demo.model.Gender;
import com.example.demo.model.Student;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@WireMockTest(httpPort = 8081)
public class StudentServiceIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createStudentWithDefaultJoke() {
        ChuckResponse defaultJoke = new ChuckResponse("Случайная шутка");

        stubFor(WireMock
                .get("/jokes/random")
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                        .withHeader("Content-Type", "application/json")
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

        assertEquals(defaultJoke.getValue(), exportedStudent.getJoke());
    }
}
