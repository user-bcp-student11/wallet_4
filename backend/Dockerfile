FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY backend/pom.xml backend/pom.xml
COPY client/openapi.json client/openapi.json
COPY backend/src backend/src
RUN mvn -B -ntp -f backend/pom.xml package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build --chown=10001:10001 /workspace/backend/target/quarkus-app/ /app/
ENV QUARKUS_HTTP_HOST=0.0.0.0
EXPOSE 8080
USER 10001:10001
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/quarkus-run.jar"]