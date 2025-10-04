# pi-univesp-2025-2-s3-t20-backend

Este projeto utiliza Spring Boot, um framework Java popular para construção de aplicações web e microsserviços.

Se você quiser aprender mais sobre Spring Boot, visite o site oficial: <https://spring.io/projects/spring-boot>.

## Executando a aplicação em modo de desenvolvimento

Você pode executar sua aplicação em modo de desenvolvimento usando:

```shell script
./mvnw spring-boot:run
```

Ou você pode executar a classe principal diretamente:

```shell script
./mvnw compile exec:java -Dexec.mainClass="com.univesp.pi.s3t20.PiUnivespApplication"
```

## Empacotando e executando a aplicação

A aplicação pode ser empacotada usando:

```shell script
./mvnw clean package
```

Isso produz o arquivo `pi-univesp-2025-2-s3-t20-backend-1.0.0-SNAPSHOT.jar` no diretório `target/`.

A aplicação agora pode ser executada usando `java -jar target/pi-univesp-2025-2-s3-t20-backend-1.0.0-SNAPSHOT.jar`.

## Executando com Docker

Você pode construir e executar a aplicação usando Docker:

```shell script
# Construir a aplicação
./mvnw clean package

# Construir a imagem Docker
docker build -f src/main/docker/Dockerfile.spring -t spring/pi-univesp-2025-2-s3-t20-backend .

# Executar o container
docker run -i --rm -p 8080:8080 spring/pi-univesp-2025-2-s3-t20-backend
```

## Documentação da API

A aplicação fornece documentação completa da API usando OpenAPI 3.0 (Swagger):

### Acesso à Documentação
- **Swagger UI**: <http://localhost:8080/swagger-ui.html>
- **API Docs (JSON)**: <http://localhost:8080/api-docs>
- **API Docs (YAML)**: <http://localhost:8080/api-docs.yaml>

### Funcionalidades da Documentação
- **Interface Interativa**: Teste os endpoints diretamente no navegador
- **Documentação Detalhada**: Descrições completas de todos os endpoints
- **Modelos de Dados**: Esquemas detalhados das entidades
- **Códigos de Resposta**: Documentação de todos os possíveis retornos
- **Exemplos**: Exemplos de requisições e respostas
- **Filtros e Busca**: Organize e encontre endpoints facilmente

### Recursos Documentados
- **Clientes**: Gerenciamento completo de clientes
- **Produtos**: Catálogo de produtos com categorias
- **Formas de Pagamento**: Métodos de pagamento disponíveis
- **Vendas**: Sistema completo de vendas com relatórios

## Console do Banco de Dados

O console do banco de dados H2 está disponível em:
- H2 Console: <http://localhost:8080/h2-console>

## Executando os Testes

Para executar os testes unitários e de integração:

```shell script
# Executar todos os testes
./mvnw test

# Executar apenas testes unitários
./mvnw test -Dtest="*Unit*"

# Executar apenas testes de integração
./mvnw test -Dtest="*Integration*"
```

## Funcionalidades

- **Spring Boot 3.2.0** - Framework Java moderno
- **Spring Data JPA** - Persistência de dados com Hibernate
- **Banco de Dados H2** - Banco de dados em memória para desenvolvimento
- **Flyway** - Ferramenta de migração de banco de dados
- **SpringDoc OpenAPI** - Documentação da API com Swagger UI
- **Spring Boot Actuator** - Monitoramento e gerenciamento da aplicação
- **Suporte ao Docker** - Implantação containerizada
- **Testes Automatizados** - Testes unitários e de integração com Spring Boot Test

## Estrutura do Projeto

- `src/main/java/com/univesp/pi/s3t20/` - Código principal da aplicação
  - `model/` - Entidades JPA
  - `repository/` - Repositórios Spring Data JPA
  - `service/` - Serviços de lógica de negócio
  - `resource/` - Controladores REST
- `src/main/resources/` - Arquivos de configuração
  - `application.properties` - Configuração da aplicação
  - `db/migration/` - Migrações do banco de dados Flyway
- `src/test/` - Código de teste
  - `unit/` - Testes unitários
  - `integration/` - Testes de integração

## Endpoints da API

### Clientes
- `GET /api/v1/clientes` - Listar todos os clientes
- `GET /api/v1/clientes/{id}` - Buscar cliente por ID
- `POST /api/v1/clientes` - Criar novo cliente
- `PUT /api/v1/clientes/{id}` - Atualizar cliente
- `DELETE /api/v1/clientes/{id}` - Deletar cliente
- `GET /api/v1/clientes/cidade/{cidade}` - Buscar clientes por cidade
- `GET /api/v1/clientes/count` - Contar clientes

### Produtos
- `GET /api/v1/produtos` - Listar todos os produtos
- `GET /api/v1/produtos/{id}` - Buscar produto por ID
- `POST /api/v1/produtos` - Criar novo produto
- `PUT /api/v1/produtos/{id}` - Atualizar produto
- `DELETE /api/v1/produtos/{id}` - Deletar produto

### Formas de Pagamento
- `GET /api/v1/formas-pagamento` - Listar todas as formas de pagamento
- `GET /api/v1/formas-pagamento/{id}` - Buscar forma de pagamento por ID
- `POST /api/v1/formas-pagamento` - Criar nova forma de pagamento
- `PUT /api/v1/formas-pagamento/{id}` - Atualizar forma de pagamento
- `DELETE /api/v1/formas-pagamento/{id}` - Deletar forma de pagamento

### Vendas
- `GET /api/v1/vendas` - Listar todas as vendas
- `GET /api/v1/vendas/{id}` - Buscar venda por ID
- `POST /api/v1/vendas` - Criar nova venda
- `PUT /api/v1/vendas/{id}` - Atualizar venda
- `DELETE /api/v1/vendas/{id}` - Deletar venda
- `GET /api/v1/vendas/resumo` - Obter resumo das vendas

## Configuração

A aplicação utiliza as seguintes configurações padrão:

- **Porta:** 8080
- **Banco de Dados:** H2 (em memória)
- **Perfil:** development (padrão)
- **Logs:** Console

Para alterar as configurações, edite o arquivo `src/main/resources/application.properties`.
