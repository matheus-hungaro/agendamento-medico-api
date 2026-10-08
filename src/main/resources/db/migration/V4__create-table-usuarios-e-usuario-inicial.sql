CREATE TABLE usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    login VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    PRIMARY KEY(id)
);

-- Insere o usuário padrão de teste
-- Login: admin@agendamento.com | Senha: 123456
INSERT INTO usuarios (login, senha)
VALUES ('admin@agendamento.com', '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYftfaf3U3eEaoBWcAgW28g9E8726');
