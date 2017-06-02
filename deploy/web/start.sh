#!/bin/bash


/opt/kuaizhan/bin/domeize -template /export-tpl.sh:/export.sh
source /export.sh

# add global environments
echo "export JETTY_PORT="$AUTO_PORT0 >> /root/.bashrc
echo "export JMX_PORT="$AUTO_PORT1 >> /root/.bashrc
source /root/.bashrc

# register jetty clean
_term() {
    kill -SIGTERM "$child"
    wait "$child"
}
trap _term SIGTERM

# start jetty, with jmx on
cd $JETTY_HOME
java -Dcom.sun.management.jmxremote \
     -Dcom.sun.management.jmxremote.port=$JMX_PORT \
     -Dcom.sun.management.jmxremote.authenticate=false \
     -Dcom.sun.management.jmxremote.ssl=false \
     -Djetty.port=$JETTY_PORT \
     -jar start.jar &

child=$!
wait "$child"
