# Spring Boot Orders CRUD Practice

## 실행

Redis와 Kafka까지 함께 확인하려면 먼저 Docker Compose로 인프라를 띄웁니다.

```bash
docker compose up -d
```

```bash
./gradlew bootRun
```

Windows PowerShell에서는 다음 명령을 사용할 수 있습니다.

```powershell
.\gradlew.bat bootRun
```

## API

| Method | Path | Description |
| --- | --- | --- |
| GET | `/api/orders` | 주문 목록 조회 |
| GET | `/api/orders/{id}` | 주문 단건 조회 |
| POST | `/api/orders` | 주문 생성 |
| PUT | `/api/orders/{id}` | 주문 수정 |
| DELETE | `/api/orders/{id}` | 주문 삭제 |

## 요청 예시

```json
{
  "userId": 1,
  "items": [
    {
      "productName": "apple",
      "price": 1000,
      "quantity": 2
    },
    {
      "productName": "banana",
      "price": 500,
      "quantity": 3
    }
  ]
}
```

H2 콘솔은 `http://localhost:8080/h2-console`에서 확인할 수 있습니다.

- JDBC URL: `jdbc:h2:mem:codingtest`
- User Name: `sa`
- Password: 비워두기

## Redis / Kafka

- Redis: `/api/orders/{id}` 단건 조회 결과를 `orders` 캐시에 10분간 저장합니다.
- Kafka: 주문 생성/수정/삭제 트랜잭션 커밋 후 `orders.changed` 토픽으로 이벤트를 발행할 수 있습니다.

Kafka 발행은 기본값이 꺼져 있습니다. 로컬 Kafka를 켠 뒤 다음처럼 실행하면 활성화됩니다.

```powershell
.\gradlew.bat bootRun --args="--app.kafka.enabled=true"
```
