#!/bin/bash

COMPOSER_NAME=OWL-S-Composer
WSDL2OWL_CONVERTER=$COMPOSER_NAME/api/wsdl2owls.sh
GITHUB_URL=https://github.com/FORMAS/$COMPOSER_NAME

# Clone OWL-S-Composer from GitHub
if [ ! -r $COMPOSER_NAME ]; then
	if [ -n "$(which git)" ]; then
	   echo "Cloning OWL-S-Composer using Git.."
	   git clone $GITHUB_URL
	fi
	if [ $? -eq 0 ]; then
    	    echo "done. OWL-S-Composer installed."
	fi
else
    echo "OWL-S-Composer already exists."
fi

# Make wsdl2owls script executable
chmod +x $WSDL2OWL_CONVERTER

