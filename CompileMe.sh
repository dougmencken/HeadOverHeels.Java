#!/bin/sh

[ -d com/github/trilarion/sound ] || ./submodules/CompileSubModules.sh

javac -g -d . source/behaviors/*.java
javac -g -d . source/*.java source/gui/*.java
