-- PostgreSQL Schema for StackTraceDoctor
-- Run: CREATE DATABASE stacktrace_db; before executing these statements

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(150) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bug_reports (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    language VARCHAR(50) NOT NULL,
    error_category VARCHAR(80) NOT NULL,
    buggy_code TEXT NOT NULL,
    error_log TEXT NOT NULL,
    root_cause TEXT NOT NULL,
    fixed_code TEXT NOT NULL,
    viva_note TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE
);
