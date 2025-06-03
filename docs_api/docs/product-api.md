# Product API

Implementação em **Spring Boot 3 / Java 21**, parte do
[Exercício 2 — Product API](https://insper.github.io/platform/exercises/exercise2/).

---

## Repositório de Desenvolvimento

Acesse o [repositório](https://github.com/gustavoribolla/api.product) da interface e o [repositório](https://github.com/gustavoribolla/api.product_service) do service do Product.

---

## Visão Geral

| Item                           | Valor                                           |
|--------------------------------|-------------------------------------------------|
| **Docker image**               | `ribollequis87/product-service:latest`         |
| **Porta padrão**               | `8080`                                          |
| **Base URL (local)**           | `http://localhost:8080`                         |
| **Health-check**               | `GET /actuator/health` → `200 {"status":"UP"}`  |
| **OpenAPI UI**                 | `GET /swagger-ui.html`                          |
| **Banco**                      | PostgreSQL (`products` schema)                  |
| **Build**                      | `mvn clean package -DskipTests`                 |

---

## Modelo de Dados

```json
{
  "id"   : "fc2e5221-3e40-4418-a1d7-0c5d68427af2",
  "name" : "Notebook Gamer 15”",
  "price": 5899.90
}
````

| Campo   | Tipo      | Regras de validação            |
| ------- | --------- | ------------------------------ |
| `id`    | `UUID`    | Gerado pelo serviço            |
| `name`  | `String`  | 2 – 120 caracteres - único     |
| `price` | `Decimal` | `>= 0.00`, duas casas decimais |

---

## Endpoints

### 1. `POST /product`

Cria um produto.

```bash
curl -X POST http://localhost:8080/product \
     -H "Content-Type: application/json" \
     -d '{"name":"Mouse Óptico","price":129.90}'
```

| Resposta               | Status            | Observação            |
| ---------------------- | ----------------- | --------------------- |
| JSON do produto criado | `201 Created`     | —                     |
| JSON de erro           | `400 Bad Request` | Violação de validação |
| JSON de erro           | `409 Conflict`    | `name` já existente   |

---

### 2. `GET /product/{id}`  <!-- cached -->

```bash
curl http://localhost:8080/product/fc2e5221-3e40-4418-a1d7-0c5d68427af2
```

| Resposta        | Status          |
| --------------- | --------------- |
| JSON do produto | `200 OK`        |
| JSON de erro    | `404 Not Found` |

> **Cache:** resultado armazenado por 60 s (cache `productById`).

---

### 3. `GET /product`  <!-- cached -->

Lista produtos (paginação opcional).

| Query param | Default | Descrição                  |
| ----------- | ------- | -------------------------- |
| `page`      | `0`     | Índice da página (0-based) |
| `size`      | `50`    | Itens por página (`≤100`)  |

> **Cache:** lista completa armazenada por 60 s (cache `allProducts`).

---

### 4. `DELETE /product/{id}`

Remove o produto e devolve o JSON excluído.

| Resposta                 | Status          |
| ------------------------ | --------------- |
| JSON do produto removido | `200 OK`        |
| JSON de erro             | `404 Not Found` |

---

## Tratamento de Erros

| Status | Quando acontece                             |
| ------ | ------------------------------------------- |
| `400`  | JSON mal-formado, `name` vazio, `price < 0` |
| `404`  | Produto não encontrado                      |
| `409`  | Conflito de nome                            |
| `500`  | Falha inesperada (consultar logs)           |

Formato padrão:

```json
{
  "timestamp": "2025-06-02T15:14:03.774+00:00",
  "status"   : 400,
  "error"    : "Bad Request",
  "message"  : "Price must be ≥ 0",
  "path"     : "/product"
}
```

---

## Caching

O serviço usa **Spring Cache** (Redis) para reduzir latência de leitura.

| Método     | Cache         | Chave | TTL (config) |
| ---------- | ------------- | ----- | ------------ |
| `findById` | `productById` | `id`  | 60 s         |
| `findAll`  | `allProducts` | –     | 60 s         |

* Criação e exclusão **evictam** (`@CacheEvict`) ambas as entradas.
* Ajuste de TTL em `application.yml`:

```yaml
spring:
  cache:
    type: redis
    redis:
      time-to-live: 60s
```

---

## Exemplo de fluxo completo

```bash
# 1) Criar
curl -X POST http://localhost:8080/product \
     -H "Content-Type: application/json" \
     -d '{"name":"Teclado Mecânico","price":349.50}'

# 2) Listar (cai no cache a partir da 2ª execução)
curl http://localhost:8080/product

# 3) Buscar individual
curl http://localhost:8080/product/{id}

# 4) Deletar
curl -X DELETE http://localhost:8080/product/{id}
```

---

## Build & Execução

```bash
# Build jar
mvn clean package -DskipTests

# Rodar local
java -jar target/product-1.0.0.jar
```

### Docker

```bash
docker build -t ribollequis87/product-service:latest .
docker run -p 8080:8080 ribollequis87/product-service:latest
```

---

## Observabilidade

* **Spring Boot Actuator** expõe:

  * `/actuator/health`     – status da aplicação
  * `/actuator/metrics`   – métricas Micrometer
* Métricas prontas para Prometheus + Grafana.

---

## Próximos Passos

* **HATEOAS** – links do Product para Order API.
* **Circuit Breaker** no cliente Feign (Resilience4J).
* **Security** – endpoints protegidos por JWT (auth-service).
* **Tests** – Testcontainers + Rest-Assured para integração.

---
