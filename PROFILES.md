# Profiles de Configuração

Este projeto suporta diferentes profiles de configuração para diferentes ambientes.

## Profiles Disponíveis

### 1. `dev` (Padrão)
- **Banco de dados**: H2 (em memória)
- **Configuração**: `application.properties`
- **Uso**: Desenvolvimento local

### 2. `test`
- **Banco de dados**: H2 (em memória)
- **Configuração**: `application-test.properties`
- **Uso**: Testes unitários

### 3. `integration`
- **Banco de dados**: H2 (em memória)
- **Configuração**: `application-integration.properties`
- **Uso**: Testes de integração

### 4. `prd` (Produção)
- **Banco de dados**: PostgreSQL
- **Configuração**: `application-prd.properties`
- **Uso**: Ambiente de produção

## Como Usar o Profile 'prd'

### 1. Configuração do PostgreSQL

Antes de usar o profile 'prd', certifique-se de que o PostgreSQL esteja instalado e configurado:

```bash
# Criar banco de dados
createdb pi_univesp_prd

# Criar usuário (opcional)
createuser -P pi_user
```

### 2. Variáveis de Ambiente

Configure as seguintes variáveis de ambiente:

```bash
# Windows (PowerShell)
$env:SPRING_PROFILES_ACTIVE="prd"
$env:DB_HOST="localhost"
$env:DB_PORT="5432"
$env:DB_NAME="pi_univesp_prd"
$env:DB_USERNAME="pi_user"
$env:DB_PASSWORD="pi_password"
$env:SERVER_PORT="8080"

# Linux/Mac
export SPRING_PROFILES_ACTIVE=prd
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=pi_univesp_prd
export DB_USERNAME=pi_user
export DB_PASSWORD=pi_password
export SERVER_PORT=8080
```

#### Valores Padrão das Variáveis de Ambiente

| Variável | Valor Padrão | Descrição |
|----------|--------------|-----------|
| `DB_HOST` | `localhost` | Host do servidor PostgreSQL |
| `DB_PORT` | `5432` | Porta do servidor PostgreSQL |
| `DB_NAME` | `pi_univesp_prd` | Nome do banco de dados |
| `DB_USERNAME` | `pi_user` | Usuário do banco de dados |
| `DB_PASSWORD` | `pi_password` | Senha do banco de dados |
| `SERVER_PORT` | `8080` | Porta da aplicação Spring Boot |

#### Exemplos de Configuração para Diferentes Ambientes

**Desenvolvimento Local:**
```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=pi_univesp_dev
export DB_USERNAME=dev_user
export DB_PASSWORD=dev_password
```

**Staging:**
```bash
export DB_HOST=staging-db.company.com
export DB_PORT=5432
export DB_NAME=pi_univesp_staging
export DB_USERNAME=staging_user
export DB_PASSWORD=staging_password
```

**Produção:**
```bash
export DB_HOST=prod-db.company.com
export DB_PORT=5432
export DB_NAME=pi_univesp_prod
export DB_USERNAME=prod_user
export DB_PASSWORD=prod_password
```

### 3. Executar a Aplicação

```bash
# Usando Maven
mvn spring-boot:run -Dspring-boot.run.profiles=prd

# Usando JAR
java -jar target/pi-univesp-2025-2-s3-t20-backend-1.0.0-SNAPSHOT.jar --spring.profiles.active=prd

# Usando variável de ambiente
java -Dspring.profiles.active=prd -jar target/pi-univesp-2025-2-s3-t20-backend-1.0.0-SNAPSHOT.jar
```

### 4. Executar Migrações do Flyway

```bash
# Migrar para PostgreSQL (usando variáveis de ambiente)
mvn flyway:migrate -Dflyway.url=jdbc:postgresql://${DB_HOST:-localhost}:${DB_PORT:-5432}/${DB_NAME:-pi_univesp_prd} -Dflyway.user=${DB_USERNAME:-pi_user} -Dflyway.password=${DB_PASSWORD:-pi_password}

# Ou definindo as variáveis explicitamente
DB_HOST=localhost DB_PORT=5432 DB_NAME=pi_univesp_prd DB_USERNAME=pi_user DB_PASSWORD=pi_password mvn flyway:migrate -Dflyway.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME} -Dflyway.user=${DB_USERNAME} -Dflyway.password=${DB_PASSWORD}
```

## Configurações do Profile 'prd'

O profile 'prd' inclui as seguintes configurações otimizadas para produção:

- **Banco de dados**: PostgreSQL com configurações de pool de conexão
- **Logging**: Níveis de log otimizados para produção
- **Hibernate**: Configurações de performance
- **Flyway**: Validação de migrações habilitada
- **Actuator**: Endpoints de monitoramento configurados

## Estrutura de Arquivos

```
src/main/resources/
├── application.properties          # Configuração padrão (dev)
├── application-prd.properties     # Configuração de produção
└── db/migration/                  # Scripts de migração do Flyway
```

## Troubleshooting

### Erro de Conexão com PostgreSQL
- Verifique se o PostgreSQL está rodando
- Confirme as credenciais de usuário e senha
- Verifique se o banco de dados `pi_univesp_prd` existe

### Erro de Migração do Flyway
- Execute as migrações manualmente antes de iniciar a aplicação
- Verifique se o usuário tem permissões para criar tabelas

### Porta em Uso
- Altere a variável `SERVER_PORT` para uma porta disponível
- Ou pare o processo que está usando a porta 8080
