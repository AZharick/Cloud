FROM openjdk:20-slim
EXPOSE 8080
COPY target/cloud-0.0.1-SNAPSHOT.jar cloud.jar
CMD ["java", "-jar", "cloud.jar"]