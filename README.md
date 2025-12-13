# Documentação - Ferias Service API

## 📋 Visão Geral

Sistema de gerenciamento de férias para servidores públicos, com controle de solicitações, aprovações e cálculo automático de pagamentos.

**Versão**: 0.0.1-SNAPSHOT  
**Grupo**: br.gov.servidor  
**Artefato**: ferias-service

---

## 🚀 Tecnologias

- **Java 17** + **Spring Boot 4.0.0**
- **PostgreSQL** (banco de dados)
- **JWT** (autenticação via JJWT 0.11.5)
- **Flyway** (migração de dados)
- **Docker** (containerização)
- **Lombok** (redução de boilerplate)
- **SpringDoc OpenAPI** (documentação API)
- **Spring Security** (autenticação e autorização)
- **Spring Data JPA** (persistência)

---

## 📦 Pré-requisitos

- **Java 17+**
- **Docker & Docker Compose**
- **Maven 3.9+**
- **PostgreSQL 17** (ou usar via Docker)

---

## ⚙️ Configuração

### Variáveis de Ambiente

```properties
# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/nome_db
SPRING_DATASOURCE_USERNAME=user
SPRING_DATASOURCE_PASSWORD=senha

# JPA/Hibernate
SPRING_JPA_HIBERNATE_DDL_AUTO=none
SPRING_JPA_SHOW_SQL=true

# Flyway
SPRING_FLYWAY_ENABLED=true
SPRING_FLYWAY_BASELINE_ON_MIGRATE=true

# Server
SERVER_PORT=8080

# JWT
JWT_SECRET=QzVxNnJtT0l2eVZac3BJSnAwb3h4M2ZsS3J0RjlFQ0tWeXd2SGFsdA==
JWT_EXPIRATION=3600000

# Actuator
management.endpoints.web.exposure.include=health,info
```

### Executar com Docker

#### 1. Subir banco de dados
```bash
docker-compose -f docker-compose-postgres.yml up -d
```

#### 2. Subir aplicação
```bash
docker-compose -f docker-compose-app.yml up -d
```

#### 3. Verificar logs
```bash
docker logs -f ferias-api
```

#### 4. Parar serviços
```bash
docker-compose -f docker-compose-app.yml down
docker-compose -f docker-compose-postgres.yml down
```

### Executar localmente

```bash
# Compilar o projeto
./mvnw clean package -DskipTests

# Executar aplicação
./mvnw spring-boot:run

# Ou executar o JAR diretamente
java -jar target/ferias-service-0.0.1-SNAPSHOT.jar
```

---

## 🔐 Autenticação

Todas as rotas (exceto `/auth/**`, `/swagger-ui/**`, `/v3/api-docs/**`) requerem token JWT no header:

```http
Authorization: Bearer {token}
```

### Fluxo de Autenticação

1. **Registrar** um novo servidor em `/auth/register`
2. **Login** com credenciais em `/auth/login`
3. Receber **token JWT** na resposta
4. Incluir token no header `Authorization` das próximas requisições

---

## 📡 Endpoints Principais

### Autenticação

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| POST | `/auth/register` | Cadastrar servidor | Não |
| POST | `/auth/login` | Fazer login | Não |

**Exemplo - Registrar:**
```json
POST /auth/register
Content-Type: application/json

{
  "nome": "João Silva",
  "email": "joao@email.com",
  "senha": "123456",
  "pagamento": 5000.00
}
```

**Resposta:**
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@email.com",
  "pagamento": 5000.00
}
```

**Exemplo - Login:**
```json
POST /auth/login
Content-Type: application/json

{
  "email": "joao@email.com",
  "senha": "123456"
}
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer"
}
```

### Servidores

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| GET | `/servidores` | Listar todos | Sim |
| GET | `/servidores/{id}` | Buscar por ID | Sim |
| POST | `/servidores` | Criar servidor | Sim |
| PUT | `/servidores/{id}` | Atualizar | Sim |
| DELETE | `/servidores/{id}` | Deletar | Sim |

**Exemplo - Criar Servidor:**
```json
POST /servidores
Authorization: Bearer {token}
Content-Type: application/json

