FROM openjdk:21-jdk
COPY target/Server.jar .
EXPOSE 8080
ENTRYPOINT ["java","-jar","/Server.jar"]