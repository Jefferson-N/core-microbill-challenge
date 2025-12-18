-- Actualizar contraseñas con hashes correctos
-- Hash para Admin#123: $2a$10$8K1p/wf4C2ki65fUFyOuAuIiQOpPgH4ALg532E/M4w4ZMZbfmub4O
-- Hash para User#123: $2a$10$8K1p/wf4C2ki65fUFyOuAuIiQOpPgH4ALg532E/M4w4ZMZbfmub4O

UPDATE users SET password = '$2a$10$8K1p/wf4C2ki65fUFyOuAuIiQOpPgH4ALg532E/M4w4ZMZbfmub4O' WHERE username = 'admin@demo.com';
UPDATE users SET password = '$2a$10$8K1p/wf4C2ki65fUFyOuAuIiQOpPgH4ALg532E/M4w4ZMZbfmub4O' WHERE username = 'user@demo.com';