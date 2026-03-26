# Booking Platform

Платформа бронирования жилья (аналог Booking.com/Airbnb), разработанная с акцентом на **clean architecture**, **DDD (Domain-Driven Design)** и **enterprise-grade patterns**.

Это pet-project для демонстрации навыков:
- Проектирования сложных доменных моделей
- Гексагональной архитектуры (Ports & Adapters)
- Паттернов микросервисов (Outbox, Saga, Strangler Fig, Circuit Breaker)
- Потокобезопасности и консистентности данных
- Spring Boot, Hibernate, PostgreSQL
- Асинхронного взаимодействия (Spring Events → Kafka)
- Тестирования (unit, integration, concurrency)

---

## Архитектура

### Общая стратегия: Modular Monolith → Microservices

Проект проходит несколько фаз развития:

```
Фаза 1: Modular Monolith
├─ Один Spring Boot процесс
├─ Модули разделены чистыми границами (нет прямого доступа)
├─ Общение через интерфейсы (ports)
└─ Синхронное взаимодействие (Java method calls)

         ↓ (когда границы устояться)

Фаза 2: Инфраструктура
├─ API Gateway (Spring Cloud Gateway)
├─ Kafka вместо Spring Events
├─ Redis для кэширования
└─ Distributed Tracing (Zipkin)

         ↓ (Strangler Fig pattern)

Фаза 3: Microservices
├─ Notification Service (вынесен первым)
├─ Auth Service
├─ Property Service
├─ Booking Service
└─ User Service
```

**Текущий статус:** Фаза 1 — Modular Monolith с Booking Module реализованным в полном объеме.

---

## Модульная структура

### Модули и их ответственность

```
booking-platform/
├── shared-kernel/          ← Общие типы для всех модулей
│   ├── DateRange           (value object с валидацией)
│   ├── Money               (value object для денег)
│   ├── PageResult<T>       (пагинированные результаты)
│   └── DomainEvent         (базовый класс для событий)
│
├── booking-module/         ← Управление жизненным циклом бронирований
│   ├── domain/
│   │   ├── model/
│   │   │   ├── Booking (агрегат)
│   │   │   └── BookingStatus (enum со статусной машиной)
│   │   ├── event/ (доменные события)
│   │   ├── port/
│   │   │   ├── in/  (use case интерфейсы)
│   │   │   └── out/ (исходящие зависимости)
│   │   ├── service/ (реализация use cases)
│   │   └── exception/
│   ├── adapter/
│   │   ├── in/rest/        (контроллеры, DTOs)
│   │   └── out/
│   │       ├── persistence/ (JPA entities, repositories)
│   │       ├── messaging/   (event publisher)
│   │       └── stub/        (заглушки для портов)
│   └── test/
│
├── property-module/        ← Управление объектами размещения (TODO)
├── user-module/            ← Профили пользователей (TODO)
├── auth-module/            ← Аутентификация и JWT (TODO)
├── notification-module/    ← Email/push уведомления (TODO)
│
└── app/                    ← Spring Boot приложение, интеграция всех модулей
```

### Правила модульных границ

- **Нет циклических зависимостей** между модулями
- **Нет прямого доступа** к классам из других модулей (только через порты)
- **Удаление модуля** не должно сломать компиляцию других модулей
- Каждый модуль имеет **свой набор исключений** и **сущностей базы данных**
- Взаимодействие **синхронное** (порты) или **асинхронное** (события)

---

## Архитектура внутри модуля: Hexagonal (Ports & Adapters)

Каждый модуль следует гексагональной архитектуре:

