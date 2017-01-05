#!/usr/bin/env bash
source /etc/profile
HOME=$(cd "$(dirname "$0")/../"; pwd)
CONFIG_FILE_HOME=/opt/ptbconf/ares-pay
cd $HOME
jars=$(ls $HOME/|grep .jar)
libjar=`ls libs | grep .jar | awk '{jar=jar"'"libs/"'"$1":"} END {print jar}'`
echo $jars

echo $CONFIG_FILE_HOME:$jars:$libjar
java -cp $CONFIG_FILE_HOME:$libjar:$jars -server -Xms6g -Xmx6g -XX:PermSize=128m -XX:MaxPermSize=128m -Xss256k  -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:MaxGCPauseMillis=10 -XX:GCPauseIntervalMillis=200 -XX:+DisableExplicitGC com.ptb.pay.Application  $* >> logs/run.log 2>&1
