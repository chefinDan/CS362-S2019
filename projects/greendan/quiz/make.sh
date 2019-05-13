#!/bin/bash
CFLAGS="-Wall -g -coverage -std=c99";
gcc $CFLAGS testme.c -o testme
./testme > testme.out
echo "\n\n Coverage results of randomTest\n" >> testme.out;
gcov -lbf testme.c >> testme.out
