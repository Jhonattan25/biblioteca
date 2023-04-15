FROM eclipse-temurin:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} libros.jar
ENTRYPOINT ["java", "-jar", "/libros.jar"]