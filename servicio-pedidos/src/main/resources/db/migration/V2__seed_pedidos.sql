INSERT INTO pedidos (id, cliente_id, estado, monto_total, fecha_creacion, fecha_actualizacion) VALUES
-- 10 pedidos creados
(1, 1, 'CREADO', 45000.00, '2026-01-15 09:00:00'::timestamp, '2026-01-15 09:00:00'::timestamp),
(2, 2, 'CREADO', 32000.00, '2026-01-07 14:00:00'::timestamp, '2026-01-07 14:00:00'::timestamp),
(3, 3, 'CREADO', 18500.00, '2026-01-11 10:00:00'::timestamp, '2026-01-11 10:00:00'::timestamp),
(4, 4, 'CREADO', 25000.00, NOW(), NOW()),
(5, 5, 'CREADO', 60000.00, NOW(), NOW()),
(6, 6, 'CREADO', 42000.00, NOW(), NOW()),
(7, 7, 'CREADO', 15000.00, NOW(), NOW()),
(8, 8, 'CREADO', 52000.00, NOW(), NOW()),
(9, 9, 'CREADO', 28000.00, NOW(), NOW()),
(10, 10, 'CREADO', 16000.00, NOW(), NOW()),

-- 10 pedidos confirmados
(11, 11, 'CONFIRMADO', 39000.00, NOW(), NOW()),
(12, 12, 'CONFIRMADO', 27000.00, NOW(), NOW()),
(13, 13, 'CONFIRMADO', 14000.00, NOW(), NOW()),
(14, 14, 'CONFIRMADO', 30000.00, NOW(), NOW()),
(15, 15, 'CONFIRMADO', 18000.00, NOW(), NOW()),
(16, 16, 'CONFIRMADO', 31000.00, NOW(), NOW()),
(17, 17, 'CONFIRMADO', 16500.00, NOW(), NOW()),
(18, 18, 'CONFIRMADO', 29000.00, NOW(), NOW()),
(29, 19, 'CONFIRMADO', 45000.00, NOW(), NOW()),
(30, 20, 'CONFIRMADO', 22000.00, NOW(), NOW()),

-- 10 pedidos cancelados
(19, 21, 'CANCELADO', 15000.00, '2026-01-12 19:00:00'::timestamp, '2026-01-12 19:30:00'::timestamp),
(20, 22, 'CANCELADO', 28000.00, '2026-01-05 15:30:00'::timestamp, '2026-01-05 16:00:00'::timestamp),
(21, 23, 'CANCELADO', 17000.00, NOW(), NOW()),
(22, 24, 'CANCELADO', 36000.00, NOW(), NOW()),
(23, 25, 'CANCELADO', 13000.00, NOW(), NOW()),
(24, 26, 'CANCELADO', 24000.00, NOW(), NOW()),
(25, 27, 'CANCELADO', 12500.00, NOW(), NOW()),
(26, 28, 'CANCELADO', 26000.00, NOW(), NOW()),
(31, 29, 'CANCELADO', 19000.00, NOW(), NOW()),
(32, 30, 'CANCELADO', 41000.00, NOW(), NOW()),

-- 10 pedidos expirados
(27, 31, 'EXPIRADO', 14000.00, NOW(), NOW()),
(28, 32, 'EXPIRADO', 15500.00, NOW(), NOW()),
(33, 33, 'EXPIRADO', 21000.00, NOW(), NOW()),
(34, 34, 'EXPIRADO', 33000.00, NOW(), NOW()),
(35, 35, 'EXPIRADO', 17500.00, NOW(), NOW()),
(36, 36, 'EXPIRADO', 28500.00, NOW(), NOW()),
(37, 37, 'EXPIRADO', 12000.00, NOW(), NOW()),
(38, 38, 'EXPIRADO', 35000.00, NOW(), NOW()),
(39, 39, 'EXPIRADO', 22500.00, NOW(), NOW()),
(40, 40, 'EXPIRADO', 19500.00, NOW(), NOW());

-- Sincroniza la secuencia de IDs de pedidos con el ID más alto existente, evitando conflictos con los próximos IDs generados automáticamente.
SELECT setval(
    pg_get_serial_sequence('pedidos','id'),
    (SELECT MAX(id) FROM pedidos)
);