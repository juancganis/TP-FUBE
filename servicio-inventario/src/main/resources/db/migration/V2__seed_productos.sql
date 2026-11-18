INSERT INTO productos (sku,  nombre,  cantidad_disponible) VALUES
('REM-NE-XS', 'Remera - Negra - Talle XS', 15),
('REM-NE-S', 'Remera - Negra - Talle S', 25),
('REM-NE-M', 'Remera - Negra - Talle M', 40),
('REM-NE-L', 'Remera - Negra - Talle L', 35),
('REM-NE-XL', 'Remera - Negra - Talle XL', 20),
('REM-NE-XXL', 'Remera - Negra - Talle XXL', 10),
('REM-BL-XS', 'Remera - Blanca - Talle XS', 18),
('REM-BL-S', 'Remera - Blanca - Talle S', 30),
('REM-BL-M', 'Remera - Blanca - Talle M', 50),
('REM-BL-L', 'Remera - Blanca - Talle L', 45),
('REM-BL-XL', 'Remera - Blanca - Talle XL', 22),
('REM-BL-XXL', 'Remera - Blanca - Talle XXL', 12),
('REM-AZ-XS', 'Remera - Azul - Talle XS', 10),
('REM-AZ-S', 'Remera - Azul - Talle S', 20),
('REM-AZ-M', 'Remera - Azul - Talle M', 1),
('REM-AZ-L', 'Remera - Azul - Talle L', 28),
('REM-AZ-XL', 'Remera - Azul - Talle XL', 15),
('REM-AZ-XXL', 'Remera - Azul - Talle XXL', 8),
('REM-RO-XS', 'Remera - Roja - Talle XS', 8),
('REM-RO-S', 'Remera - Roja - Talle S', 15),
('REM-RO-M', 'Remera - Roja - Talle M', 25),
('REM-RO-L', 'Remera - Roja - Talle L', 20),
('REM-RO-XL', 'Remera - Roja - Talle XL', 12),
('REM-RO-XXL', 'Remera - Roja - Talle XXL', 5),
('REM-VE-XS', 'Remera - Verde - Talle XS', 0),
('REM-VE-S', 'Remera - Verde - Talle S', 18),
('REM-VE-M', 'Remera - Verde - Talle M', 30),
('REM-VE-L', 'Remera - Verde - Talle L', 22),
('REM-VE-XL', 'Remera - Verde - Talle XL', 14),
('REM-VE-XXL', 'Remera - Verde - Talle XXL', 6);

-- Sincroniza la secuencia de IDs de productos con el ID más alto existente, evitando conflictos con los próximos IDs generados automáticamente.
SELECT setval(
    pg_get_serial_sequence('productos','id'),
    (SELECT MAX(id) FROM productos)
);