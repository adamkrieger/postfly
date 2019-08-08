#!/usr/bin/env bash

# Use the following if you want a locally-running
# docker container mapped to the current directory.

docker run -it --rm \
    --name postfly-test-inst \
    -e PGDATA=/db \
    -e POSTGRES_DB=testdbname \
    -e POSTGRES_USER=postgres \
    -e POSTGRES_PASSWORD=testdbpass \
    -v `pwd`/db:/db \
    -v `pwd`/scripts:/scripts \
    -p 5432:5432 \
    -p 8080:8080 \
    -u postgres \
    akrieger/postfly:latest