#安装docker
function installDocker(){
  sudo yum -y update
  sudo yum install -y yum-utils device-mapper-persistent-data lvm2
  sudo yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
  sudo yum -y install docker-ce-18.03.1.ce
  systemctl enable docker
  systemctl restart docker
  docker version
}

#校验是否安装过docker，未安装过则执行安装，反之继续
if ! command -v docker &> /dev/null
then
  #执行安装
  installDocker
fi

#校验是否已经启动docker，未启动则执行启动程序，反之继续
status=`systemctl status docker`
result=$(echo $status | grep "running")
if [[ "$result" != "" ]]
then
   echo "docker already running"
else
   systemctl start docker
fi