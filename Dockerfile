FROM openjdk:17 AS builder

# Install xargs (provided by findutils) using apk for Alpine-based images
RUN apk update && apk add findutils

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

RUN chmod +x ./gradlew

RUN ./gradlew bootJar

FROM openjdk:17

COPY --from=builder build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
