## 相关技术
### AdminLTE
https://github.com/ColorlibHQ/AdminLTE

### datatables
http://datatables.club/

## 表结构

### 机器表

表名：machine

| 字段名 | 类型         | 说明     |
| ------ | ------------ | -------- |
| id     | bigint(10)   | 机器id   |
| name   | varchar(100) | 机器名称 |
| ip     | varchar(50)  | 机器ip   |
| auth_type | varchar(10)  | 认证类型，可选值：<br>password,certificate |
| username  | varchar(100) | 认证用户名 |
| password  | varchar(100) | 认证密码   |
| group_id  | bigint(10) | 分组id   |
| add_time      | bigint(10)   | 创建时间 |

~~~sql
CREATE TABLE machine
(
  id BIGINT(10)auto_increment NOT NULL COMMENT '机器id',
  name VARCHAR(100) NULL DEFAULT NULL COMMENT '机器名称',
  ip VARCHAR(50) NULL DEFAULT NULL COMMENT '机器ip',
  auth_type VARCHAR(10) NULL DEFAULT NULL COMMENT '认证类型，可选值：password,certificate',
  username VARCHAR(100) NULL DEFAULT NULL COMMENT '认证用户名',
  password VARCHAR(100) NULL DEFAULT NULL COMMENT '认证密码',
  group_id BIGINT(10) NULL DEFAULT NULL COMMENT '分组id',
  add_time BIGINT(10) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (id)
);
~~~




### 仓库表

表名：repository

| 字段名    | 类型         | 说明                                      |
| --------- | ------------ | ----------------------------------------- |
| id        | bigint(10)   | 仓库id                                    |
| name      | varchar(100) | 仓库名称                                  |
| address   | varchar(255) | 仓库地址                                  |
| auth_type | varchar(10)  | 认证类型，可选值：<br/>password,PublicKey |
| username  | varchar(100) | 认证用户名                                |
| password  | varchar(100) | 认证密码                                  |
| add_time      | bigint(10)   | 创建时间 |


### 插件表

表名：plugin

| 字段名    | 类型         | 说明                                      |
| --------- | ------------ | ----------------------------------------- |
| id        | bigint(10)   | 主键                                   |
| name      | varchar(100) | 插件名称                                  |
| plugin_id   | varchar(100) | 插件唯一id                     |
| desc | varchar(255)  | 插件描述 |
| author  | varchar(100) | 作者                                |
| author_url  | varchar(255) | 作者网址                           |
| kind     | varchar(10)   | 插件类型 |
| path | varchar(255) | 插件路径 |



### 部署

表名：deployment

| 字段名        | 类型         | 说明     |
| ------------- | ------------ | -------- |
| id            | bigint(10)   | 部署id   |
| name          | varchar(100) | 部署名称 |
| depend_repo   | varchar(255) | 需要仓库 |
| repository_id | bigint(10)   | 仓库id   |
| branch        | varchar(100) | 分支     |
| group_id      | bigint(10)   | 分组id    |
| add_time      | bigint(10)   | 创建时间 |

~~~sql
CREATE TABLE deployment
(
  id BIGINT(10) auto_increment NOT NULL COMMENT '部署id',
  name VARCHAR(100) NULL DEFAULT NULL COMMENT '部署名称',
  depend_repo VARCHAR(255) NULL DEFAULT NULL COMMENT '需要仓库',
  repository_id bigint(10) NULL DEFAULT NULL COMMENT '仓库id',
  branch VARCHAR(100) NULL DEFAULT NULL COMMENT '分支',
  environment_id BIGINT(10) NULL DEFAULT NULL COMMENT '环境变量id',
  group_id BIGINT(10) NULL DEFAULT NULL COMMENT '分组id',
  add_time bigint(10) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (id)
);
~~~

### 部署集

表名：DeploymentSet

