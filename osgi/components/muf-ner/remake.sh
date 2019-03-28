#!/usr/bin/env bash

component="MUF NER Component.."

echo "Cleaning $component"
./clean.sh

echo "Building $component"
./make.sh

echo "Running $component"
./run.sh

echo "All done."
