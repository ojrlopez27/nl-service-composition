#!/bin/bash

COMPOSER_NAME=OWL-S-Composer

# Start WSDL2OWL-S Converter
echo "Starting WSDL2OWL-S Converter.."
cd $COMPOSER_NAME/api; ./wsdl2owls.sh
