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

ARG JAR=
COPY --from=build-stage /opt/app/build/target/${JAR} .

ARG CONFIG_PATH=
COPY --from=build-stage /opt/app/build/${CONFIG_PATH} ./config

RUN ln -s ./${JAR} ./app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "./app.jar"]

