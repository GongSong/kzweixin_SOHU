FROM registry.kuaizhan.com/kuaizhan-plf/java-env:latest
MAINTAINER "chengliu" <chengliu@sohu-inc.com>

ENV PREFIX /opt/kuaizhan
ENV JAR_FILE ./target/kzweixin.jar

COPY $JAR_FILE $PREFIX/worker/

RUN echo "source ~/.bashrc" > /init.sh && \
    echo "java -jar $PREFIX/worker/kzweixin.jar" >> /init.sh

CMD /bin/bash /init.sh
