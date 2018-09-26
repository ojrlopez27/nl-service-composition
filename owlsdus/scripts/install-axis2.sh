#!/bin/bash

AXIS2_NAME=axis2
AXIS2_VERSION=1.7.8
AXIS2=$AXIS2_NAME-$AXIS2_VERSION
AXIS2_SERVICES=$AXIS2/repository/services
AXIS2_CONF=$AXIS2/conf
AXIS2_CONFIG=$AXIS2_CONF/axis2.xml
AXIS2_START=$AXIS2/bin/axis2server.sh
AXIS2_BINARY=$AXIS2-bin.zip
AXIS2_URL=http://apache.claz.org/axis/$AXIS2_NAME/java/core/$AXIS2_VERSION/$AXIS2_BINARY

# Check if AXIS2 already exists
if [ ! -e $AXIS2 ]; then
    if [ ! -r $AXIS2_BINARY ]; then
        if [ -n "$(which curl)" ]; then
           echo "Downloading Apache Axis2 using curl.."
           curl -O $AXIS2_URL
        elif [ -n "$(which wget)" ]; then
            echo "Downloading Apache Axis2 using wget.."
            wget $AXIS2_URL
        fi
        if [ $? -eq 0 ]; then
            echo "done. Apache Axis2 downloaded."
        fi
    fi

    # Validate whether AXIS2 was downloaded
    if [ ! -r $AXIS2_BINARY ]; then
        echo "AXIS2 could not be downloaded." 1>&2
        echo "Verify that either curl or wget is installed." 1>&2
        echo "If they are, check your internet connection and try again." 1>&2
        echo "You may also download $AXIS2_BINARY and place it in this folder." 1>&2
        exit 1
    fi

    # Unzip the AXIS2 distribution
    unzip $AXIS2_BINARY
    rm $AXIS2_BINARY
else
    echo "Apache Axis2 exists."
fi

# Check if services can be hot-deployed in AXIS2
if [ ! -w $AXIS2 -o ! -w $AXIS2_SERVICES ]; then
    echo "$AXIS2 and $AXIS2_SERVICES must be writable." 1>&2
    exit 1
fi

# Check if brew and xmlstarlet exists
if [ -n "$(which brew)" ]; then
    if brew ls --versions xmlstarlet > /dev/null; then
        echo "Package xmlstarlet exists."
    else
        echo -e "Installing Xmlstarlet via Homebrew.."
        brew install xmlstarlet
        echo "done."
    fi
else
    echo "Verify that brew is installed."
fi

# Update Axis2 configuration
xml ed -u '/axisconfig/parameter[@name="disableSOAP12" and @locked="true"]' -v true $AXIS2_CONFIG > axis2.xml; mv axis2.xml $AXIS2_CONF/
xml ed -u '/axisconfig/parameter[@name="useGeneratedWSDLinJAXWS"]' -v true $AXIS2_CONFIG > axis2.xml; mv axis2.xml $AXIS2_CONF/
# xml ed -u '/axisconfig/clustering[@enable="false"]/@enable' -v true $AXIS2_CONFIG > axis2.xml; mv axis2.xml $AXIS2_CONF/

echo "Apache Axis2 Installed. Ready to use."

