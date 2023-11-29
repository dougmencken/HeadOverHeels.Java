#!/bin/sh

# ogg-vorbis support for java

javac -g -d . submodules/java-ogg-vorbis/vorbis-support/src/main/java/com/github/trilarion/sound/util/*.java

javac -g -d . submodules/java-ogg-vorbis/vorbis-support/src/main/java/com/github/trilarion/sound/vorbis/jcraft/jogg/*.java
javac -g -d . submodules/java-ogg-vorbis/vorbis-support/src/main/java/com/github/trilarion/sound/vorbis/jcraft/jorbis/*.java

javac -g -d . submodules/java-ogg-vorbis/vorbis-support/src/main/java/com/github/trilarion/sound/sampled/*.java \
			submodules/java-ogg-vorbis/vorbis-support/src/main/java/com/github/trilarion/sound/sampled/spi/*.java

javac -g -d . submodules/java-ogg-vorbis/vorbis-support/src/main/java/com/github/trilarion/sound/vorbis/sampled/*.java \
                        submodules/java-ogg-vorbis/vorbis-support/src/main/java/com/github/trilarion/sound/vorbis/sampled/spi/*.java

mkdir -p META-INF/services && cp submodules/java-ogg-vorbis/vorbis-support/src/main/resources/META-INF/services/* ./META-INF/services/
