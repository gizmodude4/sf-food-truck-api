FROM openjdk:11.0.7-jre-slim-buster
COPY target/application*.jar /application.jar
EXPOSE 8080 8081
CMD ["java", "-jar", "/application.jar", "server", "config.yaml"]