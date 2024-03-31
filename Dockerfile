FROM eclipse-temurin:21.0.2_13-jdk-alpine
WORKDIR /app
COPY build/libs/reverse-proxy-APL-0.0.1-SNAPSHOT.jar /app/reverse-proxy-APL.jar
EXPOSE 8080
CMD ["java", "-jar", "reverse-proxy-APL.jar"]
