#!/bin/bash
./gradlew clean
./gradlew build
docker build -t nimble2022/nimble .