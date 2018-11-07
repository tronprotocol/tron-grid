#!/bin/bash
git pull
mvn package
if [[ "$?" -ne 0 ]] ; then
  echo 'could not perform tests'; exit $rc
else
    while true; do
      pid=`ps -ef |grep trongrid |grep -v grep |awk '{print $2}'`
      if [ -n "$pid" ]; then
        kill -15 $pid
        echo "The java-tron process is exiting, it may take some time, forcing the exit may cause damage to the database, please wait patiently..."
        sleep 1
      else
        echo "trongrid killed successfully!"
        break
      fi
    done
    nohup java -jar target/trongrid-0.0.1-SNAPSHOT.jar &
fi

