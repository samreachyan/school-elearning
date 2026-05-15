# E-Learning Platform

A modern e-learning platform built with **Spring Boot 4.0** using **Vertical Slice Architecture** — a feature-first, domain-driven approach that organizes code by business capability rather than technical layers.

---

## Table of Contents

- [Architecture Overview](#architecture-overview)
- [Application Flow](#application-flow)
- [Project Structure](#project-structure)
- [How to Implement a New Feature](#how-to-implement-a-new-feature)
- [Key Architectural Highlights](#key-architectural-highlights)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)

---

## Architecture Overview

### Vertical Slice Architecture (Feature-Based)

Unlike traditional layered architecture (Controller → Service → Repository split across packages), this project organizes code **by feature**. Each feature is a self-contained vertical slice with its own:

- **Request/Response DTOs** — Input validation and output shaping
- **Handler** — Business logic (implements the Mediator pattern)
- **Repository** — Data access (shared entities live at feature level)

```
Traditional Layered Architecture          Vertical Slice Architecture
┌─────────────────────────────┐           ┌─────────────────────────────┐
│  controllers/               │           │  features/                  │
│    StudentController        │           │    student/                 │
│    CourseController         │           │      register/              │
│  services/                  │           │        RegisterStudentReq   │
│    StudentService           │           │        RegisterStudentRes   │
│    CourseService            │           │        RegisterStudentHdlr  │
│  repositories/              │           │      login/                 │
│    StudentRepository        │           │      profile/               │
│    CourseRepository         │           │      updateplan/            │
│  domain/                    │           │    course/                  │
│    Student                  │           │      create/                │
│    Course                   │           │      list/                  │
└─────────────────────────────┘           │      enroll/                │
                                          │    progress/                │
                                          │      marklesson/            │
                                          │      getprogress/           │
                                          │      generatecertificate/   │
                                          └─────────────────────────────┘
```

### Mediator Pattern

All business logic is invoked through a **Mediator** — a central dispatcher that routes requests to their corresponding handlers. Controllers never call handlers directly; they send requests to the Mediator.

```
┌──────────┐     send(request)     ┌───────────┐     resolve & invoke     ┌─────────────┐
│ Controller│ ──────────────────→  │ Mediator  │ ──────────────────────→  │  Handler    │
└──────────┘                       └───────────┘                          └─────────────┘
       │                                                                        │
       │  Request (IRequest<T>)                                                 │  Business Logic
       │  - Contains input data                                                 │  - Validation
       │  - Jakarta Validation annotations                                      │  - DB operations
       │                                                                        │  - Calculations
       │                                                                        ▼
       │                                                                ┌─────────────┐
       │                                                                │  Response   │
       └───────────────────────────────────────────────────────────────→│  (DTO)      │
                                                                        └─────────────┘
```

**How the Mediator resolves handlers:**
The `Mediator` uses a naming convention: `XxxRequest` → `XxxHandler`. It resolves the handler class name by replacing `"Request"` with `"Handler"` in the request class name, then fetches the bean from Spring's `ApplicationContext`.

---

## Application Flow

### Request Lifecycle

```
HTTP Request
     │
     ▼
┌─────────────────┐
│  Security Filter │  JwtAuthenticationFilter — extracts JWT, sets SecurityContext
│  (JWT)           │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   Controller     │  Thin layer: validates input, builds request, calls mediator.send()
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│    Mediator      │  Dispatches to the correct handler by naming convention
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│    Handler       │  Contains ALL business logic for this feature
│                  │  - Validates business rules
│                  │  - Calls repositories
│                  │  - Computes results
│                  │  - Returns response DTO
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   Repository     │  Spring Data JPA — data access
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   Database       │  PostgreSQL
└─────────────────┘
```

### Authentication Flow

```
1. POST /api/v1/auth/register
   └─→ RegisterStudentHandler
       └─→ Password encoded with BCrypt
       └─→ Student saved to DB

2. POST /api/v1/auth/login
   └─→ LoginHandler
       └─→ Validates email/password
       └─→ Returns JWT token (contains email + studentId)

3. Subsequent requests
   └─→ Authorization: Bearer <token>
       └─→ JwtAuthenticationFilter
           └─→ Validates token
           └─→ Creates StudentPrincipal (implements java.security.Principal)
           └─→ Sets SecurityContext
               └─→ Controller accesses via @AuthenticationPrincipal
```

---

## Project Structure

```
e-learning-platform/
│
├── school/                              # Spring Boot application module
│   ├── build.gradle                     # Dependencies & Spotless config
│   ├── settings.gradle
│   └── src/
│       ├── main/
│       │   ├── java/com/sakcode/elearning/school/
│       │   │   ├── SchoolApplication.java          # Entry point
│       │   │   │
│       │   │   ├── shared/                         # Cross-cutting concerns
│       │   │   │   ├── mediator/                   # Mediator pattern
│       │   │   │   │   ├── IRequest.java           # Marker interface for requests
│       │   │   │   │   ├── IRequestHandler.java    # Handler contract
│       │   │   │   │   ├── Mediator.java           # Central dispatcher
│       │   │   │   │   └── MediatorConfig.java     # Spring bean registration
│       │   │   │   │
│       │   │   │   ├── security/                   # JWT authentication
│       │   │   │   │   ├── JwtTokenProvider.java   # Token generation & validation
│       │   │   │   │   ├── JwtAuthenticationFilter.java  # Request filter
│       │   │   │   │   ├── SecurityConfig.java     # Spring Security config
│       │   │   │   │   └── StudentPrincipal.java   # Custom Principal
│       │   │   │   │
│       │   │   │   ├── exception/                  # Centralized error handling
│       │   │   │   │   ├── GlobalExceptionHandler.java  # @RestControllerAdvice
│       │   │   │   │   ├── BusinessException.java       # Custom exception
│       │   │   │   │   └── ErrorResponse.java           # Error DTO
│       │   │   │   │
│       │   │   │   └── domain/
│       │   │   │       └── BaseEntity.java         # Abstract base (id, createdAt)
│       │   │   │
│       │   │   ├── features/                       # Vertical slices
│       │   │   │   ├── student/                    # ── Student Management ──
│       │   │   │   │   ├── Student.java            # Entity
│       │   │   │   │   ├── PlanType.java           # Enum: FREE / PREMIUM
│       │   │   │   │   ├── StudentRepository.java  # JPA Repository
│       │   │   │   │   ├── StudentController.java  # REST Controller
│       │   │   │   │   ├── register/               # Feature: Register
│       │   │   │   │   ├── login/                  # Feature: Login
│       │   │   │   │   ├── profile/                # Feature: View Profile
│       │   │   │   │   └── updateplan/             # Feature: Update Plan
│       │   │   │   │
│       │   │   │   ├── course/                     # ── Course Management ──
│       │   │   │   │   ├── Course.java             # Entity
│       │   │   │   │   ├── CourseRepository.java   # JPA Repository
│       │   │   │   │   ├── CourseController.java   # REST Controller
│       │   │   │   │   ├── create/                 # Feature: Create Course
│       │   │   │   │   ├── list/                   # Feature: List Courses
│       │   │   │   │   └── enroll/                 # Feature: Enroll
│       │   │   │   │
│       │   │   │   ├── enrollment/                 # ── Enrollment ──
│       │   │   │   │   ├── Enrollment.java         # Entity
│       │   │   │   │   ├── EnrollmentRepository.java
│       │   │   │   │   ├── EnrolledCourseController.java  # List enrolled courses
│       │   │   │   │   └── EnrolledCourseDto.java
│       │   │   │   │
│       │   │   │   ├── lesson/                     # ── Lessons ──
│       │   │   │   │   ├── Lesson.java             # Entity
│       │   │   │   │   ├── LessonCompletion.java   # Entity
│       │   │   │   │   ├── LessonRepository.java
│       │   │   │   │   └── LessonCompletionRepository.java
│       │   │   │   │
│       │   │   │   └── progress/                   # ── Progress Tracking ──
│       │   │   │       ├── ProgressController.java # REST Controller
│       │   │   │       ├── marklesson/             # Feature: Mark Lesson Complete
│       │   │   │       ├── getprogress/            # Feature: Get Progress
│       │   │   │       └── generatecertificate/    # Feature: Generate Certificate
│       │   │   │
│       │   │   └── infrastructure/
│       │   │       └── data/
│       │   │           └── DataLoader.java         # Sample data (CommandLineRunner)
│       │   │
│       │   └── resources/
│       │       └── application.yml                 # Configuration
│       │
│       └── test/
│           └── java/com/sakcode/elearning/school/
│               └── SchoolApplicationTests.java
│
├── docker-compose.yml                  # PostgreSQL container
├── ARCHITECTURE.md                     # Architecture requirements doc
└── README.md                           # This file
```

---

## How to Implement a New Feature

Adding a new feature follows a consistent, repeatable pattern. Here's a step-by-step guide:

### Step 1: Create the Feature Package

```
features/
└── yourfeature/
    ├── YourFeatureRequest.java     # Input DTO
    ├── YourFeatureResponse.java    # Output DTO
    └── YourFeatureHandler.java     # Business logic
```

### Step 2: Create the Request DTO

```java
package com.sakcode.elearning.school.features.yourfeature;

import com.sakcode.elearning.school.shared.mediator.IRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YourFeatureRequest implements IRequest<YourFeatureResponse> {

    @NotNull(message = "Field is required")
    private Long someId;

    // Add more fields with Jakarta Validation annotations
}
```

### Step 3: Create the Response DTO

```java
package com.sakcode.elearning.school.features.yourfeature;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YourFeatureResponse {
    private Long id;
    private String message;
    // Add response fields
}
```

### Step 4: Create the Handler

```java
package com.sakcode.elearning.school.features.yourfeature;

import com.sakcode.elearning.school.shared.mediator.IRequestHandler;
import com.sakcode.elearning.school.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class YourFeatureHandler
        implements IRequestHandler<YourFeatureRequest, YourFeatureResponse> {

    // Inject repositories via constructor (Lombok @RequiredArgsConstructor)

    @Override
    public YourFeatureResponse handle(YourFeatureRequest request) {
        // 1. Validate business rules (throw BusinessException if needed)
        // 2. Perform business logic
        // 3. Return response DTO
    }
}
```

### Step 5: Add the Controller Endpoint

Add a method to an existing controller or create a new one:

```java
@RestController
@RequestMapping("/api/v1/your-resource")
@RequiredArgsConstructor
public class YourFeatureController {

    private final Mediator mediator;

    @PostMapping
    public ResponseEntity<YourFeatureResponse> doSomething(
            @Valid @RequestBody YourFeatureRequest request) {
        YourFeatureResponse response = mediator.send(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
```

### Step 6: Add Entity & Repository (if new data)

```java
@Entity
@Table(name = "your_entities")
@Getter @Setter @NoArgsConstructor
public class YourEntity extends BaseEntity {
    // fields
}

public interface YourEntityRepository extends JpaRepository<YourEntity, Long> {
    // custom queries
}
```

### Step 7: Update Security Config (if needed)

Add endpoint matchers in `SecurityConfig.java` for public/authenticated access.

### Step 8: Run Spotless

```bash
./gradlew spotlessApply   # Format code
./gradlew spotlessCheck   # Verify formatting
```

---

## Key Architectural Highlights

### 1. Feature Isolation (Vertical Slice)

| Benefit | Description |
|---------|-------------|
| **High Cohesion** | All code for a feature lives together — request, handler, response |
| **Low Coupling** | Features don't import from each other; they share only entities |
| **Easy to Navigate** | Find everything for "enroll" in `course/enroll/` |
| **Safe to Modify** | Changes to one feature won't break another |
| **Parallel Development** | Multiple developers can work on different features simultaneously |

### 2. Mediator Pattern

| Benefit | Description |
|---------|-------------|
| **Thin Controllers** | Controllers only validate input and call `mediator.send()` |
| **No Service Layer** | Handlers replace traditional service classes |
| **Automatic Routing** | Handler resolution by naming convention — no manual wiring |
| **Consistent Entry Point** | Every feature follows the same `Request → Handler → Response` flow |
| **Testable** | Handlers can be unit tested independently of HTTP layer |

### 3. Centralized Exception Handling

`GlobalExceptionHandler` (annotated with `@RestControllerAdvice`) catches all exceptions and returns consistent JSON error responses:

```json
{
    "status": 400,
    "error": "Bad Request",
    "message": "Student not found",
    "errorCode": "STUDENT_NOT_FOUND",
    "timestamp": "2026-05-16T05:25:59",
    "path": "/api/v1/auth/profile",
    "validationErrors": null
}
```

Handled exception types:
- `BusinessException` — Custom business rule violations
- `BadCredentialsException` — Invalid login
- `UsernameNotFoundException` — User not found
- `MethodArgumentNotValidException` — Jakarta Validation failures
- `IllegalArgumentException` — Invalid arguments
- `Exception` — Catch-all for unexpected errors

### 4. JWT Authentication

| Component | Responsibility |
|-----------|---------------|
| `JwtTokenProvider` | Generate, parse, and validate JWT tokens |
| `JwtAuthenticationFilter` | Extract token from `Authorization` header, set `SecurityContext` |
| `StudentPrincipal` | Custom `Principal` carrying `email` and `studentId` |
| `SecurityConfig` | Configure public endpoints, stateless sessions, CORS |

### 5. Code Quality with Spotless

The project uses **Spotless** with **Google Java Format** to enforce consistent code style:

```bash
./gradlew spotlessCheck    # Check formatting (runs as part of build)
./gradlew spotlessApply    # Auto-fix formatting issues
```

Enforced rules:
- Google Java Style (2-space indentation)
- Organized imports
- No unused imports
- No trailing whitespace
- Files end with newline

### 6. Database Schema

```
students                    courses
├── id (PK)                 ├── id (PK)
├── email (UNIQUE)          ├── title
├── name                    ├── description (TEXT)
├── password (BCrypt)       ├── price (DECIMAL)
├── plan_type (FREE/PREMIUM)├── instructor
└── created_at              └── created_at

enrollments                 lessons
├── id (PK)                 ├── id (PK)
├── student_id (FK)         ├── course_id (FK)
├── course_id (FK)          ├── title
├── progress_percentage     ├── content (TEXT)
└── created_at              └── order_number

lesson_completions
├── id (PK)
├── enrollment_id (FK)
├── lesson_id (FK)
└── completed_at
```

### 7. Key Design Decisions

| Decision | Rationale |
|----------|-----------|
| **No MapStruct** | Simple DTOs use Lombok `@Builder` — avoids annotation processor complexity |
| **BCrypt for passwords** | Industry-standard password hashing |
| **JWT in header** | Stateless authentication, no session management |
| **PostgreSQL** | Production-grade relational database |
| **`create-drop` DDL** | For development; change to `validate` in production |
| **Handler naming convention** | `XxxRequest` → `XxxHandler` — convention over configuration |

---

## Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21 | Language |
| Spring Boot | 4.0.6 | Application framework |
| Spring Security | 6.x | Authentication & authorization |
| Spring Data JPA | 3.x | Database access |
| PostgreSQL | 15 | Database |
| JWT (jjwt) | 0.12.6 | Token-based authentication |
| Lombok | Latest | Boilerplate reduction |
| Gradle | 9.4.1 | Build tool |
| Spotless | 7.0.3 | Code formatting |

---

## Getting Started

### Prerequisites

- Java 21+
- Docker (for PostgreSQL)
- Gradle (or use the included wrapper)

### 1. Start PostgreSQL

```bash
docker compose up -d
```

### 2. Run the Application

```bash
cd school
./gradlew bootRun
```

The application starts on `http://localhost:8080` with sample data pre-loaded.

### 3. Verify Formatting

```bash
./gradlew spotlessCheck
```

### 4. Build (includes tests + formatting check)

```bash
./gradlew build
```

---

## API Endpoints

### Authentication

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/v1/auth/register` | No | Register a new student |
| POST | `/api/v1/auth/login` | No | Login, returns JWT |
| GET | `/api/v1/auth/profile` | Yes | Get student profile |
| PUT | `/api/v1/auth/plan` | Yes | Update subscription plan |

### Courses

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/v1/courses` | No | Create a course |
| GET | `/api/v1/courses` | No | List courses (optional `?minPrice=&maxPrice=`) |
| POST | `/api/v1/courses/enroll` | Yes | Enroll in a course |

### Enrollments

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/v1/enrollments` | Yes | Get enrolled courses for student |

### Progress

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/v1/progress/lessons/complete` | Yes | Mark lesson as completed |
| GET | `/api/v1/progress?enrollmentId=` | Yes | Get course progress |
| POST | `/api/v1/progress/certificate` | Yes | Generate completion certificate |

### Sample Data

On startup, the `DataLoader` creates:
- **2 students**: `john@example.com` (FREE), `jane@example.com` (PREMIUM) — password: `password123`
- **4 courses**: Java, Spring Boot, DSA, Python
- **18 lessons**: 5 per Java/Spring/DSA course, 3 for Python
