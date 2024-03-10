./gradlew clean bootJar
docker build -t tech-resume:latest .
docker compose up