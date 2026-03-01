#!/bin/sh

##[ -d com/github/trilarion/sound ] || ./submodules/CompileSubModules.sh # incompatible with Java 5

javac -source 5 -target jsr14 -verbose -g -d . \
	source/*.java \
	source/items/*.java \
	source/rooms/*.java \
	source/behaviors/*.java \
	source/gui/*.java \
	source/gui/transitions/*.java \
	source/gui/swing/*.java \
2>&1 | grep --line-buffered -v '\[loading' | grep --line-buffered -v '\[parsing completed' \
	| sed --unbuffered 's/\[parsing started/parsing/' \
	| sed --unbuffered 's/[][]//g' \
	| sed --unbuffered 's/SimpleFileObject//' \
	| sed --unbuffered 's/RegularFileObject//'
