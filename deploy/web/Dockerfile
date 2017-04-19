FROM private-registry.sohucs.com/kuaizhan-plf/java-env:prod
MAINTAINER "chengliu" <chengliu@sohu-inc.com>

ENV PREFIX /opt/kuaizhan
ENV WAR_FILE ./target/kzweixin.war

COPY $WAR_FILE $PREFIX/jetty/webapps/
COPY ./deploy/web/init-tpl.sh /init-tpl.sh

ENTRYPOINT source /root/.bashrc && \
    domeize -template /init-tpl.sh:/init.sh && \
    /bin/bash /init.sh

CMD /bin/bash /init.sh
