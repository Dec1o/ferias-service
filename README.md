## 🧑‍💼 História de Usuário — Problema e Contexto

**João** é servidor público e todos os anos tira férias. Porém, atualmente ele não consegue visualizar de forma organizada quais períodos já tirou, quando irá tirar novamente, nem os pagamentos relacionados às férias. Ele costuma acessar vários sistemas internos diferentes, enviar e-mails para o RH e às vezes precisa pedir comprovantes de pagamentos manualmente.

Ele gostaria de ter uma forma simples de consultar seus períodos de férias, com datas e status de aprovação, e visualizar dados básicos dos pagamentos associados a esses períodos.

O sistema que foi desenvolvido é um **protótipo mínimo**, voltado exclusivamente a esse objetivo principal.

# Estrutura do Banco de Dados
<img width="608" height="508" alt="diagram-export-13-12-2025-08_25_51" src="https://github.com/user-attachments/assets/32bf4e1a-d69e-401e-941b-040fbf80b109" />

# Protótipo de baixa fidelidade
<img width="4149" height="1740" alt="excalidraw_01" src="https://github.com/user-attachments/assets/aa84b846-bffb-4715-ac0b-0764605f62c4" />

# Documentação - Ferias Service API

## 📋 Visão Geral

Sistema de gerenciamento de férias para servidores públicos, com controle de solicitações, aprovações e cálculo automático de pagamentos.

---

## 🚀 Tecnologias
# Stack Tecnológico - Ferias Service API

| Categoria | Tecnologia | Versão | Descrição |
|-----------|------------|--------|-----------|
| **Linguagem** | Java | 17 | Linguagem de programação |
| **Framework** | Spring Boot | 4.0.0 | Framework principal |
| **Build** | Maven | 3.9+ | Gerenciador de dependências |
| **Banco de Dados** | PostgreSQL | 17 | Banco relacional |
| **Migração** | Flyway | (Spring Boot) | Versionamento de schema |
| **ORM** | Hibernate/JPA | (Spring Boot) | Mapeamento objeto-relacional |
| **Segurança** | Spring Security | (Spring Boot) | Autenticação e autorização |
| **JWT** | JJWT | 0.11.5 | Tokens de autenticação |
| **Validação** | Bean Validation | (Spring Boot) | Validação de dados |
| **Documentação** | SpringDoc OpenAPI | 2.5.0 | Swagger UI / OpenAPI 3.0 |
| **Monitoramento** | Spring Actuator | (Spring Boot) | Health checks e métricas |
| **Desenvolvimento** | Spring DevTools | (Spring Boot) | Hot reload |
| **Produtividade** | Lombok | (Spring Boot) | Redução de boilerplate |
| **Containerização** | Docker | Latest | Containers da aplicação |
| **Orquestração** | Docker Compose | Latest | Gerenciamento de containers |
| **Imagem Base** | Alpine Linux | Latest | Imagem JRE otimizada |

---

## 📦 Pré-requisitos

- Java 17+
- Docker & Docker Compose
- Maven 3.9+

---

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

---

## 🔐 Autenticação

Todas as rotas (exceto `/auth/**`) requerem token JWT no header:

```
Authorization: Bearer {token}
```

---

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

---

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

---

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

---

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

---

## 💡 Regras de Negócio

- **Cálculo de Pagamento**: Salário + 1/3 do salário
- **Status Inicial**: Toda solicitação inicia como PENDENTE
- **Validações**:
  - Data fim ≥ data início
  - Não permite sobreposição de períodos
  - Apenas solicitações PENDENTES podem ser alteradas
  - Não é possível deletar férias APROVADAS

---

## 🗄️ Estrutura do Banco de Dados

### Visão Geral

O sistema utiliza três tabelas principais: **servidores**, **ferias** e **status**, sendo suficiente para representar o servidor, seus períodos de férias, o status da solicitação e as informações básicas de pagamento.

### Tabelas

**`servidores`** - Cadastro dos servidores
- Identificador único, nome, email, senha (hash BCrypt)
- Salário base e data de criação
- Um servidor pode ter várias solicitações de férias

**`ferias`** - Períodos de férias solicitados
- Datas de início e fim, quantidade de dias
- Valor do pagamento (salário + 1/3 constitucional)
- Observações e status da solicitação
- Cada registro vinculado a um servidor e um status

**`status`** - Situação das solicitações
- Valores possíveis: PENDENTE, APROVADO, REPROVADO
- Um status pode estar em várias solicitações

### Diagrama de Relacionamentos

```
servidores (1) ────────< (N) ferias (N) >──────── (1) status
     │                         │                        │
     │                         │                        │
  id (PK)                  servidor_id (FK)          id (PK)
  nome                     status_id (FK)            nome
  email                    data_inicio
  senha                    data_fim
  pagamento                dias
  created_at               pag_ferias
                           observacao
```

### Migrações

Gerenciadas pelo Flyway em `src/main/resources/db/migration/`:

1. **V1__create_servidores.sql** - Cria tabela de servidores
2. **V2__create_status.sql** - Cria tabela de status
3. **V3__create_ferias.sql** - Cria tabela de férias com FKs
4. **V4__insert_default_status.sql** - Insere status iniciais (PENDENTE, APROVADO, REPROVADO)

---

## 📚 Documentação API

Acesse o Swagger após subir a aplicação:
```
http://localhost:8080/swagger-ui.html
```

---

## 🏥 Health Check

```
http://localhost:8080/actuator/health
```

---

## 🔧 Collection Postman

Importe o arquivo `Ferias Service API.postman_collection.json` no Postman para testar os endpoints.
