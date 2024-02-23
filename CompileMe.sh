#!/bin/sh

[ -d com/github/trilarion/sound ] || ./submodules/CompileSubModules.sh

javac -g -d . source/*.java source/behaviors/*.java source/gui/*.java
