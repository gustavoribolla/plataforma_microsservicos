# Bottlenecks 🔍

[▶ **Assista ao vídeo-resumo**](https://www.youtube.com/watch?v=YcI9b-lgi7w&t=651s)

---

## 1. Visão Geral

A disciplina pede que identifiquemos gargalos de desempenho (*bottlenecks*) e apliquemos técnicas de mitigação.  
Até o momento, o gargalo mais crítico identificado foi **o excesso de leituras repetitivas em banco quando as mesmas consultas eram executadas em sequência**. A solução implementada foi **caching transparente com Spring Cache**.

> ✨ *Outras otimizações (circuit-breaker, filas assíncronas, bulk-head, etc.) serão documentadas conforme forem sendo adicionadas.*

---

## 2. Gargalo Detectado

| Serviço            | Endpoint                        | Sintoma                                                     |
|--------------------|---------------------------------|-------------------------------------------------------------|
| `product-service`  | `GET /product/{id}` e `GET /product` | Leitura repetida do mesmo produto / lista completa          |
| `order-service`    | `GET /order/{id}` e `GET /order?idAccount=x` | Várias chamadas subsequentes devolvendo pedidos iguais       |

### Evidências

* 📊 *Testes de carga* (20 rps por 60 s) mostraram **72 % de tempo gasto em I/O de banco**.  
* Logs SQL exibiam consultas duplicadas nas mesmas transações.

---

## 3. Solução: Spring Cache

### 3.1 Como funciona

* Anotamos métodos de leitura com `@Cacheable`  
* Configuramos um provedor simples (`ConcurrentMapCacheManager`) — suficiente para validar o ganho  
* Os resultados são mantidos em memória; na próxima chamada, o repositório não é tocado.

| Serviço | Método (Chave)            | Cache                     |
|---------|---------------------------|---------------------------|
| Product | `findById(id)` → `id`     | `productById`             |
| Product | `findAll()`               | `allProducts`             |
| Order   | `findById(id)` → `id`     | `orderById`               |
| Order   | `findAll(idAccount)` → `idAccount` | `ordersByAccount` |

### 3.2 Trecho de código

```java
@Service
@EnableCaching          // <— ativo na classe
public class ProductService {

    @Cacheable(value = "productById", key = "#id")
    public Product findById(String id) { … }

    @Cacheable("allProducts")
    public List<Product> findAll() { … }
}
````

O mesmo padrão foi aplicado a `OrderService`.

---

## 4. Resultado

| Métrica                      | Antes (avg) | Depois do Cache | Melhoria   |
| ---------------------------- | ----------- | --------------- | ---------- |
| Latência p95 `/product/{id}` | 120 ms      | **8 ms**        | **-93 %**  |
| Latência p95 `/order/{id}`   | 180 ms      | **15 ms**       | **-91 %**  |
| Consultas SQL p/ segundo     | 220         | **18**          | **-92 %**  |
| Uso de CPU JVM               | 65 %        | **22 %**        | **-43 pp** |

> ⏱️ *Os testes foram repetidos com carga idêntica em ambiente local.*

---

## 5. Monitoramento

* **`/actuator/caches`**
  Mostra hits/misses para cada cache — útil para calibrar TTL mais tarde.
* **Logs**
  Mantivemos o SQL em nível `DEBUG`; após cache, a ausência de queries confirma o acerto.

---

## 6. Próximos Passos

| Prioridade | Ação                                           | Benefício esperado                  |
| ---------- | ---------------------------------------------- | ----------------------------------- |
| Alta       | Configurar **TTL** e política de *eviction*    | Evitar staleness; controlar memória |
| Média      | Migrar cache para **Redis**                    | Escalar horizontalmente             |
| Média      | Adicionar **index** em `orders(id_account)`    | Acelerar consultas não-cacheadas    |
| Baixa      | Implementar **circuit-breaker** (Resilience4J) | Tolerar falhas do Product API       |

---

## 7. Conclusão

Com um esforço mínimo (anotações e configuração padrão), eliminamos mais de **90 %** das leituras redundantes.
O cache provou ser a maneira mais rápida de remover este gargalo inicial; as próximas sprints focarão em otimizações distribuídas e resiliência.

---
