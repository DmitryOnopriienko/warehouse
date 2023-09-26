FROM openjdk:17-slim

WORKDIR /app
ARG JAR_FILE=build/libs/warehouse-*.jar
COPY ${JAR_FILE} ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]