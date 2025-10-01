FROM openjdk:latest

# Update the source filename to use the new version number
COPY ./target/sem-group-project-0.1.0.2-jar-with-dependencies.jar /tmp/app.jar

WORKDIR /tmp

ENTRYPOINT ["java", "-jar", "app.jar"]