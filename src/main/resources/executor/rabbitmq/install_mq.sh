#校验数据文件夹是否存在，不存在则创建新的
if [ ! -d "/opt/data/mqdata/" ];then
  mkdir -p /opt/data/mqdata
  chmod -R 777 /opt/data/mqdata
fi

#校验rabbitMQ是否启动，如果未启动则启动
mqPid=`netstat -lnp|grep 15672 |awk '{print $7}'`
if [ ! $mqPid ];then
    #运行mq
    docker run --rm -d --hostname rabbit --privileged=true \
     -p 15672:15672  -p 5672:5672 -p 25672:25672 -p 4369:4369 -p 35672:35672 \
     -v /opt/data/mqdata:/var/lib/rabbitmq \
     -e RABBITMQ_ERLANG_COOKIE='MY-SECRET-KEY' \
     --name rabbitmq registry.cn-beijing.aliyuncs.com/javashop-k8s-images/rabbitmq:3.6.14
else
  echo "rabbitMq already running"
fi