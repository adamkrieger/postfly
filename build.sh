#!/usr/bin/env bash
pushd postflysc
mvn package
popd

docker build . -t postfly
