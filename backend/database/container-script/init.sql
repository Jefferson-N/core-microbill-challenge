-- =========================
-- INICIALIZACIÓN DE BASE DE DATOS
-- =========================

-- Crear base de datos si no existe
CREATE DATABASE IF NOT EXISTS microbill_billing;

-- Usar la base de datos
USE microbill_billing;

-- Crear usuario solo si no existe
SELECT COUNT(*) INTO @user_exists FROM mysql.user WHERE user = 'appuser' AND host = '%';
SET @sql = IF(@user_exists = 0, "CREATE USER 'appuser'@'%' IDENTIFIED BY 'appuser123';", 'SELECT "User already exists";');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Otorgar permisos
GRANT ALL PRIVILEGES ON microbill_billing.* TO 'appuser'@'%';
FLUSH PRIVILEGES;
