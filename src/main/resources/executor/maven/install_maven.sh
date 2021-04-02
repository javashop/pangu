mvn -v
if [[ $? == 0 ]]; then
  echo 'maven已存在'
  exit 0
fi

wget --version
if [[ $? == 0 ]]; then
    echo 'wget已存在'
else
    yum -y install wget
fi

cd /opt
wget http://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
tar -xzvf apache-maven-3.6.3-bin.tar.gz
echo 'MAVEN_HOME=/opt/apache-maven-3.6.3' >> /etc/profile
echo 'PATH=$PATH:$JAVA_HOME/bin:$JRE_HOME/bin:$MAVEN_HOME/bin' >> /etc/profile
source /etc/profile
\cp ${workspace}/settings.xml /opt/apache-maven-3.6.3/conf/settings.xml
