# 生成rsa公钥

~~~
 ssh-keygen -t rsa
~~~

一路回车,结束后查看公钥：

~~~
cat /root/.ssh/id_rsa.pub
~~~

然后参考下面的文档在gitee中部署公钥：

https://gitee.com/help/articles/4191#article-header0



