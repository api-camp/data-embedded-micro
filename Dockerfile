FROM openjdk:8
MAINTAINER david.e

ARG do_spaces_token
ARG do_spaces_secret

ENV do_spaces_token=$do_spaces_token
ENV do_spaces_secret=$do_spaces_secret

WORKDIR /
COPY app.jar /home/app.jar
EXPOSE 8080
CMD ["java","-jar","/home/app.jar"]
