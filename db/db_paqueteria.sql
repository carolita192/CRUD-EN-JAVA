-- Base de datos: db_paqueteria
-- Caso: Transporte

DROP DATABASE IF EXISTS db_paqueteria;
CREATE DATABASE db_paqueteria CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE db_paqueteria;

-- --------------------------------------------------------
-- 1. Tabla: camionero
-- --------------------------------------------------------
CREATE TABLE camionero (
    dni VARCHAR(20) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    direccion VARCHAR(150) NOT NULL,
    salario DECIMAL(10, 2) NOT NULL,
    poblacion VARCHAR(100) NOT NULL,
    CONSTRAINT pk_camionero PRIMARY KEY (dni),
    CONSTRAINT chk_salario_positivo CHECK (salario > 0)
) ENGINE=InnoDB;

-- --------------------------------------------------------
-- 2. Tabla: camion
-- --------------------------------------------------------
CREATE TABLE camion (
    matricula VARCHAR(20) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    potencia INT NOT NULL,
    CONSTRAINT pk_camion PRIMARY KEY (matricula),
    CONSTRAINT chk_potencia_positiva CHECK (potencia > 0)
) ENGINE=InnoDB;

-- --------------------------------------------------------
-- 3. Tabla: paquete
-- --------------------------------------------------------
CREATE TABLE paquete (
    codigo INT AUTO_INCREMENT NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    destinatario VARCHAR(100) NOT NULL,
    direccion_destinatario VARCHAR(150) NOT NULL,
    dni_camionero VARCHAR(20) NOT NULL,
    CONSTRAINT pk_paquete PRIMARY KEY (codigo),
    CONSTRAINT fk_paquete_camionero FOREIGN KEY (dni_camionero) 
        REFERENCES camionero(dni) 
        ON UPDATE CASCADE 
        ON DELETE RESTRICT
) ENGINE=InnoDB;

-- --------------------------------------------------------
-- 4. Tabla: camionero_camion (Relación Conduce M:N)
-- --------------------------------------------------------
CREATE TABLE camionero_camion (
    id INT AUTO_INCREMENT NOT NULL,
    dni_camionero VARCHAR(20) NOT NULL,
    matricula_camion VARCHAR(20) NOT NULL,
    fecha DATE NOT NULL,
    CONSTRAINT pk_camionero_camion PRIMARY KEY (id),
    CONSTRAINT fk_conduce_camionero FOREIGN KEY (dni_camionero) 
        REFERENCES camionero(dni) 
        ON UPDATE CASCADE 
        ON DELETE RESTRICT,
    CONSTRAINT fk_conduce_camion FOREIGN KEY (matricula_camion) 
        REFERENCES camion(matricula) 
        ON UPDATE CASCADE 
        ON DELETE RESTRICT,
    CONSTRAINT uq_camionero_camion_fecha UNIQUE (dni_camionero, matricula_camion, fecha)
) ENGINE=InnoDB;

-- ========================================================
-- DATOS DE PRUEBA INICIALES
-- ========================================================

-- Inserción en camionero
INSERT INTO camionero (dni, nombre, telefono, direccion, salario, poblacion) VALUES
('0801199512345', 'Carlos Mendoza', '98765432', 'Col. Kennedy, Casa 12', 15500.00, 'Tegucigalpa'),
('0501199254321', 'Roberto Gómez', '99887766', 'Bo. Guamilito, Calle 5', 16800.50, 'San Pedro Sula'),
('0101198811223', 'Luis Alberto Morales', '95123456', 'Col. Las Acacias', 14200.00, 'La Ceiba');

-- Inserción en camion
INSERT INTO camion (matricula, modelo, tipo, potencia) VALUES
('HAA-1234', 'Volvo FH16', 'Trailer Frigorífico', 540),
('HAB-5678', 'Scania R500', 'Cisterna', 500),
('HAC-9012', 'Mercedes-Benz Actros', 'Plataforma', 450);

-- Inserción en paquete
INSERT INTO paquete (codigo, descripcion, destinatario, direccion_destinatario, dni_camionero) VALUES
(101, 'Caja de repuestos automotrices', 'Distribuidora San José', 'Blvd. Suyapa, Edif. 4', '0801199512345'),
(102, 'Lote de medicamentos refrigerados', 'Farmacias del Ahorro', 'Av. Circunvalación Norte', '0501199254321'),
(103, 'Electrodomésticos varios', 'Tiendas Jetstereo', 'Centro Comercial Los Próceres', '0801199512345');

-- Inserción en camionero_camion
INSERT INTO camionero_camion (dni_camionero, matricula_camion, fecha) VALUES
('0801199512345', 'HAA-1234', '2026-10-20'),
('0501199254321', 'HAB-5678', '2026-10-21'),
('0101198811223', 'HAC-9012', '2026-10-22'),
('0801199512345', 'HAC-9012', '2026-10-23');
