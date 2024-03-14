FROM eclipse-temurin:20 AS build

COPY . /home/gradle/src
WORKDIR /home/gradle/src
RUN ./gradlew shadowJar --no-daemon

FROM eclipse-temurin:20

EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/resume.jar
ENTRYPOINT ["java", "-jar", "/app/resume.jar"]