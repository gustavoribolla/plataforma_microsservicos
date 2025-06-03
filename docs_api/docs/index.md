# Plataforma de Microsserviços

[YouTube – vídeo-guia](https://youtu.be/<ID_DO_VIDEO>)

> **TL;DR** – este repositório demonstra, na prática, a criação de APIs independentes (Exchange, Product, Order) integradas via **Spring Cloud OpenFeign**, empacotadas em **Docker**, entregues por **Jenkins** e orquestradas em **Minikube**.  <br>
> **Caching** e outras otimizações de desempenho são documentadas em *Bottlenecks*.

---

## O que você encontra aqui

| Seção | Conteúdo em destaque |
|-------|----------------------|
| **[Exchange API](exchange-api/)** | FastAPI + requests • Cotação de moedas • Headers customizados |
| **[Product API](product-api/)** | CRUD completo • Spring Data • Cache de resultados |
| **[Order API](order-api/)** | Composição de itens • Totalização • Integração com Product |
| **[Jenkins](jenkins/)** | Pipeline as Code • Multi-arch Docker • Publicação no Hub |
| **[Bottlenecks](bottlenecks/)** | Diagnóstico de gargalos • Spring Cache • Métricas p95 |

---

## Estrutura em submódulos Git

Este repositório **principal** funciona como um hub; cada micro-serviço vive em um **submódulo Git** separado, permitindo versionamento e pipelines completamente independentes:

```text
platform/                 # repo principal
├─ api/
│  ├─ account             -> submódulo: https://github.com/gustavoribolla/api.account
│  ├─ account-service     -> submódulo: https://github.com/gustavoribolla/api.account_service
│  ├─ auth                -> submódulo: https://github.com/gustavoribolla/api.auth
│  ├─ auth-service        -> submódulo: https://github.com/gustavoribolla/api.auth_service
│  ├─ exchange-service    -> submódulo: https://github.com/gustavoribolla/api.exchange_service
│  ├─ gateway-service     -> submódulo: https://github.com/gustavoribolla/api.gateway_service
│  ├─ order               -> submódulo: https://github.com/gustavoribolla/api.order
│  ├─ order-service       -> submódulo: https://github.com/gustavoribolla/api.order_service
│  ├─ product             -> submódulo: https://github.com/gustavoribolla/api.product
│  └─ product-service     -> submódulo: https://github.com/gustavoribolla/api.product_service
└─ docs_api/                  
   └─ docs/               # documentação (MkDocs)
```

---

## Arquitetura em alto nível

```mermaid
flowchart LR
    subgraph Frontend / Gateway
        A[Cliente<br/>Postman/cURL] -->|HTTP| GW[Gateway API]
    end
    GW -->|/exchange| EX[Exchange API<br/>(FastAPI)]
    GW -->|/product| PR[Product API<br/>(Spring)]
    GW -->|/order  | OR[Order API<br/>(Spring)]
    OR -->|Feign| PR
    classDef spring fill:#6DB33F,color:#fff
    classDef python fill:#3772a3,color:#fff
    class PR,OR spring
    class EX python
````

* **Gateway** (Reverse-proxy) encaminha as requisições.
* **Product** persiste em **PostgreSQL**; usa **cache in-memory**.
* **Order** agrega preços do Product e grava pedidos.
* **Exchange** consulta cotações em provider externo.
* CI/CD automatizado pelo **Jenkins**, imagens enviadas para `ribollequis87/*`.

---

## Quick start

```bash
# 1. Subir todo o stack em modo dev
docker compose up -d --build

# 2. Exercitar a Exchange API
curl http://localhost:8080/exchange/USD/BRL

# 3. Testar Product API
curl -X POST http://localhost:8080/product \
     -H 'Content-Type: application/json' \
     -d '{"name":"Livro","price":42.90}'

# 4. Criar um pedido
curl -X POST http://localhost:8080/order \
     -H 'Content-Type: application/json' \
     -d '{"items":[{"quantity":2,"idProduct":"<uuid>"}]}'
```

> **Requisitos:** Docker 23+, Docker Compose v2, Java 17+ (para serviços Spring), Python 3.11 (para Exchange).

---

## Contribuindo

1. `git clone` e crie sua branch.
2. Rode `pre-commit install` para checagens automáticas.
3. Descreva bem o *PR*; o Jenkins executará testes e publicará uma *preview*.

---

## Agradecimentos

Projeto desenvolvido para a disciplina **[Plataforma de Microsserviços @ Insper](https://insper.github.io/platform/)**<br>
Orientação: Prof. Humberto Sandmann

