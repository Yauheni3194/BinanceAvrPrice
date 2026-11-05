# Приложение которое по расписанию забирает среднюю цену торговой пары у биржи Binance
Приложение работает на "Java 21", "Gradle 8.7" и "PostgreSQL 17".

## Как запустить:
## Поддерживаемые переменные окружения
* `SPRING_DATASOURCE_URL` — URL подключения к БД (по умолчанию: `jdbc:postgresql://localhost:5432/BinanceAvrPrice_db`)
* `BINANCE_POLL_INTERVAL` — Интервал опроса в формате ISO-8601 (По умолчанию: `PT30S`)
* `BINANCE_API_BASE_URL` — Базовый URL API биржи Binance (по умолчанию: `https://binance.com`)
Переменные можно изменить в application.properties по пути: "BinanceAvrPrice\src\main\resources"
### 1. Требования
На компьютере должен быть установлен и запущен "Docker Desktop".

### 2. Запуск проекта
Откройте терминал/консоль в папке проекта и выполните команду:
"docker compose up --build -d"

### 3. Примечания к работе
* При отправке запросов используйте стандартный формат ISO-8601 (строка с буквой Z на конце указывает на таймзону UTC):
  • Последняя цена: GET http://localhost:8080/api/v1/prices/latest?symbol=BNBUSDT
  • Средняя цена: GET http://localhost:8080/api/v1/prices/average?from=2026-11-04T12:00:00Z&to=2026-11-04T18:00:00Z
  • Пагинация списком: GET http://localhost:8080/api/v1/prices?from=2026-11-04T12:00:00Z&to=2026-11-04T18:00:00Z&page=0&size=5
  • Определение тренда: GET http://localhost:8080/api/v1/prices/trend?from=2026-11-04T12:00:00Z&to=2026-11-04T18:00:00Z
* Остановить проект: "docker compose down -v"


