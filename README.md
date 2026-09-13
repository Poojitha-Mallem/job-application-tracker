# Job Application Tracker

A full-stack web application for tracking job applications, interview rounds, and follow-ups — built to learn Spring Boot and Angular, and to serve as a portfolio project for job interviews. Includes an AI-powered feature that generates tailored interview prep questions from a job description.

## Features

- **Authentication** — JWT-based registration and login, with role-based access control and BCrypt password hashing
- **Application tracking** — full CRUD for job applications with search, filtering by company/status, pagination, and sorting
- **Interview rounds** — track multiple interview stages per application (phone screen, technical, system design, HR, managerial) with feedback and results
- **File attachments** — upload resumes, job descriptions, and cover letters per application
- **Kanban board** — drag-and-drop interface to move applications between status columns
- **Automated reminders** — a scheduled daily job flags applications with no update in 7+ days
- **Analytics dashboard** — response rate, interview rate, and status breakdown via aggregation queries
- **AI interview prep** — generates tailored technical and behavioral interview questions from a job's description using Google's Gemini API

## Tech Stack

**Backend**
- Java 17, Spring Boot 4
- Spring Security + JWT (jjwt)
- Spring Data JPA / Hibernate
- PostgreSQL
- Maven
- JUnit 5, Mockito

**Frontend**
- Angular (standalone components, signals, zoneless change detection)
- Angular CDK (drag-and-drop)
- RxJS
- Vitest

**AI Integration**
- Google Gemini API (`gemini-flash-latest`)

## Architecture

```
┌─────────────┐      REST + JWT      ┌──────────────────┐      JDBC      ┌────────────┐
│   Angular   │ ───────────────────▶ │  Spring Boot API │ ─────────────▶ │ PostgreSQL │
│  (port 4200)│ ◀─────────────────── │   (port 8080)    │ ◀───────────── │            │
└─────────────┘                      └──────────┬────────┘                └────────────┘
                                                  │
                                                  │  HTTPS
                                                  ▼
                                         ┌─────────────────┐
                                         │  Google Gemini  │
                                         │       API       │
                                         └─────────────────┘
```

**Backend layering**: Controller → Service → Repository, with DTOs separating API contracts from JPA entities, a global exception handler for consistent error responses, and per-request ownership checks to prevent unauthorized access to another user's data (applications, interview rounds, and attachments are all scoped to the authenticated user).

## Prerequisites

- Java 17+
- Node.js 18+ and Angular CLI
- PostgreSQL (running locally)
- A free Google Gemini API key ([Google AI Studio](https://aistudio.google.com))

## Setup

### 1. Clone and configure the database

```bash
git clone https://github.com/Poojitha-Mallem/job-application-tracker
cd job-application-tracker
```

Create the database:
```sql
CREATE DATABASE job_tracker;
```

### 2. Backend setup

Create `backend/src/main/resources/application-local.properties` (gitignored — never commit this file):
```properties
spring.datasource.password=your_postgres_password
jwt.secret=your_random_secret_at_least_32_characters
gemini.api.key=your_gemini_api_key
```

Run the backend:
```bash
cd backend
./mvnw spring-boot:run
```
The API will start on `http://localhost:8080`.

### 3. Frontend setup

```bash
cd frontend
npm install
ng serve
```
The app will be available at `http://localhost:4200`.

### 4. Run tests

Backend:
```bash
cd backend
./mvnw test
```

Frontend:
```bash
cd frontend
ng test --watch=false
```

## API Overview

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Log in and receive a JWT |
| GET | `/api/applications` | List applications (paginated, filterable) |
| POST | `/api/applications` | Create an application |
| GET | `/api/applications/{id}` | Get a single application |
| PUT | `/api/applications/{id}` | Update an application |
| PATCH | `/api/applications/{id}/status` | Update only the status |
| DELETE | `/api/applications/{id}` | Delete an application |
| GET/POST | `/api/applications/{id}/rounds` | List/create interview rounds |
| POST | `/api/applications/{id}/attachments` | Upload a file (multipart) |
| GET | `/api/applications/{id}/interview-prep` | Generate AI interview questions |
| GET | `/api/stats/summary` | Get dashboard statistics |

All endpoints except `/api/auth/**` require a `Authorization: Bearer <token>` header.


## What I Learned

This project was built to learn Spring Boot and Angular from a Java background, and to get hands-on experience with LLM API integration. Notable things worked through along the way: JWT authentication and Spring Security configuration, preventing IDOR vulnerabilities with per-request ownership checks, GROUP BY aggregation queries, prompt design and structured JSON parsing for LLM responses, Angular's newer standalone/signals/zoneless architecture, and drag-and-drop with Angular CDK.

## License

MIT
