FROM openjdk:17-slim

# Environment variable for MySQL host (optional)
ENV MYSQL_HOST=localhost

# Expose ports for both application and MySQL
EXPOSE 2000 3306

# Copy your application JAR
ADD target/devops-integration.jar devops-integration.jar

# Download and configure MySQL (using apk for Alpine Linux)
RUN apk add --no-cache mysql-client mysql-server

# Create the database (optional)
RUN mysql -u root -pguddu1777 -e "CREATE DATABASE orderease CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# Entrypoint for your application with environment variables
ENTRYPOINT [ "java", "-jar", "/devops-integration.jar", \
             "-Dspring.datasource.url=jdbc:mysql://${MYSQL_HOST}:3306/orderease", \
             "-Dspring.datasource.username=root", \
             "-Dspring.datasource.password=guddu1777" ]


# FROM openjdk:17-slim

# EXPOSE 2000

# ADD target/devops-integration.jar devops-integration.jar
# ENTRYPOINT ["java", "-jar","/devops-integration.jar"]
