# syntax=docker/dockerfile:1
FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /build

RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:resolve --batch-mode -q || \
    mvn dependency:resolve --batch-mode -q

COPY . .

RUN --mount=type=cache,target=/root/.m2 \
    mvn clean package -DskipTests --batch-mode -q

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S bpm && adduser -S bpm -G bpm

COPY --from=build /build/bpm-api/target/bpm-api-*.jar app.jar

USER bpm

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
