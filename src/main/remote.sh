#!/bin/bash
scp -i "$1" "$2" mullinaxgr@34.73.226.115:"$3"
ssh -ti "$1" mullinaxgr@34.73.226.115 'java -jar "$3"'

