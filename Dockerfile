FROM openjdk:17 as builder
WORKDIR /app
COPY .mvn .mvn
COPY mvnw mvnw
COPY pom.xml pom.xml
COPY src src
RUN ./mvnw clean package -DskipTests
FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/target/javaspring-carservice.jar carservice.jar
ENTRYPOINT ["java", "-jar", "carservice.jar"]
EXPOSE 8080