| 字段名        | 类型         | 说明     |
| ------------- | ------------ | -------- |
| id            | bigint(10)   | 部署集id   |
| name          | varchar(100) | 部署集名称 |
| status        | int(2) | 状态:0:正常;1:异常; |
| add_time      | bigint(10)   | 创建时间 |

~~~
CREATE TABLE deployment_set
(
  id BIGINT(10) NOT NULL AUTO_INCREMENT COMMENT '部署集id',
  name VARCHAR(100) NULL DEFAULT NULL COMMENT '部署集名称',
  status int(2) NULL DEFAULT NULL COMMENT '状态:0:正常;1:异常;',
  add_time bigint(10) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (id)
);
~~~

### 部署集和部署关系表

表名：DeploymentSetRel

| 字段名        | 类型         | 说明     |
| ------------- | ------------ | -------- |
| id            | bigint(10)   | 主键   |
| deployment_set_id | bigint(10)   | 部署集id |
| deployment_id | bigint(10)   | 部署id |

~~~
CREATE TABLE deployment_set_rel
(
  id BIGINT(10) auto_increment NOT NULL COMMENT '主键',
  deployment_set_id bigint(10) NULL DEFAULT NULL COMMENT '部署集id',
  deployment_id bigint(10) NULL DEFAULT NULL COMMENT '部署id',
  PRIMARY KEY (id)
);
~~~

### 部署依赖表

表名：DeploymentDepend

| 字段名        | 类型         | 说明     |
| ------------- | ------------ | -------- |
| id            | bigint(10)   | 主键   |
| deployment_set_id | bigint(10)   | 部署集id |
| deployment_set_rel_id | bigint(10)   | 部署id |
| depend_id | bigint(10)   | 依赖部署id |

~~~
CREATE TABLE deployment_depend
(
  id BIGINT(10) auto_increment NOT NULL COMMENT '主键',
  deployment_set_id bigint(10) NULL DEFAULT NULL COMMENT '部署集id',
  deployment_set_rel_id bigint(10) NULL DEFAULT NULL COMMENT '部署id',
  depend_id bigint(10) NULL DEFAULT NULL COMMENT '依赖部署id',
  PRIMARY KEY (id)
);
~~~


### 步骤表

表名：step

| 字段名        | 类型         | 说明                                   |
| ------------- | ------------ | -------------------------------------- |
| id            | bigint(10)   | 步骤id                                 |
| name          | varchar(100) | 步骤名称                               |
| sequence      | int(10)      | 执行顺序                               |
| executor      | varchar(20)  | 执行器，可选值：ssh                    |
| command       | varchar(255) | 执行命令                               |
| check_type    | VARCHAR(20)  | 校验器，可选值：port，command，process |
| port          | VARCHAR(255) | 端口号，多个端口号英文逗号隔开         |
| deployment_id | BIGINT(10)   | 部署id                                 |
| add_time      | BIGINT(10)   | 创建时间                               |

```sql
CREATE TABLE step
(
  id BIGINT(10) auto_increment NOT NULL COMMENT '步骤id',
  name VARCHAR(100) NULL DEFAULT NULL COMMENT '步骤名称',
  sequence int(10) NULL DEFAULT -1 COMMENT '执行顺序',
  executor VARCHAR(20) NULL DEFAULT NULL COMMENT '执行器',
  executor_params VARCHAR(255) NULL DEFAULT NULL COMMENT '执行器参数',
  command VARCHAR(255) NULL DEFAULT NULL COMMENT '执行命令',
  check_type VARCHAR(20) NULL DEFAULT NULL COMMENT '校验器，可选值：port，command，process',
  checker_params VARCHAR(255) NULL DEFAULT NULL COMMENT '检测器参数',
  port VARCHAR(255) NULL DEFAULT NULL COMMENT '端口号，多个端口号英文逗号隔开',
  deployment_id BIGINT(10) NOT NULL COMMENT '部署id',
  add_time BIGINT(10) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (id)
);
```

### 部署、机器关联表

