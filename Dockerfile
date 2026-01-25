FROM maven:3.9.6-eclipse-temurin-21 AS build

COPY src /app/src
COPY pom.xml /app

WORKDIR /app

RUN mvn clean package -DskipTests=true

FROM eclipse-temurin:21-jre-alpine

COPY --from=build app/target/flashsale-0.0.1-SNAPSHOT.jar flashsale.jar

ENTRYPOINT ["java", "-jar", "/flashsale.jar"]
