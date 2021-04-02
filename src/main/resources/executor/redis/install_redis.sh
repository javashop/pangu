#校验数据文件夹是否存在，不存在则创建新的
if [ ! -d "/opt/data/redisdata/" ];then
  mkdir -p /opt/data/redisdata
  chmod -R 777 /opt/data/redisdata
fi

#校验redis是否启动，如果未启动则启动
mqPid=`netstat -lnp|grep 6379 |awk '{print $7}'`
if [ ! $mqPid ];then
    #运行redis
    docker run --rm -d -p 6379:6379 --privileged=true \
    -v /opt/data/redisdata:/data \
    --name redis registry.cn-beijing.aliyuncs.com/javashop-k8s-images/redis:5.0.4-alpine \
    redis-server --appendonly yes --requirepass "123456"
else
  echo "redis already running"
fi