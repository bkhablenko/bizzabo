FROM eclipse-temurin:17.0.6_10-jdk-alpine

# https://github.com/open-telemetry/opentelemetry-java/blob/main/sdk-extensions/autoconfigure/README.md
ENV OTEL_SERVICE_NAME=bizzabo
ENV OTEL_METRICS_EXPORTER=none

ARG JAR_FILE="./build/libs/bizzabo-0.1.0-SNAPSHOT.jar"

RUN apk update && \
    apk add --no-cache curl && \
    curl -sL https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.25.0/opentelemetry-javaagent.jar -o /opentelemetry-javaagent.jar && \
    addgroup -S bizzabo && \
    adduser -S appuser -G bizzabo

USER appuser:bizzabo

COPY "$JAR_FILE" /app.jar

HEALTHCHECK --interval=10s --timeout=5s --start-period=30s \
    CMD curl http://localhost:8080/health || exit 1

EXPOSE 8080

ENTRYPOINT ["java", "-javaagent:/opentelemetry-javaagent.jar", "-jar", "/app.jar"]
