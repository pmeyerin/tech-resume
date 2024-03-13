FROM openjdk:20
#ARG JAR_FILE
#COPY build/libs/*.jar app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

WORKDIR /opt/app
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:20
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/resume.jar
ENTRYPOINT ["java", "-jar", "/app/resume.jar"]