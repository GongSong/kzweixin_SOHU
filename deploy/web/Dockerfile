FROM registry.kuaizhan.com/kuaizhan-plf/java-env:latest
MAINTAINER "chengliu" <chengliu@sohu-inc.com>

ENV PREFIX /opt/kuaizhan
ENV WAR_FILE ../../target/kzweixin.war

COPY $WAR_FILE $PREFIX/jetty/webapps/

RUN echo "source ~/.bashrc" > /init.sh && \
    echo "cd $JETTY_BASE" >> /init.sh && \
    echo "java -jar $JETTY_HOME/start.jar" >> /init.sh

EXPOSE 8080
CMD /bin/bash /init.sh