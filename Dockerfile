FROM amazoncorretto:17

COPY ./target/sem-group-project-1.0-SNAPSHOT-jar-with-dependencies.jar /tmp/app.jar

WORKDIR /tmp

ENTRYPOINT ["java", "-jar", "app.jar"]
