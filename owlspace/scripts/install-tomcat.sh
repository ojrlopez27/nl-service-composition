#!/bin/bash

TOMCAT_NAME=apache-tomcat
TOMCAT_SERIES=9
TOMCAT_VERSION=9.0.12
TOMCAT=$TOMCAT_NAME-$TOMCAT_VERSION
TOMCAT_WEBAPPS=$TOMCAT/webapps
TOMCAT_CONFIG=$TOMCAT/conf/server.xml
TOMCAT_CONFIG_USERS=$TOMCAT/conf/tomcat-users.xml
TOMCAT_START=$TOMCAT/bin/startup.sh
TOMCAT_STOP=$TOMCAT/bin/shutdown.sh
TOMCAT_ARCHIVE=$TOMCAT.tar.gz
TOMCAT_URL=http://apache.claz.org/tomcat/tomcat-$TOMCAT_SERIES/v$TOMCAT_VERSION/bin/$TOMCAT_ARCHIVE

# Check if Tomcat already exists
if [ ! -e $TOMCAT ]; then
    if [ ! -r $TOMCAT_ARCHIVE ]; then
	if [ -n "$(which curl)" ]; then
	   echo "Downloading Tomcat using curl.."
	   curl -O $TOMCAT_URL
	elif [ -n "$(which wget)" ]; then
	    echo "Downloading Tomcat using wget.."
	    wget $TOMCAT_URL
	fi
	if [ $? -eq 0 ]; then
    	    echo "done. Tomcat downloaded."
	fi
    fi

    # Validate whether Tomcat was downloaded
    if [ ! -r $TOMCAT_ARCHIVE ]; then
	echo "Tomcat could not be downloaded." 1>&2
	echo "Verify that eiter curl or wget is installed." 1>&2
	echo "If they are, check your internet connection and try again." 1>&2
	echo "You may also download $TOMCAT_ARCHIVE and place it in this folder." 1>&2
	exit 1
    fi

    # Untar the Tomcat distribution
    tar -zxf $TOMCAT_ARCHIVE
    rm $TOMCAT_ARCHIVE
else
    echo "Tomcat exists."
fi

# Check if webapps can be hot-deployed in Tomcat
if [ ! -w $TOMCAT -o ! -w $TOMCAT_WEBAPPS ]; then
    echo "$TOMCAT and $TOMCAT_WEBAPPS must be writable." 1>&2
    exit 1
fi

# Change the Tomcat default port to 8181
sed -i -e 's/8080/8181/g' $TOMCAT_CONFIG

# Set permissions for admin user to access Tomcat's Admin GUI and Manager GUI
tail -r $TOMCAT_CONFIG_USERS | tail -n +2 | tail -r >> TEMP_TOMCAT_CONFIG.xml
rm $TOMCAT_CONFIG_USERS 
mv TEMP_TOMCAT_CONFIG.xml $TOMCAT_CONFIG_USERS
echo -e "<role rolename=\"manager-gui\"/>" >> $TOMCAT_CONFIG_USERS
echo -e "<role rolename=\"admin-gui\"/>" >> $TOMCAT_CONFIG_USERS
echo -e "<user username=\"admin\" password=\"admin\" roles=\"manager-gui,admin-gui\"/>" >> $TOMCAT_CONFIG_USERS
echo -e "</tomcat-users>" >> $TOMCAT_CONFIG_USERS

# Restart Tomcat
echo "Starting Tomcat.."
$TOMCAT_STOP
$TOMCAT_START

# Visit Tomcat on Browser
open http://localhost:8181/
