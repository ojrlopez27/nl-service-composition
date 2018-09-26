#!/bin/bash

TOMCAT_NAME=apache-tomcat
TOMCAT_VERSION=9.0.12
TOMCAT=$TOMCAT_NAME-$TOMCAT_VERSION
TOMCAT_START=$TOMCAT/bin/startup.sh
TOMCAT_STOP=$TOMCAT/bin/shutdown.sh

# Restart Tomcat
echo "Stopping Tomcat.."
$TOMCAT_STOP

echo "Starting Tomcat.."
$TOMCAT_START
