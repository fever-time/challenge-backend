FROM openjdk:8-jdk-alpine
ENV TZ=Asia/Seoul
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]