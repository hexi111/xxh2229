#!/bin/sh
FILE=$1
cp -rf template $FILE
rm -rf $FILE/.svn
rm -rf $FILE/*/.svn
mv $FILE/XiHe-template.tex $FILE/XiHe-$FILE.tex
rm $FILE/XiHe-template.pdf
perl -pi -e "s/template/$FILE/g" $FILE/Makefile
##



