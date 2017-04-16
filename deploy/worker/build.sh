mvn clean package -Dmaven.test.skip=true -f mq_pom.xml -P dev && \
docker build -t registry.kuaizhan.com/kuaizhan-plf/weixin-worker ./deploy/ && \
docker push registry.kuaizhan.com/kuaizhan-plf/weixin-worker