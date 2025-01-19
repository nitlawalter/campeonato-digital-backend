CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Criar um usuário admin inicial (senha: admin123)
INSERT INTO usuarios (email, senha, role) 
VALUES ('admin@torneios.com', '$2a$10$5PxcGR3KEI0iJ.A/ZQpWPOQI8.KxXFLF3iQHt5q4eqTt/ZWgJwXgC', 'ADMIN'); 