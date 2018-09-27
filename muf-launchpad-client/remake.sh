#!/usr/bin/env bash

echo "Cleaning MUF Launchpad Client.."
./clean.sh

echo "Building MUF Launchpad Client.."
./make.sh

echo "Running MUF Launchpad Client.."
./run.sh
echo "All done."
