-- Create databases
CREATE DATABASE IF NOT EXISTS microbill_auth CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS microbill_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS microbill_billing CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Use management database
USE microbill_management;

-- Customers table
CREATE TABLE IF NOT EXISTS customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    doc_number VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    address VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_doc_number (doc_number),
    INDEX idx_email (email)
) ENGINE=InnoDB;

-- Providers table
CREATE TABLE IF NOT EXISTS providers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    tax_id VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    address VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_tax_id (tax_id),
    INDEX idx_email (email)
) ENGINE=InnoDB;

-- Products table
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    tax_rate DECIMAL(5, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_code (code),
    INDEX idx_name (name)
) ENGINE=InnoDB;

-- Insert sample data
INSERT INTO customers (name, doc_number, email, address) VALUES
('Acme Corporation', '123456789', 'contact@acme.com', '123 Main St, New York, NY 10001'),
('Tech Solutions LLC', '987654321', 'info@techsolutions.com', '456 Tech Ave, San Francisco, CA 94102'),
('Global Enterprises', '555123456', 'sales@globalent.com', '789 Business Blvd, Austin, TX 78701');

INSERT INTO providers (name, tax_id, email, address) VALUES
('Tech Supplies Inc.', 'TAX-111222333', 'sales@techsupplies.com', '100 Supply St, Seattle, WA 98101'),
('Global Distributors', 'TAX-444555666', 'contact@globaldist.com', '200 Commerce Dr, Chicago, IL 60601');

INSERT INTO products (code, name, price, tax_rate, stock) VALUES
('LAPTOP-001', 'Dell XPS 15', 1500.00, 0.19, 50),
('MOUSE-001', 'Logitech MX Master 3', 99.99, 0.19, 100),
('KEYBOARD-001', 'Mechanical Keyboard RGB', 149.99, 0.19, 75),
('MONITOR-001', 'LG UltraWide 34"', 599.99, 0.19, 30),
('DOCK-001', 'USB-C Hub', 79.99, 0.19, 120);