表名：deployment_machine

| 字段名        | 类型       | 说明   |
| ------------- | ---------- | ------ |
| id            | bigint(10) | 主键   |
| deployment_id | bigint(10) | 部署id |
| machine_id    | bigint(10) | 机器id |

```sql
CREATE TABLE deployment_machine
(
  id BIGINT(10) auto_increment NOT NULL COMMENT '主键',
  deployment_id BIGINT(10) NULL DEFAULT NULL COMMENT '部署id',
  machine_id BIGINT(10) NULL DEFAULT NULL COMMENT '机器id',
  PRIMARY KEY (id)
);
```

### 标签表

表名：tag

| 字段名 | 类型        | 说明 |
| ------ | ----------- | ---- |
| id     | bigint(10)  | 主键 |
| name    | varchar(50) | 标签名字 |

```sql
CREATE TABLE tag
(
  id BIGINT(10) auto_increment NOT NULL COMMENT '主键',
  name varchar(50) NULL DEFAULT NULL COMMENT '标签',
  PRIMARY KEY (id)
);
```

### 基本分组表

表名：base_group

| 字段名 | 类型        | 说明 |
| ------ | ----------- | ---- |
| id     | bigint(10)  | 主键 |
| name    | varchar(50) | 标签名字 |
| type    | varchar(10) | 分组类型 |

```sql
CREATE TABLE base_group
(
  id BIGINT(10) auto_increment NOT NULL COMMENT '主键',
  name varchar(50) NULL DEFAULT NULL COMMENT '分组名称',
  type varchar(10) NULL DEFAULT NULL COMMENT '分组类型',
  PRIMARY KEY (id)
);
```

### 项目文件表

表名：project

| 字段名 | 类型        | 说明 |
| ------ | ----------- | ---- |
| id     | bigint(10)  | 主键 |
| name    | varchar(50) | 项目名字 |
| edit_time     | BIGINT(10)  | 修改时间  |


```
CREATE TABLE env_project
(
  id BIGINT(10) auto_increment NOT NULL COMMENT '主键',
  name varchar(50) NULL DEFAULT NULL COMMENT '项目名字',
  path varchar(190) NULL DEFAULT NULL COMMENT '文件路径',
  edit_time varchar(20) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (id)
);
```

### 项目文件管理表

表名：project_files

| 字段名 | 类型        | 说明 |
| ------ | ----------- | ---- |
| id     | bigint(10)  | 主键 |
| project_id  | bigint(10) | 项目主键 |
| name    | varchar(50) | 文件名字 |
| path    | varchar(190) | 文件路径 |
| edit_time     | BIGINT(10)  | 修改时间  |


```
CREATE TABLE env_project_files
(
  id BIGINT(10) auto_increment NOT NULL COMMENT '主键',
  project_id BIGINT(10) NULL DEFAULT NULL COMMENT '项目主键',
  name varchar(50) NULL DEFAULT NULL COMMENT '文件名字',
  path varchar(190) NULL DEFAULT NULL COMMENT '文件路径',
  edit_time varchar(20) NULL DEFAULT NULL COMMENT '修改时间', 
  PRIMARY KEY (id)
);
```

### 环境变量项目

表名：env_project

| 字段名 | 类型         | 说明     |
| ------ | ------------ | -------- |
| id     | bigint(10)   | 主键     |
| name   | varchar(100) | 项目名称 |

### 环境变量分组

表名：env_group

| 字段名 | 类型         | 说明   |
| ------ | ------------ | ------ |
| id     | bigint(10)   | 主键   |
| project_id | bigint(10)   | 项目id   |
| name   | varchar(100) | 组名称 |

~~~sql
CREATE TABLE env_group
(
  id BIGINT(10) auto_increment NOT NULL COMMENT '主键',
  project_id BIGINT(10) NULL DEFAULT NULL COMMENT '项目id',
  name VARCHAR(100) NULL DEFAULT NULL COMMENT '分组名字',
  PRIMARY KEY (id)
);
~~~

