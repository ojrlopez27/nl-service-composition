#!/bin/bash

AXIS2_NAME=axis2
AXIS2_VERSION=1.7.8
AXIS2=$AXIS2_NAME-$AXIS2_VERSION
AXIS2_START=$AXIS2/bin/axis2server.sh

# Start Axis2 server
echo "Starting Apache Axis2.."
$AXIS2_START

