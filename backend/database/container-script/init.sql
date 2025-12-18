-- =========================
-- INICIALIZACIÓN DE BASE DE DATOS
-- =========================

-- Crear base de datos si no existe
CREATE DATABASE IF NOT EXISTS microbill_db;

-- Crear usuario y otorgar permisos
CREATE USER IF NOT EXISTS 'microbill_user'@'%' IDENTIFIED BY 'microbill_pass_2024';
GRANT ALL PRIVILEGES ON microbill_db.* TO 'microbill_user'@'%';
FLUSH PRIVILEGES;

-- Usar la base de datos
USE microbill_db;

-- Crear tablas básicas para usuarios demo
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT,
    role_id BIGINT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Insertar roles
INSERT IGNORE INTO roles (name) VALUES ('ADMIN'), ('USER');

-- Insertar usuarios demo (password: Admin#123 y User#123 hasheados con BCrypt)
INSERT IGNORE INTO users (username, password, email) VALUES 
('admin@demo.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVMIlW', 'admin@demo.com'),
('user@demo.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVMIlW', 'user@demo.com');

-- Asignar roles
INSERT IGNORE INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'admin@demo.com' AND r.name = 'ADMIN';

INSERT IGNORE INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'user@demo.com' AND r.name = 'USER';
