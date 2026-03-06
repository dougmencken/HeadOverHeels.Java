#!/bin/sh

if [ -f head/over/heels/main.class ]
then

    # the $@ variable expands to all the arguments
    java -DGIT_HEAD_HASH=`git rev-parse --short HEAD 2>/dev/null` head.over.heels.main "$@"

else

    echo "./CompileMe.sh first, mmkay?"

fi
