# 安装docker

## Centos

~~~shell
  yum-config-manager --add-repo https://mirrors.aliyun.com/repo/Centos-7.repo
  yum-config-manager --add-repo https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
  yum makecache fast
  yum -y install yum-utils
  yum install -y device-mapper-persistent-data lvm2 nfs-utils  conntrack-tools
  yum install -y docker-ce-18.09.8
  yum install -y  docker-ce-cli-18.09.8
  systemctl daemon-reload
  systemctl enable docker
  systemctl restart docker
~~~

## Ubuntu

~~~shell
sudo apt-get update
sudo apt-get install apt-transport-https ca-certificates curl software-properties-common
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo apt-key fingerprint 0EBFCD88
sudo add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
   $(lsb_release -cs) \
   stable"
sudo apt-get update   
sudo apt-get install docker-ce
~~~

## Mac

> https://docs.docker.com/docker-for-mac/install/#install-and-run-docker-for-mac

## Windows

> https://docs.docker.com/docker-for-windows/install/#start-docker-for-windows