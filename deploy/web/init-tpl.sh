#!/bin/bash
source ~/.bashrc
cd $JETTY_BASE

PORT1={{.Env.AUTO_PORT0}}
MYIP={{.Env.DOMEOS_SERVER_ADDR}}
SERVICE_NAME=kzweixin
ID=${SERVICE_NAME}-${MYIP}-${PORT1}
HCURL=http://${MYIP}:${PORT1}/kzweixin/v1/common/ping
CONSUL_URL=http://consul.kuaizhan.sohuno.com

curl -X PUT -d "{\
  \"ID\": \"${ID}\",\
  \"Name\": \"${SERVICE_NAME}\",\
  \"Address\": \"${MYIP}\",\
  \"Port\": ${PORT1},\
  \"EnableTagOverride\": false,\
  \"Check\": {\
    \"DeregisterCriticalServiceAfter\": \"1m\",\
    \"HTTP\": \"${HCURL}\",\
    \"Interval\": \"5s\"\
  }\
}" $CONSUL_URL/v1/agent/service/register

java -jar $JETTY_HOME/start.jar
