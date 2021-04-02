# 规范

## 名词规范

| 中文   | 英文           |
| ------ | -------------- |
| 机器   | machine        |
| 仓库   | repository     |
| 部署   | deployment     |
| 部署集 | deployment_set |
| 步骤   | step           |
| 命令   | command        |
| 执行器 | executor       |
| 检查器 | checker        |
| 标签   | tag            |

## 路径规范

| 路径                | 说明               | 示例                                             |
| ------------------- | ------------------ | ------------------------------------------------ |
| /view/模块名/业务名 | html视图controller | /view/machine/list，机器列表                     |
| /data/模块名/业务名 | 数据controller     | /view/machines ，机器列表数据，应符合restful规范 |

## 包名规范

主包名：com.enation.pangu

| 子包名   | 说明             |
| -------- | ---------------- |
| api.view | 视图类controller |
| api.data | 数据类controller |
| mapper   | mybatis mapper   |
| model | 领域模型 |
| service | 业务类 |
| utils | 工具类 |

## 模板规范

### 总规范：

/templates/模块名/文件名

如：

/templates/machine/list.ftlh

### 页面规范

#### 公用页面

| 模板路径 | 说明             |
| -------- | ---------------- |
| common/header.ftlh | 头部 |
| common/footer.ftlh | 底部 |
| common/menu.ftlh | 菜单 |

#### 基本骨架

上述公用页面形成页面基本骨架，示例：

~~~html
<#include '../common/header.ftlh' />
<div class="content-wrapper">
  <section class="content-header">内容头</section>
  <section class="content">内容体</section>
</div>
<#include '../common/footer.ftlh' />
~~~

#### 内容头

~~~html
    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>当前页面名称</h1>
                </div>
                <div class="col-sm-6">
                    <ol class="breadcrumb float-sm-right">
                        <li class="breadcrumb-item"><a href="#">父路径</a></li>
                        <li class="breadcrumb-item active">子路径</li>
                    </ol>
                </div>
            </div>
        </div>
    </section>

~~~



#### 主内体

~~~html
    <section class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-12">
                    <div class="card">
                        <div class="card-body">
                            <table id="table" class="table table-bordered table-hover">
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
~~~



#### 其他业务

| 模板路径 | 说明             |
| -------- | ---------------- |
| list.ftlh | 列表页 |
| detail.ftlh | 详细页 |
| add.ftlh | 添加页 |
| edit.ftlh | 编辑页 |



### 页面内置变量
| 变量名称 | 说明             |
| -------- | ---------------- |
| context_path| 虚拟目录 |



## controller规范

### 分页：

分页参数：int pageNo,int pageSize

声明上述参数会自动接收到datatables的分页数据,如：

~~~java
    @RequestMapping("/pages")
    public WebPage pagesDemo(int pageNo,int pageSize) {

~~~





