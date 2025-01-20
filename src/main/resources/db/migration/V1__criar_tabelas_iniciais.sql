-- Tabela de Usuários
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Times
CREATE TABLE times (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Campeonatos
CREATE TABLE campeonatos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    data_inicio DATE,
    data_fim DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Fases
CREATE TABLE fases (
    id BIGSERIAL PRIMARY KEY,
    campeonato_id BIGINT REFERENCES campeonatos(id),
    tipo VARCHAR(50) NOT NULL,
    ordem INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Partidas
CREATE TABLE partidas (
    id BIGSERIAL PRIMARY KEY,
    fase_id BIGINT REFERENCES fases(id),
    time_casa_id BIGINT REFERENCES times(id),
    time_visitante_id BIGINT REFERENCES times(id),
    gols_time_casa INT,
    gols_time_visitante INT,
    status VARCHAR(50) NOT NULL,
    data_realizacao TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Inscrições
CREATE TABLE inscricoes (
    id BIGSERIAL PRIMARY KEY,
    campeonato_id BIGINT REFERENCES campeonatos(id),
    time_id BIGINT REFERENCES times(id),
    status VARCHAR(50) NOT NULL,
    data_inscricao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(campeonato_id, time_id)
); 