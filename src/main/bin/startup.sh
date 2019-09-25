#! /bin/bash
nohup java -jar ../lib/swaggerfilegenerator-1.0-SNAPSHOT.jar  > ../log.txt 2>&1 &
echo 'yaml文件生成完成'