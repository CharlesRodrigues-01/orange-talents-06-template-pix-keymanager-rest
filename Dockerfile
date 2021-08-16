FROM openjdk:11
MAINTAINER Charles Rodrigues
EXPOSE 8080
ARG JAR_FILE=build/libs/*-all.jar
COPY ${JAR_FILE} app.jar
ENV APP_NAME key-manager-rest
ENTRYPOINT ["java","-jar","/app.jar"]