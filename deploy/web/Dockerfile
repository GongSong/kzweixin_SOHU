FROM private-registry.sohucs.com/kuaizhan-plf/java-env:prod
MAINTAINER "chengliu" <chengliu@sohu-inc.com>

ENV PREFIX /opt/kuaizhan

COPY ./target/kzweixin.war $PREFIX/jetty/webapps/

COPY ./deploy/web/init.sh /init.sh
COPY ./deploy/web/jetty-tpl.ini /jetty-tpl.ini
COPY ./deploy/web/supervisord.conf /supervisord.conf

ENTRYPOINT supervisord -c /supervisord.conf
