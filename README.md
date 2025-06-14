# Hotel Service

Сервис для управления заселением/выселением людей в отель

## Технологии
- **Java 17** + **Spring 6**
- **MySQL** (хранение данных)
- **Liquibase** (миграции БД)
- **MapStruct** (Конвертация ДТО)
- **JJWT** (JWT аутентификация)

## Быстрый старт
- Скачайте и установите контейнер сервлетов [Tomcat](https://tomcat.apache.org/download-10.cgi)
- Клонируйте репозиторий:
  ```bash
  git clone https://github.com/{username}/Hotel-Service.git
  cd Hotel-Service
  ```
- Соберите приложение:
  ```bash
  mvn clean install
  ```
- Запуск приложения:
  ```
  mvn tomcat7:run
  ```
- Завершение приложения:
  ```
  mvn tomcat7:stop
  ```
