FROM amazoncorretto:17
VOLUME /tmp
COPY target/aws-0.0.1-SNAPSHOT.jar aws-chat-backend.jar
ENTRYPOINT ["java", "-jar", "aws-chat-backend.jar"]
