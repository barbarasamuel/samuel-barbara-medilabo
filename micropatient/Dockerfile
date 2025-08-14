# Étape 1 : image de base avec Maven pour construire l'application
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : image de base avec Java 21 pour l'exécution
FROM eclipse-temurin:21-jdk-alpine

# Étape 3 : répertoire de travail
WORKDIR /app

# Étape 4 : copie du fichier jar depuis l'étape de build
COPY --from=build /app/target/micropatient-0.0.1-SNAPSHOT.jar micropatient.jar

# Étape 4 : expose le port 8999 (par défaut pour Spring Boot)
EXPOSE 8999

# Étape 5 : commande pour exécuter l’application
ENTRYPOINT ["java", "-jar", "micropatient.jar"]