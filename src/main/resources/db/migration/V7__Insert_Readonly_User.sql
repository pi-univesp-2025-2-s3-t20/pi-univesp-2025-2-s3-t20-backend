-- Inserir usuário somente leitura com senha 'password' codificada em BCrypt
INSERT INTO users (name, username, email, password) VALUES ('Usuário Somente Leitura', 'readonly', 'readonly@univesp.br', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1c.lu6asSGPoe');

-- Associar o usuário à role ROLE_USER
INSERT INTO user_roles (user_id, role_id) VALUES ((SELECT id FROM users WHERE username = 'readonly'), (SELECT id FROM roles WHERE name = 'ROLE_USER'));
