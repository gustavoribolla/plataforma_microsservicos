# Exchange API

A **Exchange API** disponibiliza cotações em tempo-real entre duas moedas usando a fonte pública **AwesomeAPI**.  
Implementado em **Python 3 + FastAPI** conforme o [exercício 1](https://insper.github.io/platform/exercises/exercise1/).

---

## Repositório de Desenvolvimento

Acesse o [repositório](https://github.com/gustavoribolla/api.exchange_service) de desenvolvimento do Exchange API

---

## Visão Geral

| Item                     | Valor                                             |
|--------------------------|---------------------------------------------------|
| **Base URL (local)**     | `http://localhost:8000`                           |
| **Health-check**         | `GET /`                                           |
| **Endpoint principal**   | `GET /exchange/{currency1}/{currency2}`           |
| **Documentação OpenAPI** | `GET /docs`                                       |
| **Repositório**          | `api/exchange-service`                            |

---

## Endpoints

### `GET /`

Retorna um JSON estático para verificação do serviço.

```json
{"message": "Hello World"}
````

---

### `GET /exchange/{currency1}/{currency2}`

| Parâmetro    | Local  | Tipo   | Obrigatório | Exemplo                                |
| ------------ | ------ | ------ | ----------- | -------------------------------------- |
| `currency1`  | Path   | String | ✓           | `USD`                                  |
| `currency2`  | Path   | String | ✓           | `BRL`                                  |
| `id-account` | Header | UUID   | ✓           | `01fef110-5ca9-4e83-803c-453873d2db77` |

#### Resposta `200 OK`

```json
{
  "sell": "5.1372",
  "buy":  "5.1349",
  "date": "2025-06-02T18:35:04.532608",
  "id-account": "01fef110-5ca9-4e83-803c-453873d2db77"
}
```

| Campo        | Descrição                              |
| ------------ | -------------------------------------- |
| `sell`       | Preço de venda (*ask*)                 |
| `buy`        | Preço de compra (*bid*)                |
| `date`       | Timestamp ISO-8601 gerado pelo serviço |
| `id-account` | Eco do cabeçalho para rastreabilidade  |

#### Códigos de Erro

| Código | Motivo                                |
| ------ | ------------------------------------- |
| `400`  | Moeda inválida ou formato incorreto   |
| `422`  | Cabeçalho `id-account` ausente        |
| `502`  | Falha de comunicação com a AwesomeAPI |

---

## Exemplo de Uso

```bash
curl -H "id-account: 01fef110-5ca9-4e83-803c-453873d2db77" \
     http://localhost:8000/exchange/USD/BRL
```

---

## Trecho de Implementação

```python
@app.get("/exchange/{currency1}/{currency2}")
def exchange(request: Request, currency1: str, currency2: str):
    url = f"https://economia.awesomeapi.com.br/last/{currency1}-{currency2}"
    data = requests.get(url, timeout=5).json()[f"{currency1}{currency2}"]

    return {
        "sell": data["ask"],
        "buy":  data["bid"],
        "date": datetime.datetime.utcnow().isoformat(),
        "id-account": request.headers["id-account"],
    }
```

---

## Próximos Passos

* Adicionar **cache Redis** de curto prazo para reduzir chamadas externas.
* Migrar para `httpx.AsyncClient` e otimizar concorrência.
* Automatizar deployment em **MiniKube** com *HorizontalPodAutoscaler*.

