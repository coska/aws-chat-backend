FROM amazoncorretto:17
RUN pwd
RUN ls
COPY /app/target/aws-0.0.1-SNAPSHOT.jar aws-chat-backend.jar
ENTRYPOINT ["java", "-jar", "aws-chat-backend.jar"]
