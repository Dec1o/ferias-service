# Documentação - Ferias Service API

## 📋 Visão Geral

Sistema de gerenciamento de férias para servidores públicos, com controle de solicitações, aprovações e cálculo automático de pagamentos.

## 🚀 Tecnologias

- **Java 17** + **Spring Boot 4.0.0**
- **PostgreSQL** (banco de dados)
- **JWT** (autenticação)
- **Flyway** (migração de dados)
- **Docker** (containerização)

## 📦 Pré-requisitos

- Java 17+
- Docker & Docker Compose
- Maven 3.9+

## ⚙️ Configuração e Execução

### Executar Localmente

#### 1. Configurar o Banco de Dados PostgreSQL

**Opção A - Usando Docker (recomendado)**
```bash
docker-compose -f docker-compose-postgres.yml up -d
```

**Opção B - PostgreSQL instalado localmente**
```sql
CREATE DATABASE nome_db;
CREATE USER user WITH PASSWORD 'senha';
GRANT ALL PRIVILEGES ON DATABASE nome_db TO user;
```

#### 2. Configurar Variáveis de Ambiente

Edite o arquivo `src/main/resources/application.properties` com suas credenciais:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/nome_db
spring.datasource.username=user
spring.datasource.password=senha
jwt.secret=QzVxNnJtT0l2eVZac3BJSnAwb3h4M2ZsS3J0RjlFQ0tWeXd2SGFsdA==
jwt.expiration=3600000
```

#### 3. Executar a Aplicação

```bash
# Linux/Mac
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

#### 4. Verificar se está funcionando

Acesse: `http://localhost:8080/actuator/health`

Resposta esperada: `{"status":"UP"}`

---

### Executar com Docker

#### 1. Subir o Banco de Dados
```bash
docker-compose -f docker-compose-postgres.yml up -d
```

#### 2. Subir a Aplicação
```bash
docker-compose -f docker-compose-app.yml up -d
```

#### 3. Verificar logs
```bash
# Logs do banco
docker logs ferias-postgres

# Logs da aplicação
docker logs ferias-api
```

#### 4. Parar os serviços
```bash
docker-compose -f docker-compose-app.yml down
docker-compose -f docker-compose-postgres.yml down
```

## 🔐 Autenticação

Todas as rotas (exceto `/auth/**`) requerem token JWT no header:

```
Authorization: Bearer {token}
```

## 📡 Endpoints Principais

### Autenticação

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/auth/register` | Cadastrar servidor |
| POST | `/auth/login` | Fazer login |

### Servidores

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/servidores` | Listar todos |
| GET | `/servidores/{id}` | Buscar por ID |
| POST | `/servidores` | Criar servidor |
| PUT | `/servidores/{id}` | Atualizar |
| DELETE | `/servidores/{id}` | Deletar |

### Férias

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/ferias` | Listar todas |
| GET | `/ferias/{id}` | Buscar por ID |
| GET | `/ferias/servidor/{id}` | Férias de um servidor |
| POST | `/ferias` | Criar solicitação |
| PUT | `/ferias/{id}` | Atualizar |
| DELETE | `/ferias/{id}` | Deletar |

### Status

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/status` | Listar status (PENDENTE, APROVADO, REPROVADO) |

## 🔒 Segurança

### Autenticação JWT
- **Token Base**: HS256 com secret em Base64
- **Expiração**: Configurável via `JWT_EXPIRATION` (padrão: 1 hora)
- **Validação**: Filter customizado (`JwtAuthenticationFilter`) valida token em cada requisição
- **Stateless**: Sessões não são mantidas no servidor

### Proteção de Senhas
- **Encoding**: BCrypt para hash de senhas
- **Salt**: Gerado automaticamente pelo BCrypt
- **Senhas**: Nunca retornadas nas respostas da API

### Configurações Spring Security
- **CSRF**: Desabilitado (API stateless)
- **Session Management**: STATELESS
- **Rotas Públicas**: `/auth/**`, `/swagger-ui/**`, `/v3/api-docs/**`
- **Rotas Protegidas**: Todas as demais requerem autenticação