### 环境变量

表名：env_variables

| 字段名     | 类型         | 说明     |
| ---------- | ------------ | -------- |
| id         | bigint(10)   | 主键     |
| project_id | bigint(10)   | 项目id   |
| group_id   | bigint(10)   | 分组id   |
| name       | varchar(100) | 变量名字 |
| value      | varchar(100) | 变量值   |

~~~sql
CREATE TABLE env_variables
(
  id BIGINT(10) auto_increment NOT NULL COMMENT '主键',
  project_id BIGINT(10) NULL DEFAULT NULL COMMENT '项目id',
  group_id BIGINT(10) NULL DEFAULT NULL COMMENT '分组id',
  name VARCHAR(100) NULL DEFAULT NULL COMMENT '变量名字',
  value VARCHAR(100) NULL DEFAULT NULL COMMENT '变量值',
  PRIMARY KEY (id)
);
~~~

### 任务

表名： task
| 字段名        | 类型       | 说明   |
| ------------- | ---------- | ------ |
| id            | bigint(10) | 主键   |
| parent_id     | bigint(10) | 父任务id   |
| deployment_id | bigint(10) | 部署id |
| deployment_name | VARCHAR(255)  | 部署名称 |
| machine_name | VARCHAR(255)  | 机器名称 |
| machine_id    | bigint(10) | 机器id |
| step_name | VARCHAR(255)  | 步骤名称 |
| step_id    | bigint(10) | 步骤id |
| state    | VARCHAR(10) | 任务状态 |
| start_time    | bigint(10) | 开始时间 |
| end_time    | bigint(10) | 结束时间 |

```sql
drop table if exists task;
CREATE TABLE task
(
  id BIGINT(10) auto_increment NOT NULL COMMENT '主键',
  parent_id BIGINT(10) NULL DEFAULT NULL COMMENT '父任务id',
  deployment_name   VARCHAR(255)  NULL DEFAULT NULL COMMENT '部署名称',
  deployment_id BIGINT(10) NULL DEFAULT NULL COMMENT '部署id',
  machine_name   VARCHAR(255)  NULL DEFAULT NULL COMMENT '机器名称',
  machine_id BIGINT(10) NULL DEFAULT NULL COMMENT '机器id',
  step_name   VARCHAR(255)  NULL DEFAULT NULL COMMENT '步骤名称',
  step_id BIGINT(10) NULL DEFAULT NULL COMMENT '步骤id',
  state VARCHAR(10)  NULL DEFAULT NULL COMMENT '任务状态',
  start_time BIGINT(10) NULL DEFAULT NULL COMMENT '开始时间',
  end_time BIGINT(10) NULL DEFAULT NULL COMMENT '结束时间',
  deployment_set_id BIGINT(10) NULL DEFAULT NULL COMMENT '部署集id',
  task_type VARCHAR(20)  NULL DEFAULT NULL COMMENT '任务类型',
  PRIMARY KEY (id)
);
```

### 部署集任务

表名： set_task

| 字段名            | 类型        | 说明     |
| ----------------- | ----------- | -------- |
| id                | bigint(10)  | 主键     |
| deployment_set_id | bigint(10)  | 部署集id |
| state             | VARCHAR(10) | 任务状态 |
| start_time        | bigint(10)  | 开始时间 |
| end_time          | bigint(10)  | 结束时间 |

```sql
drop table if exists set_task;
CREATE TABLE set_task
(
  id BIGINT(10) auto_increment NOT NULL COMMENT '主键',
  deployment_set_id BIGINT(10) NULL DEFAULT NULL COMMENT '部署集id',
  state VARCHAR(10)  NULL DEFAULT NULL COMMENT '任务状态',
  start_time BIGINT(10) NULL DEFAULT NULL COMMENT '开始时间',
  end_time BIGINT(10) NULL DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (id)
);
```

