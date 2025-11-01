UPDATE user_roles SET role_id = (SELECT id FROM roles WHERE name = 'ROLE_READONLY') WHERE user_id = (SELECT id FROM users WHERE username = 'readonly');
