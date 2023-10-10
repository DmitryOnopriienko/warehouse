FROM openjdk:17-slim

WORKDIR /app
ARG JAR_FILE=wm-system/build/libs/wm-system-*.jar
COPY ${JAR_FILE} ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
