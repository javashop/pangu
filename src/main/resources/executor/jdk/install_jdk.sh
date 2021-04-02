echo $PATH

java -version
if [[ $? == 0 ]]; then
  echo 'jdk已存在'
  exit 0
fi

yum -y install wget

cd /opt/
echo '开始下载jdk安装包...'
wget https://mirrors.javamall.com.cn/jdk/jdk-8u281-linux-x64.tar.gz
echo '解开压缩包'

tar -xzvf jdk-8u281-linux-x64.tar.gz
mv jdk1.8.0_281/ jdk8
echo 'JAVA_HOME=/opt/jdk8' >> /etc/profile
echo 'JRE_HOME=/opt/jdk8/jre' >> /etc/profile
echo 'CLASS_PATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:$JRE_HOME/lib' >> /etc/profile
echo 'PATH=$PATH:$JAVA_HOME/bin:$JRE_HOME/bin' >> /etc/profile
echo 'export JAVA_HOME JRE_HOME CLASS_PATH PATH' >> /etc/profile
source /etc/profile
echo '安装jdk完成'
