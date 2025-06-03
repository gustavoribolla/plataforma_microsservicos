# Order API

Implementação em **Spring Boot 3 / Java 21**, conforme  
[Exercício 3 — Order API](https://insper.github.io/platform/exercises/exercise3/).

---

## Repositório de Desenvolvimento

Acesse o [repositório](https://github.com/gustavoribolla/api.order) da interface e o [repositório](https://github.com/gustavoribolla/api.order_service) do service do Order.

---

| Item                           | Valor                                                   |
|--------------------------------|---------------------------------------------------------|
| **Docker image**               | `ribollequis87/order-service:latest`                    |
| **Porta padrão**               | `8080`                                                  |
| **Base URL**                   | `http://localhost:8080`                                 |
| **Health-check**               | `GET /actuator/health` → `200 {"status":"UP"}`          |
| **OpenAPI UI**                 | `GET /swagger-ui.html`                                  |
| **Banco**                      | PostgreSQL (`orders` schema)                            |
| **Dependência externa**        | Product API (Feign)                                     |
| **Caches em uso**              | `orderById`, `ordersByAccount`                          |

---

## Modelo de Dados

<details>
<summary>Order</summary>

```json
{
  "id"        : "29e56935-7b0f-4faf-8927-1afcdf792da3",
  "date"      : "2025-06-02T12:34:56Z",
  "total"     : 1799.80,
  "idAccount" : "01fef110-5ca9-4e83-803c-453873d2db77",
  "items"     : [ /* Item[] */ ]
}
````

</details>

<details>
<summary>Item</summary>

```json
{
  "id"       : "b4edd732-a318-47b4-9f6d-9fc5c078bf76",
  "quantity" : 2,
  "total"    : 899.90,
  "product"  : {
    "id"   : "2ef0d541-8f09-4dbe-8777-e5b2810ac3af",
    "name" : "Headset Gamer",
    "price": 449.95
  }
}
```

</details>

---

## Endpoints

### `POST /order`

Cria um pedido.

*Headers*

| Header       | Exemplo                                |
| ------------ | -------------------------------------- |
| `id-account` | `01fef110-5ca9-4e83-803c-453873d2db77` |

*Body*

```json
{
  "items": [
    { "quantity": 2, "idProduct": "2ef0d541-8f09-4dbe-8777-e5b2810ac3af" }
  ]
}
```

| Resposta               | Status |
| ---------------------- | ------ |
| JSON do pedido         | `201`  |
| Produto não encontrado | `404`  |

---

### `GET /order/{id}`

Retorna um pedido (usa cache **orderById**).

| Resposta       | Status |
| -------------- | ------ |
| JSON do pedido | `200`  |
| Não encontrado | `404`  |

---

### `GET /order?idAccount={uuid}`

Lista pedidos de um cliente (usa cache **ordersByAccount**).

---

## Regras de Negócio

| Regra                   | Implementação                             |
| ----------------------- | ----------------------------------------- |
| Total do item           | `quantity * product.price`                |
| Total do pedido         | soma de subtotais                         |
| Data do pedido          | `new Date()` (UTC)                        |
| Produto inexistente     | lança `404` via Feign-client              |
| Cache *orderById*       | chave = `id`                              |
| Cache *ordersByAccount* | chave = `idAccount`                       |
| Log SLF4J               | `logger.debug("Order found: {}", order);` |

---

## Tratamento de Erros

| Status | Cenário                           |
| ------ | --------------------------------- |
| 400    | Payload inválido                  |
| 404    | Produto / Pedido não encontrado   |
| 500    | Falha inesperada (consultar logs) |

---

## Build & Execução local

```bash
mvn clean package -DskipTests
java -jar target/order-service-1.0.0.jar
```

### Docker

```bash
docker build -t ribollequis87/order-service:latest .
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/store \
  ribollequis87/order-service:latest
```

---

## Observabilidade

* **Caches** monitorados via `/actuator/caches`.
* Logs estruturados (`{ "level":"DEBUG", "msg":"Order found", … }`).

---
