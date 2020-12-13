#!/bin/bash

TOTAL="1,2"
INITIATOR=true
PLAYERS="1,2"
DELAY=0
for i in "$@"
do
case $i in
    -t=*|--total=*)
    TOTAL="${i#*=}"
    shift
    ;;
    -i=*|--initiator=*)
    INITIATOR="${i#*=}"
    shift
    ;;
    -p=*|--players=*)
    PLAYERS="${i#*=}"
    shift
    ;;
    -d=*|--delay=*)
    DELAY="${i#*=}"
    shift
    ;;
    *)
          # unknown option
          echo "Invalid option."
    ;;
esac
done
echo "Options are:"
echo "-t | --total = Comma separated integer list containing the total players id. Example -p=1,2,3,4"
echo "-i | --initiator = Boolean representing if the first player of players is the initiator. Example -i=true"
echo "-p | --players = Comma separated integer list containing the id of player in this process. Example -p=1,2"
echo "-d | --delay = Delay in seconds for players to wait. Example -d=1"


echo "Current values:"
echo "TOTAL PLAYERS          = ${TOTAL}"
echo "INITIATOR              = ${INITIATOR}"
echo "PLAYERS IN PROCESS     = ${PLAYERS}"
echo "DELAY                  = ${DELAY}"

JAR=target/players-1.0-SNAPSHOT.jar

if test ! -f "$JAR"; then
  mvn package
fi

java -jar $JAR $TOTAL $INITIATOR $PLAYERS $DELAY