{
  "nome": "Maria Santos",
  "email": "maria@email.com",
  "senha": "senha123",
  "pagamento": 6000.00
}
```

**Exemplo - Atualizar Servidor:**
```json
PUT /servidores/1
Authorization: Bearer {token}
Content-Type: application/json

{
  "nome": "João Silva Junior",
  "email": "joao.junior@email.com",
  "senha": "novaSenha123",
  "pagamento": 5500.00
}
```

### Férias

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| GET | `/ferias` | Listar todas | Sim |
| GET | `/ferias/{id}` | Buscar por ID | Sim |
| GET | `/ferias/servidor/{id}` | Férias de um servidor | Sim |
| POST | `/ferias` | Criar solicitação | Sim |
| PUT | `/ferias/{id}` | Atualizar | Sim |
| DELETE | `/ferias/{id}` | Deletar | Sim |

**Exemplo - Criar Solicitação de Férias:**
```json
POST /ferias
Authorization: Bearer {token}
Content-Type: application/json

{
  "servidorId": 1,
  "dataInicio": "2026-01-10",
  "dataFim": "2026-01-20"
}
```

**Resposta:**
```json
{
  "id": 1,
  "dataInicio": "2026-01-10",
  "dataFim": "2026-01-20",
  "servidorId": 1,
  "statusId": 1,
  "dias": 11,
  "pagFerias": 6666.67,
  "observacao": "Pagamento efetuado 48h antes do início das férias"
}
```

**Exemplo - Atualizar Férias:**
```json
PUT /ferias/1
Authorization: Bearer {token}
Content-Type: application/json

