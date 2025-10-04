# Migrations de Dados

Este diretório contém as migrations para inserção dos dados dos CSVs presentes na pasta `docs`.

## Ordem de Execução

As migrations devem ser executadas na seguinte ordem, respeitando as dependências entre as entidades:

### 1. V1__Create_Tables.sql
- Criação das tabelas e estruturas básicas
- Criação das sequências para geração de IDs
- Criação dos índices

### 2. V2__Insert_Formas_Pagamento.sql
- Inserção dos dados de formas de pagamento
- **Dependência**: Nenhuma (tabela independente)

### 3. V3__Insert_Produtos.sql
- Inserção dos dados de produtos
- **Dependência**: Nenhuma (tabela independente)

### 4. V4__Insert_Clientes.sql
- Inserção dos dados de clientes
- **Dependência**: Nenhuma (tabela independente)

### 5. V5__Insert_All_Vendas.sql
- Inserção completa de todas as 473 vendas do arquivo FatoVendas.csv
- **Dependência**: produtos, clientes, formas_pagamento

## Estrutura dos Dados

### Formas de Pagamento (V2)
- FP01: Dinheiro
- FP02: PIX
- FP03: Cartão Crédito
- FP04: Cartão Débito

### Produtos (V3)
- 79 produtos organizados por categorias:
  - Salgado
  - Assado
  - Vegetariano
  - Salgados Especiais
  - Vegetariano Especiais
  - Lanchinhos
  - Lanchinhos Vegetariano
  - Brigadeiros - tradicional
  - Brigadeiros - Gourmet
  - Doce no copinho
  - Mini Brownies
  - Empadinha Doce
  - Palha Italiana

### Clientes (V4)
- 50 clientes com informações de:
  - Nome
  - Bairro
  - Cidade
  - Tipo (Pessoa Física/Jurídica)

### Vendas (V5)
- 473 vendas com referências para:
  - Produto (FK para produtos.id)
  - Cliente (FK para clientes.id)
  - Forma de Pagamento (FK para formas_pagamento.id)
  - Data, quantidade, preços e receita total
- Período: Setembro 2025, Agosto 2025 e Julho 2025

## Notas Importantes

1. **Codificação**: Os dados foram processados para remover caracteres especiais e acentos que poderiam causar problemas de codificação.

2. **Chaves Estrangeiras**: As vendas fazem JOIN com as tabelas de referência usando os IDs externos (id_produto, id_cliente, id_pagamento) para obter os IDs internos da base de dados.

3. **Timestamps**: Todos os registros são criados com `created_at` e `updated_at` definidos como `CURRENT_TIMESTAMP`.

4. **Status**: Todos os registros são inseridos com `is_active = true`.

<!-- 5. **V5 Migration**: A migration V5 usa uma tabela temporária para processar eficientemente as 473 vendas do arquivo CSV, fazendo JOIN com as tabelas de referência para obter os IDs corretos. -->