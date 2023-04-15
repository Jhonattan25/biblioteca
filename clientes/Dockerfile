FROM eclipse-temurin:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} clientes.jar
ENTRYPOINT ["java", "-jar", "/clientes.jar"]