FROM amazoncorretto:17
WORKDIR /applications
COPY target/aws-0.0.1-SNAPSHOT.jar /applications/aws-chat-backend.jar
##COPY cert/coska.com.p12 /applications/cert/coska.com.p12
COPY . /applications
ENTRYPOINT ["java", "-jar", "aws-chat-backend.jar"]
