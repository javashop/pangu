# 安装nodejs
installNode(){
  cd /usr/local/
  wget https://npm.taobao.org/mirrors/node/v12.16.2/node-v12.16.2-linux-x64.tar.gz
  tar -zxvf node-v12.16.2-linux-x64.tar.gz
  rm -rf node-v12.16.2-linux-x64.tar.gz
  mv node-v12.16.2-linux-x64 node
  echo 'export NODE_HOME=/usr/local/node
    export PATH=$NODE_HOME/bin:$PATH'>>/etc/profile
  source /etc/profile
  # node npm 链接
  sudo ln -s /usr/local/node/bin/node /usr/bin/node
  sudo ln -s /usr/local/node/bin/node /usr/lib/node
  sudo ln -s /usr/local/node/bin/npm /usr/bin/npm
}

# 安装Pm2
installPm2(){
  # 安装yarn
  npm install yarn -g --registry=https://registry.npm.taobao.org

  # 安装pm2
  npm install pm2 -g --registry=https://registry.npm.taobao.org

  # pm2 链接
  sudo ln -s /usr/local/node/bin/pm2 /usr/bin/pm2
  sudo ln -s /usr/local/node/bin/pm2 /usr/lib/pm2
  sudo ln -s /usr/local/node/bin/yarn /usr/bin/yarn
  sudo ln -s /usr/local/node/bin/yarn /usr/lib/yarn
}

#校验node是否安装，如果安装则不重新安装
if ! type node >/dev/null 2>&1; then
    installNode
else
    echo 'node already install';
fi

#校验pm2是否安装，如果安装则不重新安装
if ! type pm2 >/dev/null 2>&1; then
    installPm2
else
    echo 'pm2 already install';
fi

