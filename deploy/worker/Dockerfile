FROM private-registry.sohucs.com/kuaizhan-plf/java-env:prod
MAINTAINER "chengliu" <chengliu@sohu-inc.com>

# set DOCKER_ENV
ARG env
ENV DOCKER_ENV=${env}

ENV PREFIX /opt/kuaizhan

COPY ./target/kzweixin.jar $PREFIX/worker/
COPY ./deploy/worker/export-tpl.sh /export-tpl.sh
COPY ./deploy/worker/start.sh /start.sh

ENTRYPOINT /bin/bash /start.sh
