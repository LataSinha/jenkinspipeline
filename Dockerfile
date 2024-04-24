FROM openjdk:17
WORKDIR /app
COPY /target/* /app/spring-boot-application.jar
CMD ["java", "-jar", "spring-boot-application.jar"]
