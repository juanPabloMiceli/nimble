#!/bin/bash
./gradlew clean
./gradlew format
./gradlew build
docker build -t nimble2022/nimble .