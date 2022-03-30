FROM openjdk:8-jdk-alpine
VOLUME /tmp
#ADD build/libs/*.jar app.jar
ADD ${JAR_LIB_FILE} lib/
ENTRYPOINT ["java","-jar","app.jar"]
