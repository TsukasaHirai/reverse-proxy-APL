FROM eclipse-temurin:21.0.5_11-jdk-ubi9-minimal
RUN ./mvnw package
WORKDIR /app
COPY target/reverse-proxy-APL-0.0.1-SNAPSHOT.jar /app/reverse-proxy-APL.jar
EXPOSE 8080
CMD ["java", "-jar", "reverse-proxy-APL.jar"]
