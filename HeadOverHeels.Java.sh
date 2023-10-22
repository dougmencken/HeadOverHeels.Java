#!/bin/sh

if [ -f head/over/heels/main.class ]
then

    # the $@ variable expands to all the arguments
    java head.over.heels.main "$@"

else

    echo "./CompileMe.sh first, mmkay?"

fi
