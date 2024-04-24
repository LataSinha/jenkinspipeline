FROM openjdk:17
WORKDIR /app
COPY .jenkins/workspace/MiniassignmnetDeploy/miniassignment/target/*.jar  /app/spring-boot-application.jar
CMD ["java", "-jar", "spring-boot-application.jar"]