### Isolamento de Containers
- Aplicação roda como usuário não-root (`spring:spring`)
- Imagem otimizada com Alpine Linux (superfície de ataque reduzida)

## 🏗️ Arquitetura

### Estrutura em Camadas

```
Controller (API REST)
    ↓
Service (Lógica de Negócio)
    ↓
Repository (Acesso a Dados)
    ↓
Database (PostgreSQL)
```

### Componentes Principais

**Controllers**: Gerenciam requisições HTTP e respostas
- Validação de entrada
- Mapeamento de rotas
- Delegação para services

**Services**: Contêm regras de negócio
- Validações complexas
- Cálculos (pagamento de férias)
- Orquestração de operações

**Repositories**: Interface com banco de dados
- Spring Data JPA
- Queries customizadas quando necessário

**DTOs**: Transferência de dados entre camadas
- Separação entre entidades e contratos de API
- Controle de exposição de dados sensíveis

### Persistência

**Flyway**: Controle de versão do schema
- Migrações versionadas (`V1__`, `V2__`, etc.)
- Rastreabilidade de mudanças no banco
- Baseline automático em migração

**JPA/Hibernate**: Mapeamento objeto-relacional
- Lazy loading para relacionamentos
- Transações gerenciadas por anotações
- Dialect específico para PostgreSQL

## 🎨 Padrões de Projeto

### Dependency Injection
- **Implementação**: Constructor injection via Lombok `@RequiredArgsConstructor`
- **Benefícios**: Testabilidade, baixo acoplamento, imutabilidade

### DTO Pattern
- **Separação**: Entidades de domínio vs. contratos de API
- **Conversão**: Métodos `toDTO()` nos services
- **Segurança**: Campos sensíveis não expostos (ex: senha)

### Repository Pattern
- **Abstração**: Spring Data JPA oculta complexidade SQL
- **Queries**: Métodos derivados de nomes (`findByEmail`, `existsByEmail`)
- **Customização**: Queries JPQL quando necessário

### Service Layer
- **Transações**: `@Transactional` para operações atômicas
- **ReadOnly**: Otimização para consultas
- **Validações**: BusinessException para regras de negócio

### Exception Handling
- **Customizadas**: `ResourceNotFoundException`, `BusinessException`
- **Propagação**: RuntimeException para rollback automático
- **Separação**: Erros de negócio vs. erros técnicos

### Filter Pattern
- **JWT Filter**: `OncePerRequestFilter` para autenticação
- **Ordem**: Executado antes de `UsernamePasswordAuthenticationFilter`
- **Bypass**: Rotas públicas ignoradas no filtro

### Builder Pattern (Implícito)
- **Lombok**: `@Data`, `@Builder` para construtores fluentes
- **JPA**: Entidades com getters/setters automáticos

## 💡 Regras de Negócio

- **Cálculo de Pagamento**: Salário + 1/3 do salário
- **Status Inicial**: Toda solicitação inicia como PENDENTE
- **Validações**:
  - Data fim ≥ data início
  - Não permite sobreposição de períodos
  - Apenas solicitações PENDENTES podem ser alteradas
  - Não é possível deletar férias APROVADAS

## 📚 Documentação API

Acesse o Swagger após subir a aplicação:
```
http://localhost:8080/swagger-ui.html
```

## 🏥 Health Check

```
http://localhost:8080/actuator/health
```

## 🗄️ Estrutura do Banco

**Tabelas:**
- `servidores` - Dados dos servidores
- `ferias` - Solicitações de férias
- `status` - Status das solicitações (PENDENTE, APROVADO, REPROVADO)

Migrações gerenciadas pelo Flyway em `src/main/resources/db/migration/`

## 🔧 Collection Postman

Importe o arquivo `Ferias Service API.postman_collection.json` no Postman para testar os endpoints.
