FROM openjdk:17-slim

EXPOSE 2000

ADD target/devops-integration.jar devops-integration.jar
ENTRYPOINT ["java", "-jar","/devops-integration.jar"]