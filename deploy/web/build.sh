#mvn clean package -Dmaven.test.skip=true && \
docker build -t registry.kuaizhan.com/kuaizhan-plf/weixin-web ./deploy/ && \
docker push registry.kuaizhan.com/kuaizhan-plf/weixin-web