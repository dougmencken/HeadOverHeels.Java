#!/bin/sh

rm -v -f head/over/heels/*.class
rm -v -f head/over/heels/items/*.class
rm -v -f head/over/heels/behaviors/*.class
rm -v -f head/over/heels/gui/*.class
rm -v -f head/over/heels/gui/transitions/*.class

[ -d head/over/heels/items ] && rmdir head/over/heels/items

[ -d head/over/heels/behaviors ] && rmdir head/over/heels/behaviors

[ -d head/over/heels/gui/transitions ] && rmdir head/over/heels/gui/transitions
[ -d head/over/heels/gui ] && rmdir head/over/heels/gui

[ -d head/over/heels ] && rmdir head/over/heels
[ -d head/over ] && rmdir head/over
[ -d head ] && rmdir head

[ -d com ] && rm -r com

[ -d META-INF ] && rm -r META-INF
