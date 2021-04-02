#校验es是否已经在运行
exist=`docker inspect --format '{{.State.Running}}' elasticsearch`
if [ ! "$exist" != "true" ]; then
  echo "elasticsearch already running"
  exit
fi

#校验数据文件夹是否存在，不存在则创建新的
if [ ! -d "/opt/data/esdata/" ];then
  mkdir -p /opt/data/esdata
  chmod -R 777 /opt/data/esdata
fi

##校验base url是否为空
#url=${base_url}
#if [ ! -n "${base_url}" ] ;then
#  echo "error Please configure URL"
#  exit
#fi
#写入参数
function writeCfg(){
  echo -e '<?xml version=""1.0" encoding="UTF-8"?>' >> /opt/ik/IKAnalyzer.cfg.xml
  echo -e '<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">' >> /opt/ik/IKAnalyzer.cfg.xml
  echo -e '<properties>' >> /opt/ik/IKAnalyzer.cfg.xml
  echo -e ' <comment>IK Analyzer 扩展配置</comment>' >> /opt/ik/IKAnalyzer.cfg.xml
  echo -e ' <entry key="remote_ext_dict">'$url'/load-customwords?secret_key=secret_value</entry>' >> /opt/ik/IKAnalyzer.cfg.xml
  echo -e '</properties>' >> /opt/ik/IKAnalyzer.cfg.xml
}

#校验扩展词典是否存在
if [ ! -f "/opt/ik/IKAnalyzer.cfg.xml" ];then
  mkdir -p /opt/ik
  touch /opt/ik/IKAnalyzer.cfg.xml
  writeCfg
else
  #如果存在此文件则清空后重新建，防止url发生变化
  : > /opt/ik/IKAnalyzer.cfg.xml
  writeCfg
fi

docker run --rm -d --name elasticsearch -v /opt/data/esdata:/usr/share/elasticsearch/data  -v /opt/ik/IKAnalyzer.cfg.xml:/usr/share/elasticsearch/plugins/ik/config/IKAnalyzer.cfg.xml  -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" registry.cn-beijing.aliyuncs.com/javashop-k8s-images/elasticsearch:6.2.2
