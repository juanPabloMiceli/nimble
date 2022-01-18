#!/bin/bash
./gradlew clean
./gradlew spotlessApply
./gradlew build
docker build -t nimble2022/nimble .