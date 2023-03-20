FROM amazoncorretto:17
WORKDIR /applications
COPY target/aws-0.0.1-SNAPSHOT.jar /applications/aws-chat-backend.jar
ENTRYPOINT ["java", "-jar", "aws-chat-backend.jar"]
