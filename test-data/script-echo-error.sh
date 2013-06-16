#!/bin/bash
# This script always writes to stderr:
#echo error >/dev/stderr
echo error 1>&2;
echo A
echo A
echo A
echo A
echo A
echo A
echo A
exit 0