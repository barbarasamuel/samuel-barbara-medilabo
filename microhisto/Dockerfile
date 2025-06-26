# Étape 1 : image de base avec Java 21
FROM eclipse-temurin:21-jdk-alpine

# Étape 2 : répertoire de travail
WORKDIR /app

# Étape 3 : copie du fichier jar dans le conteneur
COPY target/microhisto-0.0.1-SNAPSHOT.jar microhisto.jar

# Étape 4 : expose le port 8998 (par défaut pour Spring Boot)
EXPOSE 8998

# Étape 5 : commande pour exécuter l’application
ENTRYPOINT ["java", "-jar", "microhisto.jar"]