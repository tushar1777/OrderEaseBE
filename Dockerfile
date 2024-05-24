# Start with a base image containing Java runtime
FROM openjdk:17-jdk-alpine

# Set environment variables
ENV DB_USERNAME=avnadmin
ENV DB_PASSWORD=AVNS_BR5Y0Zti5uXS7fpxBB7

# Set the working directory
WORKDIR /app

# Expose the port your application runs on
EXPOSE 5454

# Copy the jar file from the target directory into the container
COPY target/devops-integration.jar /app/app.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]


# FROM openjdk:17-slim
# # Expose ports for both application 
# EXPOSE 5454

# # Set the working directory inside the container
# WORKDIR /app

# # Copy your application JAR
# ADD target/devops-integration.jar devops-integration.jar
# # Entrypoint for your application with environment variables
# ENTRYPOINT [ "java", "-jar", "/devops-integration.jar" ]

# # Run the application
# ENTRYPOINT ["java", "-jar", "app.jar"]

# FROM openjdk:17-slim

# EXPOSE 2000

# ADD target/devops-integration.jar devops-integration.jar
# ENTRYPOINT ["java", "-jar","/devops-integration.jar"]
