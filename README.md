# Вступительное задание Java
### Запуск решения с помощью Dockerfile:
1. Сбилдить image: `docker build -t application-image .`
2. Создать network: `docker network create enrollment`
3. Запустить db: `docker run --name db --network enrollment -e POSTGRES_PASSWORD=password -d postgres:15.2-alpine`
4. Запустить application: `docker run --name application --network enrollment -p 8080:8080 application-image`

### Запуск решения с помощью docker-compose.yml:
1. `docker-compose up`

### Запуск тестов с помощью Apache JMeter:
1. Путь к файлу: `src/test/java/APItests.jmx`
2. Thread `Yandex Lavka` - позитивные сценарии;
3. Thread `Validation` - негативные сценарии;
4. `View Results Tree` - результаты прогонов.