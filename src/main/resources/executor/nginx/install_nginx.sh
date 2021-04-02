function installNginx() {
  yum install -y wget vim
  #下载并且安装nginx
  wget http://tengine.taobao.org/download/tengine-2.3.2.tar.gz
  tar -zxvf tengine-2.3.2.tar.gz
  rm -rf tengine-2.3.2.tar.gz
  cd tengine-2.3.2
  yum install -y gcc-c++ pcre pcre-devel zlib zlib-devel openssl openssl-devel
  ./configure --prefix=/usr/local/nginx --with-http_stub_status_module --with-http_ssl_module
  make & make install
#  rm -rf /root/tengine-2.3.2
}


#校验nginx是否安装
if [ ! -d "/usr/local/nginx/" ];then
  installNginx
else
  echo "nginx already install"
fi
