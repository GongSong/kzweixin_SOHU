#mvn package -Dmaven.test.skip=true && \
docker build -t registry.kuaizhan.com/kuaizhan-plf/weixin-worker ./deploy/ && \
docker push registry.kuaizhan.com/kuaizhan-plf/weixin-worker