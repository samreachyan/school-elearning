package com.sakcode.elearning.school;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class SchoolApplicationTests {

  @Autowired private ApplicationContext applicationContext;

  @Test
  void contextLoads() {
    assertNotNull(applicationContext, "Application context should load successfully");
  }

  @Test
  void mainMethodShouldRun() {
    SchoolApplication.main(new String[] {});
    assertTrue(true, "Application should start without exceptions");
  }
}
