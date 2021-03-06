# 盘古可持续集成工具

[盘古](https://pangu.javamall.com.cn/)是[javashop团队](http://www.javamall.com.cn/)内部总结多年的部署经验推出的一款开源的devops工具， 致力于在提供简单、使用、高效的可持续集成服务。在目前流行的devops工具中缺少对机器、仓库、步骤、环境变量的明确定义、操作，盘古提供了上述的资源的管理，内置常见java,、PHP、nodejs、mysql、nginx等常见环境的执行器（直接编排为步骤），并提供了导出导入功能、一键复制流水线，极大方便了部署过程。通过环境变量和配置文件可以实现复杂的大型项目统一配置。未来计划支持k8s的流水线编排，感谢您的star，您的支持是我们不断完善的最大动力！
欢迎加入盘古交流群：最下方扫码进群。

## 官网&文档

[盘古官网](https://pangu.javamall.com.cn)

[盘古文档](https://pangu.javamall.com.cn/docs/#/)
> 默认密码：admin/admin

## 特色功能与亮点

对比流行的devops工具（如jenkins、瓦力等），盘古汲取了他们的众多优点，并结合我们在实际使用中碰到的问题，尝试给大家提供一款更加简便易用的devops工具：

#### 简易安装

基于Java+h2，一个jar包即可运行

> 开箱即用是盘古追求的第一目标，再使用jenkins、瓦力等等工具的过程，希望可以提供最简便的安装过程。

#### 编排更容易

> 盘古明确定义出步骤、依赖、检查等概念，希望提供极低的编排门槛

- 内置git clone、安装jdk、安装maven、mysql、nginx、nodejs等等常见执行器，简化编排过程

- 步骤可以暂停跳过、拖拽排序编排步骤，方便部署异常情况调试

- 内置检查器，可检测编排步骤是否确认成功

- 导出导入编排文件，一键复制编排

#### 环境变量、配置文件支持

  > 在大型的项目devops过程中，需要统一定义数据账号密码、秘钥、域名等等的配置文件，盘古抽象出环境变量、配置文件的概念来解决这类问题

  统一定义如mysql密码、spring boot配置文件等

#### 机器管理
提供机器管理、标签设置，按机器按标签，批量部署
#### 仓库管理
提供仓库管理、方便部署编排


#### 项目管理
按项目汇总配置文件、环境变量，简化编排过程
#### 插件
插件式扩展、希望借助开源社区的力量， 提供无限可能



![image-20210323170306700](README.assets/image-20210323170306700.png)

![image-20210323170602194](README.assets/image-20210323170602194.png)

> 请先扫码并关注公共账号，可以自动加入盘古交流群：
![image-20210204153357977](docs/qrcode.jpg)
