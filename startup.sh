#!/bin/ash

set -m

docker-entrypoint.sh postgres &

/app/startpostflyapi.sh

fg %1

