#!/bin/bash
source ~/.bashrc
cd $JETTY_BASE

PORT1={{.Env.AUTO_PORT0}}
java -jar $JETTY_HOME/start.jar jetty.http.port=$PORT1
