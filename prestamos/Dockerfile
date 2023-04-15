FROM eclipse-temurin:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} prestamos.jar
ENTRYPOINT ["java", "-jar", "/prestamos.jar"]