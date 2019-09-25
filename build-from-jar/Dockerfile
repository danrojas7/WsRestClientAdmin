FROM openjdk:8-alpine

RUN apk update && apk add vim && apk add curl \
    && apk add maven && apk add bash && apk add tzdata

ENV TZ=America/Bogota
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /opt/dist/app
WORKDIR /opt/dist/app
COPY . .
#RUN deb http://security.debian.org/ stretch/updates main contrib non-free
#RUN apt-get update && apt-get install vim && apt-get install maven

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "./WsRestClientAdmin-0.0.1-SNAPSHOT.jar"]
