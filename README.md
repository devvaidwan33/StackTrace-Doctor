# StackTraceDoctor - Full-Stack Spring Boot + PostgreSQL Project

## Project Structure
- `pom.xml` — Maven configuration (Spring Web, Spring Data JPA, PostgreSQL Driver)
- `schema.sql` — PostgreSQL table creation script (`users` and `bug_reports`)
- `src/main/resources/application.properties` — Database & Google Gemini REST API config
- `src/main/resources/static/index.html` — Multi-section YouTube/Amazon style UI (Analyze Bug, History Vault, Mistake Analytics, Profile)
- `src/main/java/com/stacktracedoctor/api/` — Complete Controller, Service, Repository, and Entity classes

## How to Run in IntelliJ IDEA
1. Open PostgreSQL (pgAdmin or `psql`) and create the database:
   ```sql
   CREATE DATABASE stacktrace_db;
   ```
2. Open this folder in **IntelliJ IDEA** as a Maven project.
3. Edit `src/main/resources/application.properties` to set your PostgreSQL password and free Google Gemini API key.
4. Run `StackTraceDoctorApplication.java` and open `http://localhost:8080/index.html` in your browser.
