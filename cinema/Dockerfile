FROM openjdk:17-jdk-alpine
COPY target/*.jar app.jar

RUN sleep 10
ENTRYPOINT ["java","-jar","/app.jar"]

#docker network create --driver bridge cinema-network