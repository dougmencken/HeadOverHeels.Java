#!/bin/sh

# ogg-vorbis support for java

ogg_vorbis_submodule="submodules/java-ogg-vorbis"
ogg_vorbis_for_java="$ogg_vorbis_submodule/vorbis-support/src/main"

[ -d "$ogg_vorbis_for_java" ] || git submodule update --init

ogg_vorbis_sources="$ogg_vorbis_for_java/java/com/github/trilarion/sound"

javac -g -d . $ogg_vorbis_sources/util/*.java

javac -g -d . $ogg_vorbis_sources/vorbis/jcraft/jogg/*.java
javac -g -d . $ogg_vorbis_sources/vorbis/jcraft/jorbis/*.java

javac -g -d . $ogg_vorbis_sources/sampled/*.java \
			$ogg_vorbis_sources/sampled/spi/*.java

javac -g -d . $ogg_vorbis_sources/vorbis/sampled/*.java \
                        $ogg_vorbis_sources/vorbis/sampled/spi/*.java

mkdir -p META-INF/services && cp $ogg_vorbis_for_java/resources/META-INF/services/* ./META-INF/services/
