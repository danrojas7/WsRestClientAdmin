FROM openjdk:8-alpine as build-stage

RUN apk update && apk add maven

WORKDIR /opt/app/build

COPY . .

RUN mvn install


FROM openjdk:8-alpine

RUN apk update && apk add bash curl maven tzdata vim

ARG TZ=America/Bogota
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /opt/dist/app
RUN mkdir ./config

ARG APP=
COPY --from=build-stage /opt/app/build/target/${APP}.jar .

ARG CONFIG_PATH=
COPY --from=build-stage /opt/app/build/${CONFIG_PATH} ./config

RUN ln -s ./${APP}.jar ./APP.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "./APP.jar"]

