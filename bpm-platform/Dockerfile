FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY bpm-api/target/bpm-api-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
