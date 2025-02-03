# Expense Tracker API

## Описание проекта

Expense Tracker API — это RESTful сервис для управления расходами пользователей. Приложение позволяет добавлять, редактировать, удалять и просматривать расходы, используя базу данных MongoDB. Реализация на Java, а для тестирования используется Testcontainers.

## Основные возможности

- Работа с базой данных MongoDB
- Контейнеризация с помощью Docker и Docker Compose
- Автоматическое тестирование с Testcontainers

---

## 📜 Описание структуры

### 📂 `controller`

#### `ExpenseController.java`

- Обрабатывает HTTP-запросы и передает их в сервис.
- Основные эндпоинты:
    - `POST /api/expense` — добавление расхода.
    - `PUT /api/expense` — обновление расхода.
    - `DELETE /api/expense/{id}` — удаление расхода.
    - `GET /api/expense` — получение всех расходов.
    - `GET /api/expense/{name}` — поиск расхода по имени.

### 📂 `repository`

#### `ExpenseRepository.java`

- Реализует `MongoRepository<Expense, String>`.
- Автоматически поддерживает стандартные CRUD-операции.
- Дополнительный метод:
  ```java
  Optional<Expense> findExpenseByExpenseName(String expenseName);
  ```
  Позволяет находить расходы по их названию.

### 📂 `service`

#### `ExpenseService.java`

- Отвечает за бизнес-логику приложения.
- Основные методы:

  ```java
  public void addExpense(Expense expense) {
      expenseRepository.save(expense);
  }
  ```
  Добавляет новый расход в базу данных.

  ```java
  public void updateExpense(Expense expense) {
      Expense existing = expenseRepository.findById(expense.getId())
          .orElseThrow(() -> new IllegalArgumentException("Expense not found"));
      existing.setExpenseName(expense.getExpenseName());
      existing.setCategory(expense.getCategory());
      existing.setExpenseAmount(expense.getExpenseAmount());
      expenseRepository.save(existing);
  }
  ```
  Обновляет данные о расходе.

  ```java
  public void deleteExpense(String id) {
      expenseRepository.deleteById(id);
  }
  ```
  Удаляет расход по ID.

  ```java
  public List<Expense> getAllExpenses() {
      return expenseRepository.findAll();
  }
  ```
  Возвращает список всех расходов.

### 📂 `config`

#### `application.yml`

- Конфигурирует подключение к MongoDB через `MONGO_URI`.
- Включает автоматическое создание индексов:
  ```yaml
  spring:
    data:
      mongodb:
        uri: ${MONGO_URI}
        auto-index-creation: true
  ```


#### `init-mongo.js`

- Создает пользователя `admin` и задает права:
```javascript
// Переключаемся на базу данных 'expense_tracker'
db = db.getSiblingDB('expense_tracker');

// Создаём пользователя 'admin' с паролем 'secret' и назначаем ему роли
db.createUser({
    user: "admin",
    pwd: "secret", 
    roles: [
        { role: "readWrite", db: "expense_tracker" }, // Разрешает чтение и запись в 'expense_tracker'
        { role: "dbOwner", db: "expense_tracker" },   // Даёт контроль над схемой базы данных
        { role: "root", db: "admin" }                // Полные права администратора на уровне MongoDB
    ]
});

print("User 'admin' created successfully with full access to 'expense_tracker' database");
```

### 🔍 Разбор параметров:
- `user: "admin"` — создаёт пользователя с этим именем.
- `pwd: "secret"` — устанавливает пароль для аутентификации.
- `roles`:
  - `"readWrite"` — позволяет пользователю добавлять, изменять и удалять данные в базе `expense_tracker`.
  - `"dbOwner"` — предоставляет контроль над структурой базы данных, включая создание и удаление коллекций.
  - `"root"` — даёт полный административный доступ ко всей MongoDB.

В `docker-compose.yml` строка:
```yaml
MONGO_URI: mongodb://admin:secret@mongodb:27017/expense_tracker
```

Это указывает, что приложение Spring Boot будет подключаться к MongoDB с пользователем `admin`, паролем `secret` и базой `expense_tracker`.

---

## 🚀 Развёртывание

### 📌 Запуск

```sh
docker-compose up --build
```

### 📌 Остановка

```sh
docker-compose stop 
```

---

## 🛠 Тестирование

Тесты можно запускать вручную:

```sh
./gradlew test
```

Пример теста:

```java
@Test
void shouldSaveExpense() {
    Expense expense = new Expense(null, "Groceries", ExpenseCategory.GROCERIES, BigDecimal.valueOf(100.50));
    expenseService.addExpense(expense);
    List<Expense> expenses = expenseRepository.findAll();
    assertThat(expenses).hasSize(1);
    assertThat(expenses.getFirst().getExpenseName()).isEqualTo("Groceries");
}
```

---

## 📡 API Документация

### 1️⃣ Добавить расход

**POST** `/api/expense`

```json
{
    "expenseName": "Supermarket",
    "category": "GROCERIES",
    "expenseAmount": 150.50
}
```

### 2️⃣ Получить все расходы

**GET** `/api/expense`

### 3️⃣ Найти расход по имени

**GET** `/api/expense/{name}`

### 4️⃣ Обновить расход

**PUT** `/api/expense`

```json
{
    "id": "67a0c612bfe62a47e8d341e5",
    "expenseName": "Restaurant",
    "category": "RESTAURANTS",
    "expenseAmount": 200.00
}
```

### 5️⃣ Удалить расход

**DELETE** `/api/expense/{id}`