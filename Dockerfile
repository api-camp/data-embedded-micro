FROM openjdk:8
MAINTAINER david.e
WORKDIR /
COPY app.jar /home/app.jar
EXPOSE 8080
CMD ["java","-jar","/home/app.jar"]
