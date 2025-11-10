FROM eclipse-temurin:17-jdk
WORKDIR /app
ARG JAR_FILE=target/PatientService-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app.jar
EXPOSE 8081
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]


