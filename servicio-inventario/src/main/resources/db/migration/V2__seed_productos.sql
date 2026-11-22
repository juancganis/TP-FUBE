INSERT INTO productos (sku, nombre, cantidad_disponible, precio_unitario) VALUES
('REM-NE-XS', 'Remera - Negra - Talle XS', 15, 12000.00),
('REM-NE-S', 'Remera - Negra - Talle S', 25, 10666.67),
('REM-NE-M', 'Remera - Negra - Talle M', 40, 15000.00),
('REM-NE-L', 'Remera - Negra - Talle L', 35, 14000.00),
('REM-NE-XL', 'Remera - Negra - Talle XL', 20, 16000.00),
('REM-NE-XXL', 'Remera - Negra - Talle XXL', 10, 18000.00),
('REM-BL-XS', 'Remera - Blanca - Talle XS', 18, 12500.00),
('REM-BL-S', 'Remera - Blanca - Talle S', 30, 14000.00),
('REM-BL-M', 'Remera - Blanca - Talle M', 50, 12000.00),
('REM-BL-L', 'Remera - Blanca - Talle L', 45, 15000.00),
('REM-BL-XL', 'Remera - Blanca - Talle XL', 22, 15000.00),
('REM-BL-XXL', 'Remera - Blanca - Talle XXL', 12, 14500.00),
('REM-AZ-XS', 'Remera - Azul - Talle XS', 10, 22000.00),
('REM-AZ-S', 'Remera - Azul - Talle S', 20, 14000.00),
('REM-AZ-M', 'Remera - Azul - Talle M', 1, 15000.00),
('REM-AZ-L', 'Remera - Azul - Talle L', 28, 17000.00),
('REM-AZ-XL', 'Remera - Azul - Talle XL', 15, 18500.00),
('REM-AZ-XXL', 'Remera - Azul - Talle XXL', 8, 13000.00),
('REM-RO-XS', 'Remera - Roja - Talle XS', 8, 12000.00),
('REM-RO-S', 'Remera - Roja - Talle S', 15, 13000.00),
('REM-RO-M', 'Remera - Roja - Talle M', 25, 12500.00),
('REM-RO-L', 'Remera - Roja - Talle L', 20, 13500.00),
('REM-RO-XL', 'Remera - Roja - Talle XL', 12, 15500.00),
('REM-RO-XXL', 'Remera - Roja - Talle XXL', 5, 14000.00),
('REM-VE-XS', 'Remera - Verde - Talle XS', 0, 31500.00),
('REM-VE-S', 'Remera - Verde - Talle S', 18, 13000.00),
('REM-VE-M', 'Remera - Verde - Talle M', 30, 14000.00),
('REM-VE-L', 'Remera - Verde - Talle L', 22, 13000.00),
('REM-VE-XL', 'Remera - Verde - Talle XL', 14, 16500.00),
('REM-VE-XXL', 'Remera - Verde - Talle XXL', 6, 15500.00);

-- Sincroniza la secuencia de IDs de productos con el ID más alto existente, evitando conflictos con los próximos IDs generados automáticamente.
SELECT setval(
    pg_get_serial_sequence('productos','id'),
    (SELECT MAX(id) FROM productos)
);