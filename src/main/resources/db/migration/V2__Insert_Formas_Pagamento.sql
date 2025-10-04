-- Inserção dos dados de formas de pagamento
-- V2__Insert_Formas_Pagamento.sql

INSERT INTO formas_pagamento (id_pagamento, forma_pagamento, is_active, created_at, updated_at) VALUES
('FP01', 'Dinheiro', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('FP02', 'PIX', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('FP03', 'Cartão Crédito', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('FP04', 'Cartão Débito', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
