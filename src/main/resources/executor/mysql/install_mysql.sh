mkdir -p /home/mysql/{conf,logs,data/mysql}
#校验my.cnf是否存在
if [ -f "/home/mysql/conf/my.cnf" ];then
  echo "my.cnf already exit"
else
  cp ${workspace}/my.cnf /home/mysql/conf/
fi
#先校验执行器中的post是否配置，如果没有配置则查询环境变量的，如果都没有配置默认3306
port=${port}
if [ ! -n "${port}" ] ;then
      #校验是否配置了环境变量
      <#if !mysql??>
        port=3306
        echo "mysql.port param is empty,default 3306"
      <#else>
        if [ ! -n "${mysql.port}" ] ;then
          port=3306
          echo "mysql.port param is empty,default 3306"
        else
          port=${mysql.port}
        fi
      </#if>
fi
#校验密码参数是否为空
password=${password}
if [ ! -n "${password}" ] ;then
  <#if !mysql??>
     password=123456
     echo "mysql.password param is empty,default 123456"
  <#else>
     if [ ! -n "${mysql.password}" ] ;then
        password=123456
        echo "mysql.password param is empty,default 123456"
      else
         password=${mysql.password}
      fi
  </#if>
fi

#校验mysql是否启动,如果没有启动则启动
exist=`docker inspect --format '{{.State.Running}}' mysql`
if [ "$exist" != "true" ]; then
  docker run --rm -d -p $port:3306 -e MYSQL_ROOT_PASSWORD=$password --name mysql --privileged=true -v /home/mysql/conf/my.cnf:/etc/mysql/my.cnf -v /home/mysql/logs:/logs -v /home/mysql/data/mysql:/var/lib/mysql   registry.cn-beijing.aliyuncs.com/javashop-k8s-images/mysql:5.6.35
else
  echo "mysql already running"
fi


