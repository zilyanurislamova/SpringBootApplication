FROM openjdk:17.0.1-jdk-slim

ENV POSTGRES_DSN=postgresql://postgres:password@db/postgres
ENV POSTGRES_SERVER=db
ENV POSTGRES_PORT=5432
ENV POSTGRES_DB=postgres
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=password

WORKDIR /opt/app

COPY build.gradle gradlew gradlew.bat settings.gradle ./
COPY gradle gradle
COPY src src

RUN ./gradlew build -x test && cp build/libs/*SNAPSHOT.jar yandex-lavka.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "yandex-lavka.jar"]