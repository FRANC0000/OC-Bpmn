FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml ./
COPY bpm-common/pom.xml bpm-common/
COPY bpm-tenant-context/pom.xml bpm-tenant-context/
COPY bpm-process-context/pom.xml bpm-process-context/
COPY bpm-document-context/pom.xml bpm-document-context/
COPY bpm-security-context/pom.xml bpm-security-context/
COPY bpm-infrastructure/pom.xml bpm-infrastructure/
COPY bpm-api/pom.xml bpm-api/
RUN mvn dependency:go-offline -B -q

COPY . .
RUN mvn clean package -DskipTests -B -q

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /build/bpm-api/target/bpm-api-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