{
  "dataInicio": "2026-01-15",
  "dataFim": "2026-01-25",
  "statusId": 2
}
```

### Status

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| GET | `/status` | Listar status | Sim |

**Resposta:**
```json
[
  { "id": 1, "nome": "PENDENTE" },
  { "id": 2, "nome": "APROVADO" },
  { "id": 3, "nome": "REPROVADO" }
]
```

---

## 🔒 Segurança

### Autenticação JWT

**Token Base**: HS256 com secret em Base64  
**Expiração**: Configurável via `JWT_EXPIRATION` (padrão: 1 hora / 3600000ms)  
**Validação**: Filter customizado (`JwtAuthenticationFilter`) valida token em cada requisição  
**Stateless**: Sessões não são mantidas no servidor

#### Componentes de Segurança

**JwtTokenProvider**
- Gera tokens JWT com subject (email do usuário)
- Valida tokens recebidos
- Extrai informações (subject) de tokens válidos
- Usa chave HMAC-SHA256 derivada do secret

**JwtAuthenticationFilter**
- Intercepta todas as requisições HTTP
- Extrai token do header `Authorization: Bearer {token}`
- Valida token e configura contexto de segurança do Spring
- Bypass automático para rotas públicas (`/auth/**`)

### Proteção de Senhas

**Encoding**: BCrypt para hash de senhas  
**Salt**: Gerado automaticamente pelo BCrypt (16 bytes)  
**Rounds**: Padrão BCrypt (10 rounds)  
**Senhas**: Nunca retornadas nas respostas da API (filtradas nos DTOs)

### Configurações Spring Security

**CSRF**: Desabilitado (API stateless REST)  
**Session Management**: STATELESS (sem cookies de sessão)  
**Rotas Públicas**: 
- `/auth/**` (autenticação)
- `/swagger-ui/**` (documentação)
- `/v3/api-docs/**` (OpenAPI)
- `/actuator/health` (health check)

**Rotas Protegidas**: Todas as demais requerem token JWT válido

### Isolamento de Containers

**Multi-stage Build**:
- Etapa 1: Build com Maven (imagem completa)
- Etapa 2: Runtime com JRE Alpine (imagem otimizada)

**Segurança do Container**:
- Aplicação roda como usuário não-root (`spring:spring`)
- Imagem base Alpine Linux (superfície de ataque reduzida)
- JVM otimizada: `-Xmx512m -Xms256m`
- Health check configurado

---

## 🏗️ Arquitetura

### Estrutura em Camadas

```
┌─────────────────────────────────────┐
│       Controller Layer              │  ← API REST (JSON)
│  (AuthController, FeriasController) │
└─────────────────────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│        Service Layer                │  ← Lógica de Negócio
│  (FeriasService, ServidorService)   │
└─────────────────────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│       Repository Layer              │  ← Acesso a Dados
│  (FeriasRepository, etc)            │
└─────────────────────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│         Database Layer              │  ← PostgreSQL
│  (Servidores, Ferias, Status)      │
└─────────────────────────────────────┘
```

### Componentes Principais

#### Controllers
Responsabilidades:
- Gerenciam requisições HTTP e respostas
- Validação básica de entrada
- Mapeamento de rotas REST
- Delegação para services
- Serialização JSON automática

Anotações principais:
- `@RestController` - Marca classe como controller REST
- `@RequestMapping` - Define prefixo de rota
- `@GetMapping`, `@PostMapping`, etc. - Mapeia métodos HTTP

#### Services
Responsabilidades:
- Contêm regras de negócio complexas
- Validações de negócio
- Cálculos (ex: pagamento de férias = salário + 1/3)
- Orquestração de operações entre repositories
- Conversão entre entities e DTOs

Anotações principais:
- `@Service` - Marca classe como service
- `@Transactional` - Gerencia transações de banco
- `@Transactional(readOnly = true)` - Otimiza consultas

#### Repositories
Responsabilidades:
- Interface com banco de dados
- Spring Data JPA (queries automáticas)
- Queries customizadas quando necessário
- Abstração de SQL

Anotações principais:
- `@Repository` - Marca interface como repository
- Métodos derivados: `findByEmail`, `existsByEmail`

#### DTOs (Data Transfer Objects)
Responsabilidades:
- Transferência de dados entre camadas
- Separação entre entidades de domínio e contratos de API
- Controle de exposição de dados sensíveis
- Versionamento de API facilitado

Tipos:
- `ServidorDTO` - Resposta (sem senha)
- `ServidorCreateDTO` - Criação (com senha)
- `FeriasDTO` - Resposta completa
- `FeriasCreateDTO` - Criação simplificada

### Persistência

#### Flyway
- Controle de versão do schema de banco
- Migrações versionadas (`V1__`, `V2__`, `V3__`, `V4__`)
- Rastreabilidade de mudanças no banco
- Baseline automático em migração
- Histórico em tabela `flyway_schema_history`

Estrutura de migrações:
```
V1__create_servidores.sql
V2__create_status.sql
V3__create_ferias.sql
V4__insert_default_status.sql
```

#### JPA/Hibernate
- Mapeamento objeto-relacional automático
- Lazy loading para relacionamentos (`@ManyToOne`)
- Transações gerenciadas por anotações
- Dialect específico para PostgreSQL
- DDL desabilitado (`hibernate.ddl-auto=none`)

---

## 🎨 Padrões de Projeto

### 1. Dependency Injection (Injeção de Dependências)

**Implementação**: Constructor injection via Lombok `@RequiredArgsConstructor`

**Benefícios**:
- Testabilidade (mocks fáceis)
- Baixo acoplamento entre componentes
- Imutabilidade dos campos (final)
- Código mais limpo

**Exemplo**:
```java
@Service
@RequiredArgsConstructor
public class FeriasService {
    private final FeriasRepository feriasRepository;
    private final ServidorRepository servidorRepository;
    private final StatusService statusService;
    // Construtor gerado automaticamente pelo Lombok
}
```

### 2. DTO Pattern (Data Transfer Object)

**Separação**: Entidades de domínio vs. contratos de API

**Conversão**: Métodos `toDTO()` nos services para mapear entidades

**Segurança**: Campos sensíveis não expostos (ex: senha nunca retornada)

**Exemplo**:
```java
// Entity (nunca exposta diretamente)
@Entity
class Servidor {
    private String senha; // hash BCrypt
}

// DTO (exposto na API)
class ServidorDTO {
    // senha não existe aqui
}
```

### 3. Repository Pattern

**Abstração**: Spring Data JPA oculta complexidade SQL

**Queries**: Métodos derivados de nomes
- `findByEmail(String email)`
- `existsByEmail(String email)`
- `findByServidorId(Long servidorId)`

**Customização**: Queries JPQL quando necessário

**Exemplo**:
```java
public interface ServidorRepository extends JpaRepository<Servidor, Long> {
    Optional<Servidor> findByEmail(String email);
    boolean existsByEmail(String email);
}
```

### 4. Service Layer Pattern

**Transações**: `@Transactional` para operações atômicas

**ReadOnly**: Otimização para consultas (não gerencia dirty checking)

**Validações**: BusinessException para regras de negócio

**Exemplo**:
```java
@Transactional
public FeriasDTO create(FeriasCreateDTO dto) {
    // Validações
    // Cálculos
    // Persistência
    // Conversão para DTO
}

@Transactional(readOnly = true)
public List<FeriasDTO> findAll() {
    // Apenas consulta
}
```

### 5. Exception Handling

**Customizadas**: 
- `ResourceNotFoundException` - Recurso não encontrado (404)
- `BusinessException` - Regra de negócio violada (400)

**Propagação**: RuntimeException para rollback automático de transações

**Separação**: Erros de negócio vs. erros técnicos

**Exemplo**:
```java
if (servidorRepository.existsByEmail(dto.getEmail())) {
    throw new BusinessException("Email já cadastrado");
}

Servidor s = servidorRepository.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException("Servidor não encontrado"));
```

### 6. Filter Pattern

**JWT Filter**: `OncePerRequestFilter` para autenticação em cada requisição

**Ordem**: Executado antes de `UsernamePasswordAuthenticationFilter`

**Bypass**: Rotas públicas ignoradas no filtro (`/auth/**`)

**Fluxo**:
1. Extrai token do header
2. Valida token
3. Configura contexto de segurança
4. Passa requisição adiante

### 7. Builder Pattern (Implícito)

**Lombok**: 
- `@Data` - Getters, setters, equals, hashCode, toString
- `@AllArgsConstructor` - Construtor com todos os campos
- `@NoArgsConstructor` - Construtor vazio (para JPA)

**JPA**: Entidades com getters/setters automáticos

### 8. Strategy Pattern (Implícito)

**PasswordEncoder**: Interface para diferentes algoritmos de hash
- Atualmente: BCrypt
- Facilita mudança futura para Argon2, SCrypt, etc.

---

## 💡 Regras de Negócio

### Cálculo de Pagamento de Férias

**Fórmula**: `pagFerias = salário + (salário / 3)`

**Exemplo**:
- Salário: R$ 5.000,00
- 1/3: R$ 1.666,67
- **Total**: R$ 6.666,67

**Arredondamento**: 2 casas decimais, modo HALF_UP

### Status de Solicitações

**PENDENTE** (ID: 1)
- Status inicial de toda solicitação
- Permite edição e exclusão
- Aguardando aprovação

**APROVADO** (ID: 2)
- Solicitação aprovada
- Não permite exclusão
- Permite consulta

**REPROVADO** (ID: 3)
- Solicitação negada
- Permite exclusão
- Permite consulta

### Validações de Férias

#### 1. Datas Válidas
```
dataFim >= dataInicio
```

#### 2. Sobreposição de Períodos
Não permite que um mesmo servidor tenha férias sobrepostas:
```
NÃO (novaDataFim < existenteDataInicio OU novaDataInicio > existenteDataFim)
```

#### 3. Alteração de Status
- Apenas solicitações **PENDENTES** podem ser alteradas
- Férias APROVADAS ou REPROVADAS são somente leitura

#### 4. Exclusão
- Não é possível deletar férias com status **APROVADO**
- PENDENTE e REPROVADO podem ser excluídas

### Cálculo de Dias

**Fórmula**: `ChronoUnit.DAYS.between(dataInicio, dataFim) + 1`

**Exemplo**:
- Início: 10/01/2026
- Fim: 20/01/2026
- **Dias**: 11 dias (incluindo início e fim)

### Observações Automáticas

Toda solicitação recebe automaticamente:
```
"Pagamento efetuado 48h antes do início das férias"
```

---

## 📚 Documentação da API

### Swagger UI

Acesse a documentação interativa após subir a aplicação:

```
http://localhost:8080/swagger-ui.html
```

ou

```
http://localhost:8080/swagger-ui/index.html
```

**Funcionalidades**:
- Visualização de todos os endpoints
- Schemas de request/response
- Testes interativos
- Autenticação JWT integrada

### OpenAPI JSON

Especificação OpenAPI 3.0 disponível em:

```
http://localhost:8080/v3/api-docs
```

---

## 🏥 Health Check

### Endpoint de Saúde

```
GET http://localhost:8080/actuator/health
```

**Resposta** (aplicação saudável):
```json
{
  "status": "UP"
}
```

### Docker Health Check

O container possui health check configurado:
```yaml
healthcheck:
  test: ["CMD", "wget", "--quiet", "--tries=1", "--spider", 
         "http://localhost:8080/actuator/health"]
  interval: 30s
  timeout: 10s
  retries: 3
  start_period: 40s
```

---

## 🗄️ Estrutura do Banco de Dados

### Tabelas

#### `servidores`
| Coluna | Tipo | Constraints | Descrição |
|--------|------|-------------|-----------|
| id | SERIAL | PK | Identificador único |
| nome | VARCHAR(255) | NOT NULL | Nome completo |
| email | VARCHAR(255) | NOT NULL, UNIQUE | Email único |
| senha | VARCHAR(255) | NOT NULL | Hash BCrypt da senha |
| pagamento | NUMERIC(10,2) | - | Salário mensal |
| created_at | TIMESTAMP | DEFAULT NOW() | Data de criação |

#### `status`
| Coluna | Tipo | Constraints | Descrição |
|--------|------|-------------|-----------|
| id | SERIAL | PK | Identificador único |
| nome | VARCHAR(100) | NOT NULL, UNIQUE | PENDENTE/APROVADO/REPROVADO |

**Dados iniciais**:
```sql
INSERT INTO status (nome) VALUES
('PENDENTE'),
('APROVADO'),
('REPROVADO');
```

#### `ferias`
| Coluna | Tipo | Constraints | Descrição |
|--------|------|-------------|-----------|
| id | SERIAL | PK | Identificador único |
| servidor_id | INTEGER | NOT NULL, FK | Referência ao servidor |
| status_id | INTEGER | NOT NULL, FK | Referência ao status |
| data_inicio | DATE | NOT NULL | Início das férias |
| data_fim | DATE | NOT NULL | Fim das férias |
| dias | INTEGER | NOT NULL | Total de dias |
| pag_ferias | NUMERIC(10,2) | - | Valor a receber |
| observacao | TEXT | NOT NULL | Observações gerais |

**Relacionamentos**:
```sql
CONSTRAINT fk_ferias_servidor FOREIGN KEY (servidor_id)
    REFERENCES servidores (id)
    
CONSTRAINT fk_ferias_status FOREIGN KEY (status_id)
    REFERENCES status (id)
```

### Diagrama ER Simplificado

```
servidores (1) ────< (N) ferias (N) >──── (1) status
    │                      │
    id                status_id
    nome              servidor_id
    email             data_inicio
    senha             data_fim
    pagamento         dias
    created_at        pag_ferias
                      observacao
```

### Migrações Flyway

Arquivos em `src/main/resources/db/migration/`:

```
V1__create_servidores.sql      → Cria tabela servidores
V2__create_status.sql          → Cria tabela status
V3__create_ferias.sql          → Cria tabela ferias
V4__insert_default_status.sql → Insere status iniciais
```

---

## 🔧 Testes com Postman

### Importar Collection

1. Abra o Postman
2. Click em **Import**
3. Selecione o arquivo: `Ferias Service API.postman_collection.json`
4. Collection será importada com todos os endpoints

### Configurar Variável de Token

A collection possui uma variável `{{token}}` que pode ser configurada:

**Opção 1 - Manual**:
1. Faça login em `/auth/login`
2. Copie o token da resposta
3. Cole diretamente nos headers dos requests que precisam

**Opção 2 - Variável de Collection**:
1. Edite a collection
2. Vá em **Variables**
3. Defina `token` com o valor obtido no login

### Fluxo de Teste Recomendado

```
1. POST /auth/register     → Criar servidor
2. POST /auth/login        → Obter token
3. GET  /status            → Listar status disponíveis
4. POST /ferias            → Criar solicitação de férias
5. GET  /ferias            → Listar todas as férias
6. GET  /ferias/1          → Buscar férias específica
7. PUT  /ferias/1          → Atualizar férias
8. GET  /ferias/servidor/1 → Listar férias do servidor
9. DELETE /ferias/1        → Excluir férias (se PENDENTE)
```

---

## 🐛 Troubleshooting

### Erro: "Connection refused" ao conectar no banco

**Causa**: PostgreSQL não está rodando ou não está acessível

**Solução**:
```bash
# Verificar se container está rodando
docker ps

# Verificar logs do PostgreSQL
docker logs ferias-postgres

# Reiniciar PostgreSQL
docker-compose -f docker-compose-postgres.yml restart
```

### Erro: "Unauthorized" em endpoints protegidos

**Causa**: Token JWT inválido, expirado ou ausente

**Solução**:
1. Fazer novo login em `/auth/login`
2. Verificar se header está correto: `Authorization: Bearer {token}`
3. Verificar se token não expirou (padrão: 1 hora)

### Erro: "Email já cadastrado"

**Causa**: Tentativa de criar servidor com email duplicado

**Solução**:
- Use um email diferente
- Ou atualize o servidor existente com PUT

### Erro: "Período conflita com férias já existentes"

**Causa**: Sobreposição de datas de férias para mesmo servidor

**Solução**:
- Escolha datas diferentes
- Ou exclua/altere a solicitação conflitante

### Erro: "Somente solicitações pendentes podem ser alteradas"

**Causa**: Tentativa de alterar férias APROVADAS ou REPROVADAS

**Solução**:
- Apenas férias com status PENDENTE podem ser editadas
- Crie uma nova solicitação se necessário

### Aplicação não sobe no Docker

**Verificar**:
```bash
# Logs da aplicação
docker logs ferias-api

# Verificar se porta 8080 está livre
lsof -i :8080  # Linux/Mac
netstat -ano | findstr :8080  # Windows

# Verificar conectividade com banco
docker exec -it ferias-api ping ferias-postgres
```

---

## 📝 Notas de Desenvolvimento

### Ambiente de Desenvolvimento

**IDE Recomendada**: IntelliJ IDEA ou VS Code com extensões Java

**Extensões úteis**:
- Spring Boot Extension Pack
- Lombok
- Docker
- REST Client

### Hot Reload

A aplicação possui Spring Boot DevTools configurado para hot reload automático durante desenvolvimento.

### Profiles Spring

Atualmente apenas profile padrão (default). Para adicionar profiles:

```properties
# application-dev.properties
spring.jpa.show-sql=true

# application-prod.properties
spring.jpa.show-sql=false
```

Executar com profile:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

---

## 🚀 Deploy em Produção

### Checklist de Segurança

- [ ] Alterar `JWT_SECRET` para valor complexo e único
- [ ] Usar senhas fortes para banco de dados
- [ ] Configurar HTTPS/TLS
- [ ] Configurar CORS adequadamente
- [ ] Revisar variáveis de ambiente sensíveis
- [ ] Desabilitar endpoints de desenvolvimento (`/actuator/*`)
- [ ] Configurar rate limiting
- [ ] Implementar logging adequado
- [ ] Configurar backup de banco de dados

### Variáveis de Ambiente Produção

```bash
# Database
export SPRING_DATASOURCE_URL=jdbc:postgresql://db.prod.com:5432/ferias_prod
export SPRING_DATASOURCE_USERNAME=ferias_user
export SPRING_DATASOURCE_PASSWORD=<senha-forte>

# JWT
export JWT_SECRET=<secret-complexo-256bits-base64>
export JWT_EXPIRATION=1800000  # 30 minutos

# JPA
export SPRING_JPA_SHOW_SQL=false
export SPRING_JPA_HIBERNATE_DDL_AUTO=none

# Server
export SERVER_PORT=8080
```

### Build para Produção

```bash
# Compilar sem testes
./mvnw clean package -DskipTests

# Ou executar testes antes
./mvnw clean package

# Build da imagem Docker
docker build -t ferias-service:latest .

# Executar container
docker run -d \
  --name ferias-api \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/ferias_db \
  -e SPRING_DATASOURCE_USERNAME=user \
  -e SPRING_DATASOURCE_PASSWORD=senha \
  -e JWT_SECRET=<seu-secret> \
  ferias-service:latest
```

---

## 📄 Licença

Este projeto é um sistema interno de gerenciamento de férias para servidores públicos.

---

## 👥 Contato e Suporte

Para dúvidas, sugestões ou problemas:
- Abra uma issue no repositório
- Entre em contato com a equipe de desenvolvimento

---

**Versão da Documentação**: 1.0.0  
**Última Atualização**: Dezembro 2024
