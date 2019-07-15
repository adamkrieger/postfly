FROM postgres:10-alpine

RUN apk update
RUN apk add -u \
    openjdk8-jre \
    curl

WORKDIR /home/tmp

RUN ["curl", "-O", "https://repo1.maven.org/maven2/org/flywaydb/flyway-commandline/5.2.4/flyway-commandline-5.2.4-linux-x64.tar.gz"]
RUN ["tar", "-C", "/usr/local/bin", "-xzf", "flyway-commandline-5.2.4-linux-x64.tar.gz"]
RUN ["ln", "-s", "/usr/local/bin/flyway-5.2.4/flyway", "/usr/bin"]

# Remove any JRE that comes with flyway. Prefer to default to apk's jre8 installed above.
RUN ["rm", "-rf", "/usr/local/bin/flyway-5.2.4/jre"]
# Remove working files
RUN ["rm", "-rf", "/home/tmp"]

VOLUME /db
VOLUME /scripts

CMD ["postgres"]