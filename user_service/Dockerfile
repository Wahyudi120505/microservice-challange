# Base image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy build jar ke container
COPY target/*.jar app.jar

# Expose port sesuai service
ARG SERVICE_PORT
EXPOSE ${SERVICE_PORT}

# Jalankan aplikasi
ENTRYPOINT ["java","-jar","/app/app.jar"]
