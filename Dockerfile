FROM ubuntu:20.04

# Environment variable for MySQL host (optional)
ENV MYSQL_HOST=localhost

# Expose ports for both application and MySQL
EXPOSE 2000 3306

# Copy your application JAR
ADD target/devops-integration.jar devops-integration.jar

# Update package lists
RUN apt-get update

# Install OpenJDK 17
RUN apt-get install -y openjdk-17-jdk

# Download and configure MySQL
RUN apt-get install -y mysql-server

# (Optional) Create the database after MySQL starts
CMD mysql -u root -pguddu1777 -h localhost -S /var/run/mysqld/mysqld.sock -e "CREATE DATABASE orderease CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# Entrypoint for your application with environment variables
ENTRYPOINT [ "java", "-jar", "/devops-integration.jar", \
             "-Dspring.datasource.url=jdbc:mysql://${MYSQL_HOST}:3306/orderease", \
             "-Dspring.datasource.username=root", \
             "-Dspring.datasource.password=guddu1777" ]


# FROM openjdk:17-slim

# EXPOSE 2000

# ADD target/devops-integration.jar devops-integration.jar
# ENTRYPOINT ["java", "-jar","/devops-integration.jar"]
