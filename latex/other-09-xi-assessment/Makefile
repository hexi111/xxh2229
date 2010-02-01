FILE=vonLaszewski-09-xihe-greenit
preview=open -a /Applications/Preview.app

all:
	pdflatex ${FILE}
	bibtex ${FILE}
	pdflatex ${FILE}
	pdflatex ${FILE}

g:
	pdflatex ${FILE}-g
	bibtex ${FILE}-g
	pdflatex ${FILE}-g
	pdflatex ${FILE}-g
	${preview} ${FILE}-g.pdf

clean:
	rm -f *~ *.dvi *.aux *.log *.bbl *.blg *.out *.toc

view:
	${preview} ${FILE}.pdf

acro:
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
