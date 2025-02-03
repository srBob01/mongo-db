# Используем OpenJDK 21 с минимальным размером образа
FROM openjdk:21-jdk-slim-buster

# Автор
LABEL authors="Nikita"

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем только Gradle файлы отдельно (ускоряет кэширование зависимостей)
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle

# Даем права на выполнение Gradle Wrapper
RUN chmod +x gradlew

# Загружаем зависимости, но не собираем проект (ускоряет кэширование)
RUN ./gradlew dependencies --no-daemon

# Копируем весь код проекта
COPY . .

# Собираем приложение **без тестов**
RUN ./gradlew clean build -x test --no-daemon

# Запускаем приложение
CMD ["java", "-jar", "./build/libs/application-0.0.1-SNAPSHOT.jar"]
