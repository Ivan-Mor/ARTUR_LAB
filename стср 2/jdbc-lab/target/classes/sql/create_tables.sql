-- Скрипт генерации БД (таблицы из методички + новые таблицы задания пакета prepared_statement)

CREATE TABLE IF NOT EXISTS authors (
    id BIGSERIAL PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT
);

CREATE TABLE IF NOT EXISTS books (
    id BIGSERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    author_id BIGINT NOT NULL,
    year_of_publication INT,
    FOREIGN KEY (author_id) REFERENCES authors (id)
);

CREATE TABLE IF NOT EXISTS shops (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    address TEXT
);

CREATE TABLE IF NOT EXISTS shops_books (
    id BIGSERIAL PRIMARY KEY,
    shop_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    FOREIGN KEY (shop_id) REFERENCES shops (id),
    FOREIGN KEY (book_id) REFERENCES books (id)
);

CREATE TABLE IF NOT EXISTS students (
    id BIGSERIAL PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT,
    age INT NOT NULL,
    city TEXT,
    email TEXT
);

-- Новая сущность №1: издательства
CREATE TABLE IF NOT EXISTS publishers (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    country TEXT,
    founding_year INT,
    email TEXT
);

-- Новая сущность №2: читатели
CREATE TABLE IF NOT EXISTS readers (
    id BIGSERIAL PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT,
    phone TEXT,
    city TEXT,
    birth_year INT
);
