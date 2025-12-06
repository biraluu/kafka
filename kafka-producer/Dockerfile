FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

# Install Maven
RUN apk add --no-cache maven

# Build the application
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/kafka-producer-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]