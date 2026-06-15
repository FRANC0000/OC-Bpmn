FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /build

# Cache layer: dependencies
COPY pom.xml .
COPY bpm-common/pom.xml bpm-common/pom.xml
COPY bpm-tenant-context/pom.xml bpm-tenant-context/pom.xml
COPY bpm-process-context/pom.xml bpm-process-context/pom.xml
COPY bpm-document-context/pom.xml bpm-document-context/pom.xml
COPY bpm-security-context/pom.xml bpm-security-context/pom.xml
COPY bpm-infrastructure/pom.xml bpm-infrastructure/pom.xml
COPY bpm-api/pom.xml bpm-api/pom.xml

RUN mvn dependency:go-offline -q

# Build: copy source and package
COPY . .
RUN mvn clean package -DskipTests -q

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S bpm && adduser -S bpm -G bpm

COPY --from=build /build/bpm-api/target/bpm-api-*.jar app.jar

USER bpm

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
