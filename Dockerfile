# ---------- Build Stage ----------
FROM gradle:8.7.0-jdk21 AS builder

WORKDIR /app

# Copy Gradle files first for layer caching (Kotlin DSL: *.gradle.kts)
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./

RUN chmod +x gradlew

# Download dependencies first (better caching)
RUN ./gradlew dependencies --no-daemon

# Copy remaining source code
COPY src src

# Build application
RUN ./gradlew bootJar --no-daemon -x test


# ---------- Runtime Stage ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
