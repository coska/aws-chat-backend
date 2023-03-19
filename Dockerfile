FROM amazoncorretto:17
WORKDIR /app
RUN pwd
RUN ls -d **/*
COPY /app/target/aws-0.0.1-SNAPSHOT.jar aws-chat-backend.jar
ENTRYPOINT ["java", "-jar", "aws-chat-backend.jar"]
