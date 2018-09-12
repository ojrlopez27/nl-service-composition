#!/usr/bin/env bash

echo "Killing existing processes.."
./kill-555*-port-processes.sh

echo "Cleaning MUF Launchpad.."
./clean.sh

echo "Building MUF Launchpad.."
./make.sh

echo "Running MUF Launchpad.."
./run.sh
echo "All done."
