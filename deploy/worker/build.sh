build_image() {
    env=$1
    tag=$2

    mvn clean package -Dmaven.test.skip=true -f mq_pom.xml -P ${env} && \
    docker build -t ${tag} -f ./deploy/worker/Dockerfile . && \
    docker push ${tag}
}

ENV=$1
case "${ENV}" in
    'dev')
        build_image dev registry.kuaizhan.com/kuaizhan-plf/weixin-worker:dev
    ;;
    'test')
        build_image test registry.kuaizhan.com/kuaizhan-plf/weixin-worker:test
    ;;
    'pre')
        build_image pre private-registry.sohucs.com/kuaizhan-plf/weixin-worker:pre
    ;;
    'prod')
        build_image production private-registry.sohucs.com/kuaizhan-plf/weixin-worker:prod
    ;;
esac
