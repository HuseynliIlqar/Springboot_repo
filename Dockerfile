# ─── Stage 1: Build ───────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Cache Maven dependencies
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline -q

# Build the JAR
COPY src/ src/
RUN ./mvnw package -DskipTests -q

# ─── Stage 2: Runtime ─────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# SQLite DB will be stored in a volume so data persists across restarts
VOLUME /app/data

# Override the datasource URL to point inside the volume
ENV SPRING_DATASOURCE_URL=jdbc:sqlite:/app/data/portfolio.db

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
