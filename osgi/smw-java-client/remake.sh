#!/usr/bin/env bash

component="Semantic Middleware Java Client.."

echo "Cleaning $component"
./clean.sh

echo "Building $component"
./make.sh

echo "Running $component"
./run.sh

echo "All done."
