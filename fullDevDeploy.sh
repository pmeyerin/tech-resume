cd resume
./gradlew clean bootJar
docker build -t tech-resume:latest .
cd ../resume-ui
docker build -t resume-ui:latest .
cd ../
docker compose up
