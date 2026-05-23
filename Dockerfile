FROM        docker.io/library/openjdk:21-ea AS builder
WORKDIR     /app
COPY        ./ /app/
RUN         chmod +x ./gradlew && ./gradlew bootJar --no-daemon -x test

#FROM        sonarsource/sonar-scanner-cli AS sonar-scanner
#WORKDIR     /usr/src
#COPY        --from=builder /app /usr/src
#RUN         sonar-scanner \
#            -Dsonar.host.url=http://172.31.17.79:9000 \
#            -Dsonar.login=admin -Dsonar.password=admin123 -Dsonar.qualitygate.wait=true \
#            -Dsonar.projectKey=portfolio-service \
#            -Dsonar.sources=. -Dsonar.java.binaries=./build/classes && \
#            touch /tmp/scan-success

FROM        docker.io/redhat/ubi9:latest
RUN         dnf install java-21-openjdk.x86_64 -y
#COPY        --from=sonar-scanner /tmp/scan-success /tmp/
COPY        --from=builder  /app/build/libs/*.jar portfolio-service.jar
ENTRYPOINT  [ "java", "-jar", "./portfolio-service.jar" ]