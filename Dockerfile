FROM maven:3.6.3-openjdk-11
COPY application/target/application*.jar /application.jar
CMD ["java", "-jar", "/application.jar", "server", "config.yaml"]