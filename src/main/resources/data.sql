INSERT INTO persona (nombre, genero, edad, identificacion, direccion, telefono) VALUES 
('Jose Lema', 'Masculino', 30, '12345678A', 'Otavalo sn y principal', '98254785'),
('Marianela Montalvo', 'Femenino', 25, '87654321B', 'Amazonas y NNUU', '97548965'),
('Juan Osorio', 'Masculino', 45, '87654321C', '13 junio y Equinoccial', '98874587');

INSERT INTO cliente (contrasena, estado, persona_id) VALUES 
('1234', true, 1),
('5678', true, 2),
('1245', true, 3);

INSERT INTO cuenta (numero_cuenta, tipo_cuenta, saldo_inicial, estado, cliente_id, limite_diario_retiro) VALUES
('478758', 'Ahorros', 2000, true, 1, 1000),
('225487', 'Corriente', 100, true, 2, 1000),
('495878', 'Ahorros', 0, true, 3, 1000),
('496825', 'Ahorros', 540, true, 2, 1000);
