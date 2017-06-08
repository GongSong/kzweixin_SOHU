FROM private-registry.sohucs.com/kuaizhan-plf/java-env:prod
MAINTAINER "chengliu" <chengliu@sohu-inc.com>

# set DOCKER_ENV
ARG env
ENV DOCKER_ENV=${env}

ENV PREFIX /opt/kuaizhan

COPY ./target/kzweixin.war $PREFIX/jetty/webapps/
COPY ./deploy/web/export-tpl.sh /export-tpl.sh
COPY ./deploy/web/start.sh /start.sh

ENTRYPOINT /bin/bash /start.sh
