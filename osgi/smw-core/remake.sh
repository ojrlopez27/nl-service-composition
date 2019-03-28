#!/usr/bin/env bash

project="Semantic Middleware Core.."

echo "Killing existing processes.."
./sweepPorts.sh

echo "Cleaning $project"
./clean.sh

echo "Building $project"
./make.sh

echo "Running $project"
./run.sh

echo "All done."
