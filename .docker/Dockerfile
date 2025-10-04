FROM eclipse-temurin:23-jdk

WORKDIR /app

COPY build/libs/pms-property-service-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
