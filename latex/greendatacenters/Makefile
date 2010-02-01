FILE=XiHe-greendatacenters
preview=open -a /Applications/Preview.app

simple:
	pdflatex ${FILE}
	make -f Makefile view

all:
	pdflatex ${FILE}
	bibtex ${FILE}
	pdflatex ${FILE}
	pdflatex ${FILE}

mimetype:
	svn propset svn:mime-type "application/pdf" *.pdf
	svn commit -m "fixed mimetype"

clean:
	rm -f *~ *.dvi *.aux *.log *.bbl *.blg *.out *.toc

view:
	${preview} ${FILE}.pdf

acro:
#	gv ${FILE}.pdf
	killall AdobeReader
	open ${FILE}.pdf

gv:
	gv ${FILE}.pdf

endnote:
	java -classpath ../bin/bib2endnote.jar BibEndnote PUTYOURBIBFILEHERE.bib > endnote.xml

bib-duplicate:
	echo "CHECKING FOR DUPLICATED ENTRIES"
	bibtool -d ../bib/*.bib *.bib | fgrep -v "non-space" > bib.log

bib-extract:
	echo "EXTRACTING ALL USED CITATIONS INTO A BIB FILE"
	bibtool -x ${FILE}.aux -o ${FILE}.bib