```
┌─────────────────────────────────────────────────────────────┐
│                         ADAPTER LAYER                        │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  ┌──────────────┐         ┌──────────────┐                  │
│  │ REST         │         │ JPA          │                  │
│  │ Controller   │ ──────► │ Repository   │                  │
│  └──────────────┘         └──────────────┘                  │
│         │                        │                           │
│         └────────────┬───────────┘                           │
│                      │                                       │
├──────────────────────┼────────────────────────────────────────┤
│                      ▼                                        │
│           ┌─────────────────────┐                           │
│           │   DOMAIN SERVICE    │                           │
│           │  (реализует ports)  │                           │
│           └─────────────────────┘                           │
│                      │                                       │
├──────────────────────┼────────────────────────────────────────┤
│                      ▼                                        │
│        ┌─────────────────────────────────┐                  │
│        │      DOMAIN LOGIC               │                  │
│        │  (чистые модели, без Spring)    │                  │
│        │  Booking, BookingStatus, events │                  │
│        └─────────────────────────────────┘                  │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

**Ключевые принципы:**
1. **Domain layer независим** — не содержит Spring, JPA, или других фреймворков
2. **Порты определяют контракт** — что нужно сервису от внешнего мира
3. **Адаптеры реализуют порты** — как конкретно работать с БД, REST и т.д.
4. **Маппинг на границах** — между DTOs (для клиента) и доменными моделями

---

## Booking Module — Пример реализации

### Статусная машина (State Machine)

Бронирование проходит через строгие состояния:

```
                    ┌─────────┐
                    │ PENDING │
                    └────┬────┘
         ┌───────────────┼──────────────┐
         ▼               ▼              ▼
    ┌─────────┐     ┌──────────┐   ┌──────────┐
    │APPROVED │     │ REJECTED │   │CANCELLED │
    └────┬────┘     └──────────┘   └──────────┘
         │
         ├──── гость запросил изменение дат ────┐
         │                                      ▼
         │                             ┌─────────────────┐
         │                             │ CHANGE_REQUESTED│
         │                             └────────┬────────┘
         │               ┌─────────────────────┤
         │               ▼                     ▼
         │          ┌──────────┐    хозяин одобрил → APPROVED
         │          │CANCELLED │    (новые или старые даты)
         │          └──────────┘
         ▼
    ┌──────────┐
    │COMPLETED │
    └──────────┘
```

**Правила переходов закодированы в enum:**

```java
public enum BookingStatus {
    PENDING {
        public boolean canTransitionTo(BookingStatus next) {
            return next == APPROVED || next == REJECTED || next == CANCELLED;
        }
    },
    APPROVED {
        public boolean canTransitionTo(BookingStatus next) {
            return next == CHANGE_REQUESTED || next == COMPLETED || next == CANCELLED;
        }
    },
    // ... остальные статусы
}
```

Это гарантирует **типобезопасность** — невозможно сделать недопустимый переход на уровне компиляции.

### Use Case интерфейсы (Driving Ports)

```java
public interface CreateBookingUseCase {
    Booking create(UUID guestId, UUID roomId, DateRange dateRange, Money totalPrice);
}

public interface ApproveBookingUseCase {
    Booking approve(UUID bookingId);
}

// ... остальные use cases
```

**Чем это помогает:**
- Контроллер зависит от интерфейса, а не от реализации
- Легко тестировать (можно подменить mock)
- Ясно видно, что может делать модуль

### Исходящие зависимости (Driven Ports)

```java
public interface BookingRepositoryPort {
    Booking save(Booking booking);
    Optional<Booking> findById(UUID id);
    Optional<Booking> findByIdForUpdate(UUID id);  // ← для lock'а
    List<Booking> findConflicting(...);
    PageResult<Booking> findAll(BookingSearchFilter filter);
}

public interface BookingEventPublisherPort {
    void publish(DomainEvent event);
    void publishAll(List<DomainEvent> events);
}

public interface RoomAvailabilityPort {
    boolean isAvailable(UUID roomId, DateRange dateRange);
}
```

**Зачем:**
- Сервис не знает как работает БД — использует абстракцию
- Можно легко сменить реализацию (PostgreSQL → MongoDB)
- Тестировать сервис можно с mock'ами

### Доменные события (Event-Driven Architecture)

Когда изменяется состояние, генерируются события:

```java
booking.approve();  // ← внутри агрегата генерируется BookingApprovedEvent

List<DomainEvent> events = booking.pullDomainEvents();
eventPublisher.publishAll(events);
```

**Слушатели событий (Property Module, Notification Module и т.д.):**

```java
@EventListener(BookingApprovedEvent.class)
public void onBookingApproved(BookingApprovedEvent event) {
    // Property Module обновляет доступность
    // Notification Module отправляет письмо хозяину
}
```

**Преимущества:**
- **Слабая связанность** между модулями
- **Легко добавлять новые реакции** без изменения Booking Module
- **Асинхронное взаимодействие** (фаза 2 с Kafka)

---

## Потокобезопасность и консистентность данных

### Проблема: Race Condition при одобрении брони

Без защиты может произойти **Lost Update** (двойное бронирование):

```
Поток 1: approve(booking1)          Поток 2: approve(booking2)
├─ findById() ✓                      ├─ findById() ✓
├─ findConflicting()                 ├─ findConflicting()
│  → пусто (booking2 еще нет) ✓      │  → пусто (booking1 еще нет) ✓
├─ approve() ✓                       ├─ approve() ✓
└─ save() ✓                          └─ save() ✓

