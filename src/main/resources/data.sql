INSERT INTO usuarios (login, senha)
SELECT 'admin@agendamento.com', '$2a$10$pJpkB119PhpcMKJUw8K7ge2MQmxskb9VP7n3d3xtYCC7lE1iUxAVW'
WHERE NOT EXISTS (
    SELECT 1 FROM usuarios WHERE login = 'admin@agendamento.com'
);
