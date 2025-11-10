#FROM eclipse-temurin:17-jdk-alpine
#WORKDIR /app
#COPY target/PrescriptionService-0.0.1-SNAPSHOT.jar app.jar
#EXPOSE 8085
#ENTRYPOINT ["java","-jar","app.jar"]


FROM eclipse-temurin:17-jdk
WORKDIR /app
ARG JAR_FILE=target/PrescriptionService-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app.jar
EXPOSE 8084
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]