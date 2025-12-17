-- =========================
-- PERMISOS
-- =========================

CREATE USER 'appuser'@'%' IDENTIFIED BY 'appsecret';
GRANT ALL PRIVILEGES ON invoice_db.* TO 'appuser'@'%';
FLUSH PRIVILEGES;
