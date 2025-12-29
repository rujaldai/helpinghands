# ---------- Build stage ----------
FROM --platform=linux/amd64 eclipse-temurin:25-jdk AS build

WORKDIR /app

# Copy Maven wrapper and config first
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests -Dmaven.repo.local=/root/.m2

# ---------- Run stage ----------
FROM eclipse-temurin:25-jre-jammy

WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
