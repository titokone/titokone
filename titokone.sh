#!/bin/sh
#
# This is a startup scrip to run titokone.jar. If you change 
# INSTALL_DIRECTORY to be the directory where you have located 
# titokone.jar, this script can be eg. placed in your ~/bin/
# directory, so that it can be run with just 'titokone.sh'
# anywhere on the machine, provided you have ~/bin/ on your 
# command path.
#
# T‰m‰ on k‰ynnistysskripti titokone.jarin ajamiseen. Jos muutat
# INSTALL_DIRECTORYn arvon hakemistoksi, jossa titokone.jar 
# sijaitsee, voit siirt‰‰ itse skriptin minne haluat, esimerkiksi
# ~/bin/-hakemistoon, jolloin voit ajaa skriptin (vaikka uudelleen-
# nimettyn‰) mist‰ tahansa hakemistosta, kunhan ~/bin/ on
# komentopolullasi.
 
INSTALL_DIRECTORY=.

# Example install directory: in your home directory, in the subdirectory
# titokone: Remove # to uncomment the line below.

# INSTALL_DIRECTORY=~/titokone/

# It is not necessary to change the line below unless you are really
# turning this script into something more sophisticated.
# The additional '/' between titokone.jar and INSTALL_DIRECTORY
# exists to allow both directories with or without / in their end.
# If you wish to pass additional parameters to the command, you can just
# add them after the ./titokone.sh. For example, ./titokone.sh -vvv 
# starts up titokone.jar in verbose debugging mode.

java -classpath ${INSTALL_DIRECTORY}/titokone.jar -jar ${INSTALL_DIRECTORY}/titokone.jar $@
