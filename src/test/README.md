# Testes do Sistema DataFood

Este diretório contém os testes do sistema DataFood, organizados em dois tipos principais:

## Estrutura de Diretórios

```
src/test/java/com/univesp/pi/s3t20/
├── unit/                           # Testes Unitários
│   ├── ProdutoServiceTest.java
│   ├── ClienteServiceTest.java
│   ├── FormaPagamentoServiceTest.java
│   ├── VendaServiceTest.java
│   └── UnitTestProfile.java
├── integration/                    # Testes de Integração
│   ├── ProdutoResourceIntegrationTest.java
│   ├── ClienteResourceIntegrationTest.java
│   ├── FormaPagamentoResourceIntegrationTest.java
│   ├── VendaResourceIntegrationTest.java
│   └── IntegrationTestProfile.java
└── resources/
    ├── application-test.properties
    └── application-unit.properties
```

## Tipos de Testes

### Testes Unitários (`unit/`)
- **Objetivo**: Testar a lógica de negócio dos services
- **Banco**: H2 em memória (`unittestdb`)
- **Perfil**: `UnitTestProfile`
- **Características**:
  - Testam apenas a camada de serviço
  - Não fazem chamadas HTTP
  - Executam mais rapidamente
  - Isolados e independentes

### Testes de Integração (`integration/`)
- **Objetivo**: Testar os endpoints REST completos
- **Banco**: H2 em memória (`testdb`)
- **Perfil**: `IntegrationTestProfile`
- **Características**:
  - Testam toda a stack (Resource + Service + Model)
  - Fazem chamadas HTTP reais
  - Testam validações e respostas HTTP
  - Simulam cenários reais de uso

## Como Executar os Testes

### Executar Todos os Testes
```bash
mvn test
```

### Executar Apenas Testes Unitários
```bash
mvn test -Dtest="**/unit/*Test"
```

### Executar Apenas Testes de Integração
```bash
mvn test -Dtest="**/integration/*Test"
```

### Executar Testes de uma Entidade Específica
```bash
# Testes unitários do Produto
mvn test -Dtest="**/unit/ProdutoServiceTest"

# Testes de integração do Produto
mvn test -Dtest="**/integration/ProdutoResourceIntegrationTest"
```

### Executar com Relatório de Cobertura
```bash
mvn clean test jacoco:report
```

## Configurações de Teste

### Banco de Dados
- **Testes Unitários**: `jdbc:h2:mem:unittestdb`
- **Testes de Integração**: `jdbc:h2:mem:testdb`
- **Migrações**: Flyway habilitado para ambos
- **Console H2**: Disponível apenas nos testes de integração

### Logs
- **Testes Unitários**: Nível WARN (menos verboso)
- **Testes de Integração**: Nível INFO (mais detalhado)

## Cobertura de Testes

### Testes Unitários (57 testes)
- **ProdutoServiceTest**: 12 testes
- **ClienteServiceTest**: 13 testes
- **FormaPagamentoServiceTest**: 14 testes
- **VendaServiceTest**: 18 testes

### Testes de Integração (42 testes)
- **ProdutoResourceIntegrationTest**: 10 testes
- **ClienteResourceIntegrationTest**: 12 testes
- **FormaPagamentoResourceIntegrationTest**: 12 testes
- **VendaResourceIntegrationTest**: 14 testes

## Cenários Testados

### Testes Unitários
- ✅ CRUD básico (Create, Read, Update, Delete)
- ✅ Validações de negócio
- ✅ Buscas por critérios específicos
- ✅ Tratamento de erros
- ✅ Contadores e estatísticas
- ✅ Métodos de ativação/desativação

### Testes de Integração
- ✅ Endpoints GET, POST, PUT, DELETE
- ✅ Códigos de status HTTP corretos
- ✅ Validação de JSON de entrada e saída
- ✅ Tratamento de erros HTTP
- ✅ Validação de relacionamentos entre entidades
- ✅ Cenários de sucesso e falha

## Dependências de Teste

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-junit5</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>io.restassured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>4.5.1</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.hamcrest</groupId>
    <artifactId>hamcrest</artifactId>
    <version>2.2</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-test-h2</artifactId>
    <scope>test</scope>
</dependency>
```

## Notas Importantes

1. **Isolamento**: Cada teste limpa os dados antes de executar
2. **Ordem**: Os testes usam `@Order` para garantir execução sequencial
3. **Transações**: Testes unitários usam `@Transactional` para rollback automático
4. **Dados de Teste**: Cada teste cria seus próprios dados de teste
5. **Validação**: Testes verificam tanto sucesso quanto cenários de erro
