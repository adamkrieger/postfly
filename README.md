# PostFly

Postgres + Flyway + Docker

[![Build Status](https://travis-ci.org/adamkrieger/postfly.svg?branch=master)](https://travis-ci.org/adamkrieger/postfly)

## Disclaimer

This project is only for use in rapid prototyping applications at the moment. There are currently several security compromises made in order to improve minimum-time-to-run.

## Setup

Use postfly to set up a postgres database for rapidly iterating in Dockerized environments.

Directory Setup:

```text
svcDB/
  - db/   <--generated on startup if not present
  - scripts/
    - migrations/
      - V001_firstmigration.sql
      - V00N_othermigrations.sql
  - start.sh
```

`start.sh`

```bash
#!/usr/bin/env bash

# Use the following if you want a locally-running
# docker container mapped to the current directory.

docker run -it --rm \
    --name postfly-inst \
    -e PGDATA=/db \
    -e POSTGRES_DB=yourdb \
    -e POSTGRES_USER=postgres \
    -e POSTGRES_PASSWORD=<PASSWORD> \
    -v `pwd`/db:/db \
    -v `pwd`/scripts:/scripts \
    -p 5432:5432 \  <-- Exposed port to the postgres db
    -p 8080:8080 \  <-- Exposed port to the flyway wrapper REST API
    -u postgres \
    akrieger/postfly
```

## Usage

Execute common operations on the postgres database by accessing the exposed REST API.

- `GET /health`
  - returns a simple response when the REST API is online
- `GET /revision`
  - returns the current revision, or an error if the database hasn't been 'baselined' by Flyway
- `POST /migrate`
  - uses flyway to migrate the database to the latest revision, baselining the database if necessary