ИТОГ: Обе бронирования APPROVED на одни даты! ❌ ОШИБКА!
```

### Решение: Многоуровневая защита

#### Уровень 1: Pessimistic Locking (SELECT FOR UPDATE)

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT b FROM BookingJpaEntity b WHERE b.id = :id")
Optional<BookingJpaEntity> findByIdForUpdate(@Param("id") UUID id);
```

Когда один поток заблокирует запись, другие потоки **ждут**:

```
Поток 1: findByIdForUpdate(booking1)
├─ БД: SELECT FOR UPDATE  → LOCK на строку
│
Поток 2: findByIdForUpdate(booking2)
├─ БД: SELECT FOR UPDATE  → ждет пока Поток 1 отпустит lock
│
Поток 1: commit/rollback → отпускает lock
│
Поток 2: SELECT FOR UPDATE → получает lock ✓
```

**Механика:**
- PostgreSQL использует **rowlevel locks** (SELECT FOR UPDATE)
- Другие потоки не могут читать/писать в заблокированную строку
- Lock автоматически отпускается в конце транзакции

**Ограничения:**
- Может быть медленно на высоком трафике
- Может привести к deadlock'ам (редко)
- Не масштабируется идеально

#### Уровень 2: Database Constraint (UNIQUE)

```sql
ALTER TABLE bookings 
ADD CONSTRAINT uk_room_dates_approved_booking 
UNIQUE (room_id, start_date, end_date, status) 
WHERE status = 'APPROVED';
```

Если даже что-то пойдет не так, БД **не позволит** вставить дубликат:

```
INSERT INTO bookings (room_id, start_date, end_date, status, ...)
VALUES (room123, 2026-05-10, 2026-05-15, 'APPROVED', ...)

UNIQUE constraint violation! ❌
→ DataIntegrityViolationException → 409 Conflict
```

**Зачем два уровня:**
1. **Lock** предотвращает race condition в коде (быстро)
2. **Constraint** страхует на случай если lock не сработал (редко, но надежно)

#### Уровень 3: Transactional Isolation

```java
@Transactional
public Booking approve(UUID bookingId) {
    // Вся операция в одной транзакции
    // Используется REPEATABLE_READ (PostgreSQL default)
    
    Booking booking = repository.findByIdForUpdate(bookingId);
    List<Booking> conflicts = repository.findConflicting(...);
    
    if (!conflicts.isEmpty()) {
        throw new IllegalArgumentException(...);
    }
    
    booking.approve();
    repository.save(booking);
    eventPublisher.publishAll(booking.pullDomainEvents());
    
    // commit ← все или ничего
}
```

**Spring @Transactional:**
- Автоматически открывает транзакцию
- На commit: либо все save'ы прошли, либо все rollback'ились
- На Exceptions: автоматический rollback

**Isolation Level (PostgreSQL):**
- **READ_UNCOMMITTED** — грязное чтение (не используем)
- **READ_COMMITTED** (default) — видим только committed changes
- **REPEATABLE_READ** — snapshot isolation (идеально для нашего случая)
- **SERIALIZABLE** — все транзакции выполняются по очереди (медленно)

### Проверка конфликтов

```java
// В approve()
List<Booking> conflicts = repository.findConflicting(
    booking.getRoomId(), 
    booking.getDateRange(), 
    BookingStatus.APPROVED
);

if (!conflicts.isEmpty()) {
    throw new IllegalArgumentException("Room already booked for this period");
}
```

**Что проверяем:**
- Ищем все APPROVED бронирования для этой комнаты
- Проверяем пересечение дат (используется правильный предикат: `startDate < other.endDate AND other.startDate < endDate`)
- Если конфликт найден → отклоняем новое бронирование

**Индекс для оптимизации:**

```sql
CREATE INDEX idx_bookings_room_status_dates
    ON bookings (room_id, status, start_date, end_date);
```

Это позволяет быстро найти конфликты без full table scan.

