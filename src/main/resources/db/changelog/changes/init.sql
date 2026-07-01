--liquibase formatted sql

--changeset andrey:init
CREATE TABLE users (
    id UUID PRIMARY KEY,
    email TEXT UNIQUE,
    nickname TEXT,
    password_hash TEXT,
    role TEXT NOT NULL DEFAULT 'USER',
    created_at DATE
);

CREATE TABLE photo_files (
    id UUID PRIMARY KEY,
    file_name TEXT,
    file_content TEXT,
    file_extension VARCHAR(10),
    file_size_kb INT,
    upload_date DATE
);

CREATE TABLE photos (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(100),
    created_at DATE,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    photo_file_id UUID REFERENCES photo_files(id) ON DELETE SET NULL,
    version INT DEFAULT 0
);

CREATE TABLE ratings (
    id UUID PRIMARY KEY,
    visual_appeal INT NOT NULL,
    photo_quality INT NOT NULL,
    style INT NOT NULL,
    created_at DATE,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    photo_id UUID REFERENCES photos(id) ON DELETE CASCADE,
    UNIQUE (user_id, photo_id)
);

CREATE TABLE comments (
    id SERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    created_at DATE,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    photo_id UUID REFERENCES photos(id) ON DELETE CASCADE
);

--changeset andrey:seed-admin
INSERT INTO users (id, email, nickname, password_hash, role, created_at)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'admin@mail.ru', 'admin',
        '$2a$10$2YDogiEAPi4a/jccXbgI6uodMjaVO/1ArjM/N/nJqwpAq9fg83Hl2', 'ADMIN', current_date);
