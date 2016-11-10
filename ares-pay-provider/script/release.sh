#!/usr/bin/env bash
PNAME=$1
OUTPUT_DIR=`pwd`
OUTPUT_DIR=$OUTPUT_DIR/dist
echo "=============OUTPUT_DIR:" + $OUTPUT_DIR + "============="
mkdir $OUTPUT_DIR
HOME=$(cd "$(dirname "$0")/../"; pwd)
echo "=============HOME:" + $HOME + "============="
cd $HOME
publishTime=`date "+%Y%m%d%H%M%S"`

JARNAME=`ls ${HOME}/target|grep -v 'sources\..*ar'|grep '.*\..*ar$'`
echo "输出包为"$JARNAME
echo "发布包名为：${PNAME}"
DistDir=$HOME/target/dist/${PNAME}
echo "=============DistDir:" + $DistDir + "============="
mkdir -p $DistDir
mkdir -p ${DistDir}/config/
mkdir -p ${DistDir}/logs
cp target/${JARNAME} ${DistDir}/${PNAME}.jar
cp -rf ${HOME}/bin ${DistDir}
cp -rf ${HOME}/script/Dockerfile.txt ${DistDir}/Dockerfile
cp -rf ${HOME}/target/classes/*.xml ${DistDir}/config/
cp -rf ${HOME}/target/classes/*.properties ${DistDir}/config/
cp -rf ${HOME}/../README.md ${DistDir}
rm -rf ${HOME}/target/libs/spring-boot-devtools*
cp -rf ${HOME}/target/libs ${DistDir}

cd $HOME/target/dist/
chmod -R 755   ${PNAME}/
tar czvf ${PNAME}.tar.gz ${PNAME}
cp -rf ${PNAME}.tar.gz  $OUTPUT_DIR
rm  -rf ${PNAME}/
echo "打包成功"