---

## Технологический стек

### Backend

| Слой | Технология | Версия | Назначение |
|------|-----------|--------|-----------|
| **Framework** | Spring Boot | 3.5.12 | REST API, dependency injection, transactions |
| **Web** | Spring Web (embedded Tomcat) | - | HTTP server |
| **Data** | Spring Data JPA | - | ORM, repositories |
| **ORM** | Hibernate | - | Object-relational mapping |
| **Database** | PostgreSQL | 16 | Production database |
| **Migrations** | Flyway | - | Database versioning |
| **Validation** | Jakarta Validation | - | Bean validation (JSR 380) |
| **Testing** | JUnit 5 + AssertJ + Mockito | - | Unit & integration tests |
| **Build** | Maven | 3.9.12 | Build automation |

### Инструменты разработки

- **IDE** — IntelliJ IDEA (рекомендуется)
- **Java** — JDK 17+
- **Git** — VCS
- **Docker** — для запуска PostgreSQL локально
- **Postman/curl** — для тестирования API

### Планируемый стек (фазы 2-3)

| Компонент | Технология | Для чего |
|-----------|-----------|---------|
| **Message Broker** | Apache Kafka | Асинхронное взаимодействие между сервисами |
| **Caching** | Redis | Кэширование результатов поиска, хранение JWT |
| **API Gateway** | Spring Cloud Gateway | Роутинг, аутентификация, rate limiting |
| **Service Registry** | Consul / Eureka | Service discovery для микросервисов |
| **Tracing** | Zipkin + Micrometer | Distributed tracing, debugging |
| **Metrics** | Prometheus + Grafana | Мониторинг производительности |
| **Logging** | ELK Stack (Elasticsearch, Logstash, Kibana) | Централизованные логи |
| **Container Orchestration** | Kubernetes | Деплой и масштабирование |

---

## Как запустить проект

### Предварительно

```bash
# 1. Клонировать репозиторий
git clone <repo>
cd booking-platform

# 2. Установить JDK 17+
java -version

# 3. Запустить PostgreSQL через Docker
docker-compose up -d

# 4. Проверить что БД поднялась
docker-compose ps
```

### Запуск приложения

```bash
# Option 1: Через Maven (используется встроенный Maven wrapper)
./mvnw spring-boot:run

# Option 2: Через IDE (запустить BookingPlatformApplication.java)

# Option 3: Собрать JAR и запустить
./mvnw clean package
java -jar app/target/app-0.0.1-SNAPSHOT.jar
```

Приложение будет доступно на `http://localhost:8080`

### Проверить что работает

```bash
# Создать бронирование
curl -X POST http://localhost:8080/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "guestId": "550e8400-e29b-41d4-a716-446655440000",
    "roomId": "550e8400-e29b-41d4-a716-446655440001",
    "startDate": "2026-05-10",
    "endDate": "2026-05-15",
    "amount": 100.00,
    "currency": "USD"
  }'

# Получить бронирование
curl http://localhost:8080/bookings/{id}

# Одобрить бронирование
curl -X POST http://localhost:8080/bookings/{id}/approve
```

---

## Паттерны проектирования (GoF)

### Creational

| Паттерн | Где | Зачем |
|---------|-----|-------|
| **Factory Method** | `Booking.create(...)` | Гарантировать инварианты при создании (например, что статус всегда PENDING) |
| **Builder** | Для фильтров поиска | Удобно строить сложные query объекты |

### Structural

| Паттерн | Где | Зачем |
|---------|-----|-------|
| **Adapter** | Адаптеры в persistence layer | Конвертировать между JPA Entity и доменной моделью |
| **Facade** | Use Case интерфейсы | Скрыть сложность сервиса за простым интерфейсом |
| **Decorator** | Логирование, метрики | Добавлять функциональность без изменения кода |

### Behavioral

| Паттерн | Где | Зачем |
|---------|-----|-------|
| **Observer** | Доменные события | Слабая связанность между модулями |
| **State** | BookingStatus enum | Управление переходами состояний |
| **Strategy** | Ценообразование (TODO) | Легко менять стратегии расчета цены |
| **Template Method** | Базовые классы | Скелет алгоритма, детали в подклассах |
| **Chain of Responsibility** | Валидаторы (TODO) | Цепочка проверок при создании объекта |

---

## Паттерны микросервисов

