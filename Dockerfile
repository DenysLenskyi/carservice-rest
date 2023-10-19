FROM openjdk:21
MAINTAINER lenskyi
COPY target/javaspring-carservice.jar carservice.jar
ENTRYPOINT ["java", "-jar", "carservice.jar"]
EXPOSE 8080