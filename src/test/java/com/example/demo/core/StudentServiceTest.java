package com.example.demo.core;

import com.example.demo.exception.StudentNotFoundException;
import com.example.demo.integration.BookingClient;
import com.example.demo.integration.ChuckClient;
import com.example.demo.model.ChuckResponse;
import com.example.demo.model.Gender;
import com.example.demo.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private ChuckClient chuckClient;

    @Mock
    private BookingClient bookingClient;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void addStudentWithRandomJoke() {
        ChuckResponse randomJoke = new ChuckResponse("Тип шутка должна из сервиса");
        Student student = new Student("Aleg", "admin@mail.ru", Gender.FEMALE);


        when(chuckClient.getJoke()).thenReturn(randomJoke);
        studentService.addStudent(student);

        assertNotEquals("Случайная шутка", student.getJoke());

        verify(chuckClient, times(1)).getJoke();
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void getStudentIfExistById() {
        Student student = new Student("John", "john@mail.ru", Gender.MALE);
        student.setId(1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student getStudent = studentService.getStudent(1L);

        assertNotNull(getStudent);
        assertEquals(1L, getStudent.getId());

        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void getExceptionNotFoundStudentIfNotExistById() {
        when(studentRepository.findById(99999L)).thenReturn(Optional.empty());

        StudentNotFoundException exception = assertThrows(
                StudentNotFoundException.class,
                () -> studentService.getStudent(99999L)
        );

        assertEquals("Студент с id = " + 99999L + " не был найден в базе данных", exception.getMessage());

        verify(studentRepository, times(1)).findById(99999L );
    }

    @Test
    void createStudentWithCorrectId() {
        Student student = new Student("John", "john@mail.ru", Gender.MALE);
        student.setId(1L);

        when(studentRepository.selectExistsEmail(anyString())).thenReturn(false);
        when(chuckClient.getJoke()).thenReturn(new ChuckResponse(""));
        when(bookingClient.createBookingAndGetId(any())).thenReturn(Optional.of(1));

        studentService.addStudent(student);

        assertEquals(1, student.getBookingId());

        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void createStudentWithIncorrectId() {
        Student student = new Student("John", "john@mail.ru", Gender.MALE);
        student.setId(1L);

        when(studentRepository.selectExistsEmail(anyString())).thenReturn(false);
        when(chuckClient.getJoke()).thenReturn(new ChuckResponse(""));
        when(bookingClient.createBookingAndGetId(any())).thenReturn(Optional.empty());

        studentService.addStudent(student);

        assertEquals(-1, student.getBookingId());

        verify(studentRepository, times(1)).save(student);
    }
}