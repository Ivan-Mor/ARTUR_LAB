-- Создайте отдельную базу department_lab5 с кодировкой utf8mb4 до запуска скрипта.
-- Существующие базы и данные этим скриптом не удаляются.
CREATE TABLE departments (
    id CHAR(3) PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    location VARCHAR(255) NOT NULL
);
CREATE TABLE developers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    specialty VARCHAR(100) NOT NULL,
    department_id CHAR(3) NOT NULL,
    FOREIGN KEY (department_id) REFERENCES departments(id)
);
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(30) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(10) NOT NULL DEFAULT 'USER'
);
-- Новая таблица: один ответственный сотрудник может вести несколько проектов.
CREATE TABLE projects (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    developer_id INT NOT NULL,
    FOREIGN KEY (developer_id) REFERENCES developers(id)
);
INSERT INTO departments (id,name,location) VALUES ('A01','Разработка','Минск'),('A07','Тестирование','Минск');
INSERT INTO developers (name,specialty,department_id) VALUES ('Владимир','Java-разработчик','A01'),('Анна','Тестировщик','A07');
INSERT INTO projects (name,description,developer_id) VALUES ('Портал отдела','Внутренний портал сотрудников',1);
