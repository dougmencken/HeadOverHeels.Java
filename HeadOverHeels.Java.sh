#!/bin/sh

if [ -f head/over/heels/main.class ]
then

    export GIT_HEAD_HASH=`git rev-parse --short HEAD 2>/dev/null`

    # the $@ variable expands to all the arguments
    java head.over.heels.main "$@"

else

    echo "./CompileMe.sh first, mmkay?"

fi
