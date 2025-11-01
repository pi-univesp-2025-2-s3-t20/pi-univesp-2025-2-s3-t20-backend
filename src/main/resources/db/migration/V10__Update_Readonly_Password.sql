-- Atualiza a senha do usuário readonly para um novo hash BCrypt da senha 'password'
UPDATE users SET password = '$2a$10$N0myi2M2U/3/yL/ZCVt2d.e509w2iV3d2Ld.a.3z.I.yG.Nq.l/mS' WHERE username = 'readonly';
