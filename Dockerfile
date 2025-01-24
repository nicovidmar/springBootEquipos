FROM eclipse-temurin:17

LABEL author=nicolas.vidmar

COPY target/equipos-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
