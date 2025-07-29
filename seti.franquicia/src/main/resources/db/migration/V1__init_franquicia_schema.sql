CREATE DATABASE IF NOT EXISTS franquicias_db;
USE franquicias_db;

-- Tabla de Franquicia
CREATE TABLE franquicia (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL
);

-- Tabla de Sucursal
CREATE TABLE sucursal (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    franquicia_id INTEGER,
    FOREIGN KEY (franquicia_id) REFERENCES franquicia(id) ON DELETE CASCADE
);

-- Tabla de Producto
CREATE TABLE producto (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    stock INT NOT NULL,
    sucursal_id INTEGER,
    FOREIGN KEY (sucursal_id) REFERENCES sucursal(id) ON DELETE CASCADE
);