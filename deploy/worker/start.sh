#!/bin/bash

# generate auto port
/opt/kuaizhan/bin/domeize -template /export-tpl.sh:/export.sh
source /export.sh

# add global environments
echo "export JMX_PORT="$AUTO_PORT0 >> /root/.bashrc
source /root/.bashrc

# register jetty clean
_term() {
    kill -SIGTERM "$child"
    wait "$child"
}
trap _term SIGTERM


# figure out jvm size

JVM_SIZE_DEV="-Xmx1024m -Xms1024m"
JVM_SIZE_PROD="-Xmx2000m -Xms2000m"

case "${DOCKER_ENV}" in
    'dev')
        JVM_SIZE_ARG=${JVM_SIZE_DEV}
    ;;
    'test')
        JVM_SIZE_ARG=${JVM_SIZE_DEV}
    ;;
    'pre')
        JVM_SIZE_ARG=${JVM_SIZE_PROD}
    ;;
    'production')
        JVM_SIZE_ARG=${JVM_SIZE_PROD}
    ;;
esac

# start worker, with jmx on
java -Dcom.sun.management.jmxremote \
     -Dcom.sun.management.jmxremote.port=$JMX_PORT \
     -Dcom.sun.management.jmxremote.authenticate=false \
     -Dcom.sun.management.jmxremote.ssl=false \
     ${JVM_SIZE_ARG} \
     -jar $PREFIX/worker/kzweixin.jar &

child=$!
wait "$child"
