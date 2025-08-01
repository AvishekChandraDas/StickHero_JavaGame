#!/bin/bash

# Set Java 21 as JAVA_HOME for this project
export JAVA_HOME=/opt/homebrew/Cellar/openjdk@21/21.0.8/libexec/openjdk.jdk/Contents/Home

# Run the Stick Hero game
./gradlew run --no-daemon
