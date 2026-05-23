plugins {
    java
    // 3.4.16 is not on Maven Central / Gradle Plugin Portal yet; use 3.4.5 + dependency overrides below for CVE pins
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.wmp"
version = "1.0.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

// Fixes for all listed 2026 flaws across downstream packages
extra["tomcat.version"] = "10.1.55"           // Fixes CVE-2026-41293, CVE-2026-43512, CVE-2026-34483, CVE-2026-34487, CVE-2026-41284, CVE-2026-42498, CVE-2026-43513
extra["postgresql.version"] = "42.7.11"       // Fixes CVE-2026-42198 (and legacy CVE-2025-49146)

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    // Database
    implementation("org.postgresql:postgresql") // Resolves to 42.7.11 via extra property override
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")

    // Observability
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("net.logstash.logback:logstash-logback-encoder:8.0")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("com.h2database:h2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
