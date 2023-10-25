FROM maven:3.8.3-openjdk-17-slim AS builder
WORKDIR /app
COPY .mvn/ .mvn
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/target/javaspring-carservice.jar carservice.jar
ENTRYPOINT ["java", "-jar", "carservice.jar"]
EXPOSE 8080