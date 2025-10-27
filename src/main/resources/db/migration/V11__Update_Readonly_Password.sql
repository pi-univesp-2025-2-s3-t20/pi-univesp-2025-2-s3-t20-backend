-- Atualiza a senha do usuário readonly para um novo hash BCrypt da senha 'password'
UPDATE users SET password = '$2b$10$/N0VG94kvS5MlPKQehU8..1MbbjrR.YwPzF49FAFMFJ2K6YQ8zliW' WHERE username = 'readonly';
