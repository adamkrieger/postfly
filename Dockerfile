FROM postgres:10-alpine

RUN apk update
RUN apk add -u \
    openjdk8-jre \
    curl

COPY ./postflysc/target/postflysc-0.0.1-SNAPSHOT.jar /app/postflysc.jar
COPY ./startup.sh /app/startup.sh
COPY ./startpostflyapi.sh /app/startpostflyapi.sh

VOLUME /db
VOLUME /scripts

ENTRYPOINT [""]
CMD ["/app/startup.sh"]
