package com.sakcode.elearning.school.infrastructure.data;

import com.sakcode.elearning.school.features.course.Course;
import com.sakcode.elearning.school.features.course.CourseRepository;
import com.sakcode.elearning.school.features.lesson.Lesson;
import com.sakcode.elearning.school.features.lesson.LessonRepository;
import com.sakcode.elearning.school.features.student.PlanType;
import com.sakcode.elearning.school.features.student.Student;
import com.sakcode.elearning.school.features.student.StudentRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

  private final StudentRepository studentRepository;
  private final CourseRepository courseRepository;
  private final LessonRepository lessonRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) {
    if (studentRepository.count() > 0) {
      log.info("Data already loaded, skipping...");
      return;
    }

    log.info("Loading sample data...");

    // Create sample students
    Student student1 =
        new Student(
            "john@example.com", "John Doe", passwordEncoder.encode("password123"), PlanType.FREE);
    student1 = studentRepository.save(student1);

    Student student2 =
        new Student(
            "jane@example.com",
            "Jane Smith",
            passwordEncoder.encode("password123"),
            PlanType.PREMIUM);
    student2 = studentRepository.save(student2);

    log.info("Created students: {} (FREE), {} (PREMIUM)", student1.getEmail(), student2.getEmail());

    // Create sample courses
    Course course1 =
        new Course(
            "Java Programming Fundamentals",
            "Learn Java from scratch. Covers OOP, collections, streams, and more.",
            new BigDecimal("49.99"),
            "Dr. Alan Turing");
    course1 = courseRepository.save(course1);

    Course course2 =
        new Course(
            "Spring Boot Masterclass",
            "Build production-ready applications with Spring Boot 4.0",
            new BigDecimal("79.99"),
            "Prof. Dennis Ritchie");
    course2 = courseRepository.save(course2);

    Course course3 =
        new Course(
            "Data Structures & Algorithms",
            "Master DSA with practical examples and coding challenges.",
            new BigDecimal("59.99"),
            "Dr. Grace Hopper");
    course3 = courseRepository.save(course3);

    Course course4 =
        new Course(
            "Introduction to Python",
            "A beginner-friendly Python course for everyone.",
            new BigDecimal("0.00"),
            "Guido van Rossum");
    course4 = courseRepository.save(course4);

    log.info("Created {} courses", 4);

    // Create lessons for Java course
    createLessons(
        course1.getId(),
        new String[][] {
          {
            "Introduction to Java",
            "Learn about Java history, setup JDK, and write your first program."
          },
          {"Variables and Data Types", "Understand primitive types, strings, and type conversion."},
          {"Control Flow", "Master if-else, switch, loops, and conditional statements."},
          {"Object-Oriented Programming", "Learn classes, objects, inheritance, and polymorphism."},
          {"Collections Framework", "Explore List, Set, Map, and their implementations."}
        });

    // Create lessons for Spring Boot course
    createLessons(
        course2.getId(),
        new String[][] {
          {"Spring Boot Basics", "Introduction to Spring Boot and its auto-configuration."},
          {"Dependency Injection", "Understand IoC, DI, and Spring Bean lifecycle."},
          {"Building REST APIs", "Create RESTful web services with Spring MVC."},
          {"Data Access with JPA", "Learn Spring Data JPA and repository pattern."},
          {"Security with Spring", "Implement authentication and authorization."}
        });

    // Create lessons for DSA course
    createLessons(
        course3.getId(),
        new String[][] {
          {"Arrays and Strings", "Fundamental data structures for storing collections."},
          {"Linked Lists", "Singly and doubly linked lists implementation."},
          {"Stacks and Queues", "LIFO and FIFO data structures."},
          {"Trees and Graphs", "Binary trees, BST, and graph traversals."},
          {"Sorting Algorithms", "Bubble, merge, quick sort and their complexities."}
        });

    // Create lessons for Python course
    createLessons(
        course4.getId(),
        new String[][] {
          {"Getting Started with Python", "Install Python and write your first script."},
          {"Python Data Types", "Numbers, strings, lists, tuples, and dictionaries."},
          {"Functions and Modules", "Define functions and organize code with modules."}
        });

    log.info("Sample data loaded successfully!");
  }

  private void createLessons(Long courseId, String[][] lessonData) {
    for (int i = 0; i < lessonData.length; i++) {
      Lesson lesson = new Lesson(courseId, lessonData[i][0], lessonData[i][1], i + 1);
      lessonRepository.save(lesson);
    }
  }
}