Хотя сейчас это монолит, архитектура подготовлена для миграции:

| Паттерн | Реализация | Фаза |
|---------|-----------|------|
| **Outbox** | Event publishing via domain events | 1 → 2 (добавим Kafka) |
| **Saga** | Для координации транзакций между сервисами | 2 → 3 |
| **Strangler Fig** | Постепенный вынос модулей в микросервисы | 1 → 2 → 3 |
| **Circuit Breaker** | Resilience4j для межсервисных вызовов | 2 → 3 |
| **API Gateway** | Spring Cloud Gateway | 2 → 3 |
| **Service Registry** | Eureka/Consul для discovery | 3 |

---

## Тестирование

### Unit Tests

```bash
# Тесты для доменных моделей и логики
booking-module/src/test/java/com/ilyanin/booking_platform/booking/domain/

BookingTest.java              — тесты агрегата Booking
BookingStatusTest.java        — тесты статусной машины
BookingServiceTest.java       — тесты сервиса (с mock'ами)
```

**Запуск:**
```bash
./mvnw test
```

### Integration Tests

```bash
# Тесты с реальной БД (в памяти, например H2)
# TODO: добавить тесты с @DataJpaTest и @SpringBootTest
```

### Concurrency Tests

```bash
# Тесты на потокобезопасность
# TODO: добавить тесты с ExecutorService и CountDownLatch
```

**Пример (как должен выглядеть):**
```java
@Test
void shouldPreventDoubleBookingUnderConcurrency() {
    ExecutorService executor = Executors.newFixedThreadPool(2);
    CountDownLatch latch = new CountDownLatch(2);
    
    // Запустить два потока, пытающихся одобрить на одни даты
    executor.submit(() -> {
        latch.countDown();
        latch.await();
        bookingService.approve(booking1.getId());  // ← один пройдет
    });
    
    executor.submit(() -> {
        latch.countDown();
        latch.await();
        assertThrows(IllegalArgumentException.class,
            () -> bookingService.approve(booking2.getId())  // ← второй упадет
        );
    });
    
    executor.awaitTermination(5, TimeUnit.SECONDS);
}
```

---

## Документация и решения

- **docs/DECISIONS.md** — архитектурные решения, рассуждения, trade-offs
- **README.md** (этот файл) — общий обзор и гайд по запуску

---

## Планы развития

### Фаза 2 (Infrastructure)
- [ ] Kafka интеграция (вместо Spring Events)
- [ ] Redis кэширование
- [ ] Spring Security + JWT
- [ ] Distributed Tracing (Zipkin)
- [ ] Prometheus metrics + Grafana

### Фаза 3 (Microservices)
- [ ] API Gateway (Spring Cloud Gateway)
- [ ] Вынесение Notification Service
- [ ] Вынесение Auth Service
- [ ] Вынесение Property Service
- [ ] Kubernetes deployment

### Улучшения Booking Module
- [ ] Добавить UNIQUE constraint для потокобезопасности
- [ ] Реализовать concurrency tests
- [ ] Добавить custom exception для конфликтов

### Реализация остальных модулей
- [ ] **Property Module** — управление объектами, доступностью, ценами
- [ ] **User Module** — профили гостей и хозяев
- [ ] **Auth Module** — регистрация, логин, JWT
- [ ] **Notification Module** — email/push уведомления

---

## Ресурсы и источники вдохновения

### Domain-Driven Design
- **"Domain-Driven Design" (Eric Evans)** — классическая книга по DDD
- **"Implementing Domain-Driven Design" (Vaughn Vernon)** — практическое применение
- **DDD Community** — https://dddcommunity.org

### Clean Architecture
- **"Clean Architecture" (Robert C. Martin)** — принципы чистой архитектуры
- **Hexagonal Architecture (Alistair Cockburn)** — Ports & Adapters

### Микросервисы и паттерны
- **"Microservices Patterns" (Chris Richardson)** — паттерны для микросервисов
- **"Building Microservices" (Sam Newman)**

### Потокобезопасность в Java
- **"Java Concurrency in Practice" (Brian Goetz)** — глубокий dive в threading
- **PostgreSQL Documentation** — о locks и isolation levels

---

## Автор

Проект создан как учебная работа для демонстрации навыков enterprise Java разработки.

---

## Лицензия

MIT License — используется для образовательных целей.
