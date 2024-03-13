FROM openjdk:20
ARG JAR_FILE
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]