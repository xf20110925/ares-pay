#!/usr/bin/env bash
source /etc/profile
HOME=$(cd "$(dirname "$0")/../"; pwd)
CONFIG_FILE_HOME=/opt/ptbconf/payment
cd $HOME
jars=$(ls $HOME/|grep .jar)
libjar=`ls libs | grep .jar | awk '{jar=jar"'"libs/"'"$1":"} END {print jar}'`
echo $jars

echo $CONFIG_FILE_HOME:$jars:$libjar
java -cp $CONFIG_FILE_HOME:$libjar:$jars  com.ptb.account.Application  $* >> logs/run.log 2>&1
