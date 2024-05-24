FROM tomcat:9.0

# Copy the WAR file to the webapps directory of Tomcat
COPY target/devops-integration.war /usr/local/tomcat/webapps/

# Expose the port your application runs on
EXPOSE 5454

# Run Tomcat
CMD ["catalina.sh", "run"]

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
