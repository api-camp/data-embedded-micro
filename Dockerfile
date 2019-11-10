FROM openjdk:8
MAINTAINER david.e
WORKDIR /
COPY data-embedded-micro-0.1.0.jar /home/data-embedded-micro-0.1.0.jar
EXPOSE 8080
CMD ["java","-jar","/home/data-embedded-micro-0.1.0.jar"]
