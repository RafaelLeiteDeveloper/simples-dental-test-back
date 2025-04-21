# 🧠 Desafio Backend - Simples Dental

Este projeto foi desenvolvido como parte de um desafio técnico proposto pela Simples Dental. A aplicação é uma API REST responsável por gerenciar **produtos** e **categorias**, com autenticação segura via JWT, controle de permissões, cache com Redis, e logging estruturado.

---

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Security**
- **PostgreSQL**
- **Redis**
- **Docker / Docker Compose**
- **JUnit + Mockito**
- **Swagger (OpenAPI 3)**
- **MapStruct**
- **Lombok**

---

## 📁 Estrutura de Pastas Baseado na arquitetura Hexagonal

```bash
.
├── src/main/java/com/simplesdental/product
│   ├── application       # Casos de uso (Application Layer)
│   ├── domain            # Modelos de domínio (Domain Layer)
│   ├── infrastructure    # Controllers, DTOs, Repositórios, Mappers, Segurança
│   └── ProductApplication.java
├── src/test              # Testes unitários
├── docker-compose.yml    # Configuração de containers Docker
├── Dockerfile            # Empacotamento da aplicação
├── pom.xml               # Dependências Maven
├── README.md             # Este arquivo
├── Postman_Collection.json  # 📫 Collection do Postman para testar a API
```

---

## ⚙️ Pré-requisitos

- Java 17+
- Docker e Docker Compose
- Maven 3.8+

---

## ▶️ Como Executar o Projeto

### ✅ Com Docker (recomendado)

```bash
docker-compose up --build
```

- A aplicação será iniciada em: `http://localhost:8080`

### ✅ Sem Docker (ambiente local)

1. Configure um banco PostgreSQL local com:
  - **Database:** produto
  - **Usuário:** postgres
  - **Senha:** postgres

2. Rode o comando:

```bash
./mvnw spring-boot:run
```

---

## 🔐 Autenticação

- A autenticação é baseada em **JWT**
- O token deve ser enviado no header:

```
Authorization: Bearer <token>
```

### Endpoints de Autenticação:

- `POST /auth/login` – Login do usuário
- `GET /auth/context` – Retorna dados do usuário autenticado

---

## 🧰 Funcionalidades

- CRUD de Produtos
- CRUD de Categorias
- Paginação e ordenação
- Validações completas de campos
- Autenticação com JWT
- Controle de acesso por perfil (admin/user)
- Cache com Redis para `/auth/context`
- Logging estruturado

---

## 🧪 Rodando os Testes

```bash
./mvnw test
```

---

## 📫 Postman Collection

A collection de testes está disponível no arquivo:

```
Postman_Collection.json
```

Você pode importá-la no [Postman](https://www.postman.com/) para testar os endpoints rapidamente.

---

## 📖 Documentação da API

A documentação interativa da API está disponível via Swagger:

📎 `http://localhost:8080/swagger-ui.html`

---


## 👨‍💻 Autor

Desenvolvido por **Rafael Leite**  
📧 rleite.developer@gmail.com  
💼 Desafio técnico - Simples Dental

