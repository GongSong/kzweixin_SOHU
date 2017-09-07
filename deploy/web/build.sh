build_image() {
    env=$1
    tag=$2

    mvn clean package -Dmaven.test.skip=true -P ${env}
    docker build --build-arg env=${env} -t ${tag} -f ./deploy/web/Dockerfile .
    docker push ${tag}
}

ENV=$1
case "${ENV}" in
    'dev')
        # build_image dev registry.kuaizhan.com/kuaizhan-plf/weixin-web:dev
        build_image dev private-registry.sohucs.com/kuaizhan-plf/weixin-web:dev

    ;;
    'test')
        # build_image test registry.kuaizhan.com/kuaizhan-plf/weixin-web:test
        build_image test private-registry.sohucs.com/kuaizhan-plf/weixin-web:test
    ;;
    'pre')
        build_image pre private-registry.sohucs.com/kuaizhan-plf/weixin-web:pre
    ;;
    'production')
        build_image production private-registry.sohucs.com/kuaizhan-plf/weixin-web:prod
    ;;
esac
