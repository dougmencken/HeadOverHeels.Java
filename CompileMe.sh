#!/bin/sh

[ -d com/github/trilarion/sound ] || ./submodules/CompileSubModules.sh

javac -g -d . \
	source/*.java \
	source/items/*.java \
	source/rooms/*.java \
	source/behaviors/*.java \
	source/gui/*.java \
	source/gui/transitions/*.java \
	source/gui/swing/*.java
