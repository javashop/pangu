# 快速上手

## 安装盘古

请根据您的情况选择

### 基于docker安装

> 前置条件：[安装docker](docker.md)

通过如下命令来运行盘古：

~~~shell
docker volume create pangudata
docker run --rm -d -p 8080:8080 --name pangu -v pangudata:/opt/pangu/database  registry.cn-beijing.aliyuncs.com/javashop-enation/pangu:1.0.0
~~~

### 基于jar包安装


> 前置条件[安装Java](https://www.runoob.com/java/java-environment-setup.html)

通过如下命令来运行盘古：

~~~shell
wget -P /home/ https://mirrors.javamall.com.cn/pangu/1.0.0/pangu.tar.gz
tar -xzvf  /home/pangu.tar.gz
cd  /home/pangu
java -jar ./pangu-1.0.0.jar
~~~

上述命令会独占当前窗口，您可以使用nohup来运行盘古：

~~~shell
cd  /home/pangu
nohup java -jar ./pangu-1.0.0.jar > /home/pangu/pangu.log 2>&1 &
~~~
>注意必须在当前目录

### 基于源码安装

~~~shell
git clone https://gitee.com/javashop/pangu.git
cd pangu
mvn clean install -DskipTests
~~~

## 完成您的第一个部署

完成安装后，访问：ip:8080，默认账号/密码是admin/admin

### 机器配置

您需要准备一台linux机器来体验盘古

并在盘古中将机器配置好（我们将此机器命名为“测试机”)

### 部署的编排

> 我以安装docker，并部署一个mysql实例为例，体验部署的编排及执行

首先需要您新增一个部署：

1、部署名字随意（比如mysql部署）

2、部署服务器：选择刚刚我们建立的“测试机”

3、环境变量：暂时可以先不选择

然后我们来编排这个部署

> 在盘古中内置了很多执行器，这些执行器是我们组织编排的核心

在这个步骤管理中，我们新增两个步骤：

#### 第一个步骤：安装docker

![image-20210204153519982](README.assets/image-20210204153519982.png ':size=500')

如上图所示，执行器选择安装docker，步骤的名字命名为"安装docker"



#### 第二个步骤：安装mysql

![image-20210204153357977](README.assets/image-20210204153357977.png ':size=500')



### 执行部署

点击“执行部署”跳转到执行界面，来体验基于盘古的第一次部署。



# 概念&功能

## 机器、标签

要执行部署操作的目标机器，部署时可以选择部署一台或多台机器，也可以将机器按标签来分类，然后按标签来部署。

## 部署与步骤

部署的编排核心是步骤，一次部署包含多个步骤，比如：

- 步骤1：克隆源码

- 步骤2：编译源码

- 步骤3：运行Jar


步骤是顺序执行的，只有上一个步骤执行成功后下一个才会被执行，否则整个部署将被终止。

盘古将步骤分为执行器和检查器两个部分（检查器是可选的），执行器执行具体的部署动作（比如安装mysql)，检查器负责检查这个部署动作是否执行成功。

## 执行器和检查器

盘古内置了一些常用的执行器和检查器，如：“SSH命令执行”、“git clone”、“安装maven”、“端口检查器”等，详解下面的列表。

- ssh命令执行
- git clone
- 安装maven
- 安装mysql
- 安装redis
- 安装node

您可以通过安装插件来使用别人开发的执行器或检查器。

您也可以通过[开发插件](plugin.md)来扩展执行器或检查器。

## 仓库

这里的仓库是指的git源码仓库，在"git clone"执行器中可以选择要clone的仓库

## 环境变量和配置文件

在部署的编排过程中，我们有这样的场景：

1、安装mysql时指定mysql的密码

2、将密码配置在程序相应的配置文件中

盘古是通过”环境变量“和”配置文件“实现上述过程的自动化的。

通过定义一组环境变量，比如：

![image-20210204153357977](README.assets/image-20210223155126667.png ':size=500')



那么在配置文件中就可以通过${mysql.username}这种语法来引用这个变量（遵循[freemaker](https://freemarker.apache.org/)语法）：

![image-20210204153357977](README.assets/image-20210223155453483.png ':size=500')




> 您需要编辑部署，使某个环境变量项目和部署关联才能使环境变量生效

配置文件需要使用”写入配置文件“这个执行器来将配置文件写入到具体的服务器某个位置：

![image-20210204153357977](README.assets/image-20210223155726286.png ':size=500')

# 插件开发

盘古中的执行器和检查器是以插件的形式提供的，您可以开发自己的插件来拓展执行器和检查器。

具体请参考”[插件开发](plugin.md)“



