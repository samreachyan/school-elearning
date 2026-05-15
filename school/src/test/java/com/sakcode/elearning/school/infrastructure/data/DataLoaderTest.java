package com.sakcode.elearning.school.infrastructure.data;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.sakcode.elearning.school.features.course.Course;
import com.sakcode.elearning.school.features.course.CourseRepository;
import com.sakcode.elearning.school.features.lesson.Lesson;
import com.sakcode.elearning.school.features.lesson.LessonRepository;
import com.sakcode.elearning.school.features.student.PlanType;
import com.sakcode.elearning.school.features.student.Student;
import com.sakcode.elearning.school.features.student.StudentRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class DataLoaderTest {

  @Mock private StudentRepository studentRepository;
  @Mock private CourseRepository courseRepository;
  @Mock private LessonRepository lessonRepository;
  @Mock private PasswordEncoder passwordEncoder;

  @Captor private ArgumentCaptor<Student> studentCaptor;
  @Captor private ArgumentCaptor<Course> courseCaptor;
  @Captor private ArgumentCaptor<Lesson> lessonCaptor;

  private DataLoader dataLoader;

  @BeforeEach
  void setUp() {
    dataLoader =
        new DataLoader(studentRepository, courseRepository, lessonRepository, passwordEncoder);
  }

  @Test
  void shouldLoadSampleDataWhenDatabaseIsEmpty() {
    when(studentRepository.count()).thenReturn(0L);
    when(passwordEncoder.encode("password123")).thenReturn("encoded-password");

    Student savedStudent1 = new Student("john@example.com", "John Doe", "encoded-password", PlanType.FREE);
    savedStudent1.setId(1L);
    Student savedStudent2 = new Student("jane@example.com", "Jane Smith", "encoded-password", PlanType.PREMIUM);
    savedStudent2.setId(2L);

    when(studentRepository.save(any(Student.class)))
        .thenReturn(savedStudent1)
        .thenReturn(savedStudent2);

    Course savedCourse1 = new Course("Java Programming Fundamentals", "Learn Java", new BigDecimal("49.99"), "Dr. Alan Turing");
    savedCourse1.setId(1L);
    Course savedCourse2 = new Course("Spring Boot Masterclass", "Build apps", new BigDecimal("79.99"), "Prof. Dennis Ritchie");
    savedCourse2.setId(2L);
    Course savedCourse3 = new Course("Data Structures & Algorithms", "Master DSA", new BigDecimal("59.99"), "Dr. Grace Hopper");
    savedCourse3.setId(3L);
    Course savedCourse4 = new Course("Introduction to Python", "Python course", new BigDecimal("0.00"), "Guido van Rossum");
    savedCourse4.setId(4L);

    when(courseRepository.save(any(Course.class)))
        .thenReturn(savedCourse1)
        .thenReturn(savedCourse2)
        .thenReturn(savedCourse3)
        .thenReturn(savedCourse4);

    when(lessonRepository.save(any(Lesson.class))).thenAnswer(invocation -> invocation.getArgument(0));

    dataLoader.run();

    verify(studentRepository).count();
    verify(studentRepository, times(2)).save(any(Student.class));
    verify(courseRepository, times(4)).save(any(Course.class));
    // 5 + 5 + 5 + 3 = 18 lessons
    verify(lessonRepository, times(18)).save(any(Lesson.class));
  }

  @Test
  void shouldSkipLoadingWhenDataAlreadyExists() {
    when(studentRepository.count()).thenReturn(5L);

    dataLoader.run();

    verify(studentRepository, never()).save(any(Student.class));
    verify(courseRepository, never()).save(any(Course.class));
    verify(lessonRepository, never()).save(any(Lesson.class));
  }
}
