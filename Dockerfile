FROM openjdk:latest
# Copy the self-contained JAR file created by Maven package goal
# NOTE: This name must match the Maven output
COPY ./target/sem-group-project-0.1.0.3-jar-with-dependencies.jar /tmp/app.jar
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "app.jar"]