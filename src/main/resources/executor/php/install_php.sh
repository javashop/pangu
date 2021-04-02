mkdir -p /opt/php
cd /opt/php

#安装依赖
yum -y install libxml2 libxml2-devel openssl openssl-devel curl-devel libjpeg-devel libpng-devel freetype-devel libmcrypt-devel

#安装libzip
yum remove libzip
wget https://mirrors.javamall.com.cn/libzip/libzip-1.2.0.tar.gz

tar -zxvf libzip-1.2.0.tar.gz
cd libzip-1.2.0
./configure
 make && make install

#find /usr/local -iname 'zipconf.h'
ln -s /usr/local/lib/libzip/include/zipconf.h /usr/local/include

#编译安装php
cd /opt/php
wget http://mirrors.sohu.com/php/php-7.3.3.tar.gz
tar -zxvf php-7.3.3.tar.gz
cd php-7.3.3

./configure --prefix=/usr/local/php7 \
--with-config-file-path=/usr/local/php7/etc \
--with-config-file-scan-dir=/usr/local/php7/etc/php.d \
--with-mcrypt=/usr/include \
--enable-mysqlnd \
--with-mysqli \
--with-pdo-mysql \
--enable-fpm \
--with-gd \
--with-iconv \
--with-zlib \
--enable-xml \
--enable-shmop \
--enable-sysvsem \
--enable-inline-optimization \
--enable-mbregex \
--enable-mbstring \
--enable-ftp \
--enable-gd-native-ttf \
--with-openssl \
--enable-pcntl \
--enable-sockets \
--with-xmlrpc \
--enable-zip \
--enable-soap \
--without-pear \
--with-gettext \
--enable-session \
--with-curl \
--with-jpeg-dir \
--with-freetype-dir \
--enable-opcache

make && make install

cp /usr/local/php7/etc/php-fpm.conf.default  /usr/local/php7/etc/php-fpm.conf
cp /usr/local/php7/etc/php-fpm.d/www.conf.default /usr/local/php7/etc/php-fpm.d/www.conf
cp /opt/php/php-7.3.3/sapi/fpm/init.d.php-fpm /etc/init.d/php-fpm
chmod +x /etc/init.d/php-fpm
chkconfig --add php-fpm
chkconfig php-fpm on
service php-fpm start
