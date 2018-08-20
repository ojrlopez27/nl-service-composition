#!/usr/bin/env bash

echo "start composition jar"
java -jar ./build/libs/edu.cmu.inmind.services.composition-1.0.jar -Xmx2048
echo "composition jar ended..."