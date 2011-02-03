#!/bin/bash
#merges all the i18n files into a single i18n.txt file
cat /dev/null > i18n.txt
find . -name 'Bundle.properties' | while read FILENAME;
do
echo $'\n'  >> i18n.txt
echo "[filename='$FILENAME']" >> i18n.txt
cat $FILENAME >> i18n.txt
done
