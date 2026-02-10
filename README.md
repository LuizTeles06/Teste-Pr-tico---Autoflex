# Inventory Control System

Sistema de controle de estoque industrial para gerenciamento de produtos e matérias-primas.

## 📋 Requisitos

- Java 17+
- Maven 3.9+
- Node.js 18+
- PostgreSQL 15+ (ou Docker)

## 🏗️ Estrutura do Projeto

```
projeto AutoFlex/
├── backend/                 # API REST com Quarkus
│   ├── src/main/java/      # Código fonte
│   └── src/test/java/      # Testes unitários
├── frontend/               # Aplicação React
│   ├── src/                # Código fonte
│   └── cypress/            # Testes E2E
├── database/               # Scripts SQL
└── docker-compose.yml      # Orquestração de containers
```

## 🚀 Como Executar

### Opção 1: Docker Compose (Recomendado)

```bash
# Iniciar todos os serviços
docker-compose up -d

# Acessar a aplicação
# Frontend: http://localhost:3000
# API: http://localhost:8080
# Swagger: http://localhost:8080/swagger-ui
```

### Opção 2: Desenvolvimento Local

#### 1. Banco de Dados

```bash
# Com Docker
docker run -d --name inventory-db \
  -e POSTGRES_DB=inventory_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:15-alpine

# Ou instale o PostgreSQL e crie o banco manualmente
```

#### 2. Backend

```bash
cd backend

# Modo desenvolvimento (hot reload)
./mvnw quarkus:dev

# A API estará disponível em http://localhost:8080
```

#### 3. Frontend

```bash
cd frontend

# Instalar dependências
npm install

# Modo desenvolvimento
npm run dev

# A aplicação estará disponível em http://localhost:3000
```

## 🧪 Executar Testes

### Backend (JUnit)

```bash
cd backend
./mvnw test
```

### Frontend (Vitest)

```bash
cd frontend
npm test
```

### Testes E2E (Cypress)

```bash
cd frontend

# Modo interativo
npm run cypress:open

# Modo headless
npm run cypress:run
```

## 📚 API Endpoints

### Products

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/products` | Lista todos os produtos |
| GET | `/api/products/{id}` | Busca produto por ID |
| POST | `/api/products` | Cria novo produto |
| PUT | `/api/products/{id}` | Atualiza produto |
| DELETE | `/api/products/{id}` | Remove produto |
| POST | `/api/products/{id}/raw-materials` | Adiciona matéria-prima |
| DELETE | `/api/products/{id}/raw-materials/{rmId}` | Remove matéria-prima |

### Raw Materials

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/raw-materials` | Lista todas as matérias-primas |
| GET | `/api/raw-materials/{id}` | Busca matéria-prima por ID |
| POST | `/api/raw-materials` | Cria nova matéria-prima |
| PUT | `/api/raw-materials/{id}` | Atualiza matéria-prima |
| DELETE | `/api/raw-materials/{id}` | Remove matéria-prima |

### Production

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/production/suggestion` | Calcula sugestão de produção |

## 🔧 Tecnologias

### Backend
- **Quarkus 3.6** - Framework Java
- **Hibernate ORM com Panache** - Persistência
- **PostgreSQL** - Banco de dados
- **RESTEasy Reactive** - API REST
- **OpenAPI/Swagger** - Documentação

### Frontend
- **React 18** - Biblioteca UI
- **TypeScript** - Tipagem estática
- **Redux Toolkit** - Gerenciamento de estado
- **React Router 6** - Roteamento
- **Tailwind CSS** - Estilização
- **Vite** - Build tool
- **Cypress** - Testes E2E

## 📝 Funcionalidades

- ✅ CRUD de Produtos
- ✅ CRUD de Matérias-Primas
- ✅ Associação de matérias-primas aos produtos
- ✅ Cálculo de sugestão de produção
- ✅ Priorização por maior valor do produto
- ✅ Interface responsiva
- ✅ Testes unitários
- ✅ Testes de integração E2E

## 🎯 Algoritmo de Sugestão de Produção

1. Produtos são ordenados por valor (maior para menor)
2. Para cada produto, calcula-se quantas unidades podem ser produzidas
3. As matérias-primas são "reservadas" conforme os produtos são alocados
4. O processo repete até não haver mais produção possível
5. Retorna a lista de produtos com quantidades e valor total

## 📄 Licença

Este projeto foi desenvolvido como teste prático para a empresa Autoflex.
