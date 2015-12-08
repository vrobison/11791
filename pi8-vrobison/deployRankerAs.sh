#!/bin/bash

PROJ_FOLDER=`dirname $0`

DD=${1:-src/main/resources/deploy/deployRanker.xml} 
BROKER=${2:-tcp://ur.lti.cs.cmu.edu:61616}

export UIMA_JVM_OPTS="-Xms128M -Xmx4000M -XX:-UseGCOverheadLimit"

export UIMA_CLASSPATH="$PROJ_FOLDER/target/classes:$PROJ_FOLDER/target/dependency:$PROJ_FOLDER/src/main/resources/extraDependencies"

$UIMA_HOME/bin/deployAsyncService.sh $DD -brokerURL $BROKER
