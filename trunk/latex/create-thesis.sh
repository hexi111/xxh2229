#!/bin/sh
FILE=$1
cp -rf thesis-template $FILE
rm -rf $FILE/.svn
rm -rf $FILE/*/.svn
mv $FILE/xihe-template.tex $FILE/xihe-$FILE.tex
rm $FILE/xihe-template.pdf
perl -pi -e "s/template/$FILE/g" $FILE/Makefile
##



