#!/bin/zsh

echo "Building..."
./gradlew clean build test
echo "Cleaning up old install..."
rm data-embedded-micro-0.1.0.jar
echo "Install..."
cp -f build/libs/data-embedded-micro-0.1.0.jar .
