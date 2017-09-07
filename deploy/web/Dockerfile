FROM private-registry.sohucs.com/kuaizhan-plf/java-env:prod
MAINTAINER "chengliu" <chengliu@sohu-inc.com>

# set DOCKER_ENV
ARG env
ENV DOCKER_ENV=${env}

ENV PREFIX /opt/kuaizhan
ENV JAVA_HOME /usr/java/jdk1.8.0_121

COPY ./target/kzweixin.war $PREFIX/jetty/webapps/
COPY ./deploy/web/export-tpl.sh /export-tpl.sh
COPY ./deploy/web/start.sh /start.sh

# 只有替换了这两个jar包，才能使用微信非128位aeskey解密
# 见文档: https://stackoverflow.com/questions/6481627/java-security-illegal-key-size-or-default-parameters
COPY ./deploy/web/local_policy.jar $JAVA_HOME/jre/lib/security
COPY ./deploy/web/US_export_policy.jar $JAVA_HOME/jre/lib/security

ENTRYPOINT /bin/bash /start.sh
