FROM openjdk:11-jdk-alpine
VOLUME /tmp
ADD build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
