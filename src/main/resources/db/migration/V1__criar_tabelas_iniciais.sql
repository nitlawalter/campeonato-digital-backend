CREATE TABLE times (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    jogador VARCHAR(100) NOT NULL,
    emblema VARCHAR(255)
);

CREATE TABLE campeonatos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    data_inicio TIMESTAMP NOT NULL,
    data_fim TIMESTAMP NOT NULL,
    numero_grupos INTEGER NOT NULL,
    times_por_grupo INTEGER NOT NULL,
    numero_maximo_times INTEGER NOT NULL,
    status VARCHAR(30) NOT NULL
);

CREATE TABLE fases (
    id BIGSERIAL PRIMARY KEY,
    campeonato_id BIGINT NOT NULL REFERENCES campeonatos(id),
    tipo VARCHAR(20) NOT NULL,
    numero INTEGER NOT NULL
);

CREATE TABLE partidas (
    id BIGSERIAL PRIMARY KEY,
    fase_id BIGINT NOT NULL REFERENCES fases(id),
    time_casa_id BIGINT NOT NULL REFERENCES times(id),
    time_visitante_id BIGINT NOT NULL REFERENCES times(id),
    gols_time_casa INTEGER,
    gols_time_visitante INTEGER,
    data_hora TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE inscricoes (
    id BIGSERIAL PRIMARY KEY,
    campeonato_id BIGINT NOT NULL REFERENCES campeonatos(id),
    time_id BIGINT NOT NULL REFERENCES times(id),
    data_inscricao TIMESTAMP NOT NULL,
    aprovada BOOLEAN NOT NULL,
    UNIQUE(campeonato_id, time_id)
); 