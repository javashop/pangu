drop table if exists machine;
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
  filepath VARCHAR(100) NULL DEFAULT NULL COMMENT '秘钥路径',
  PRIMARY KEY (id)
);

drop table if exists repository;
CREATE TABLE repository
(
  id BIGINT(10) auto_increment NOT NULL COMMENT '仓库id',
  name VARCHAR(100) NULL DEFAULT NULL COMMENT '仓库名称',
  address VARCHAR(255) NULL DEFAULT NULL COMMENT '仓库地址',
  auth_type VARCHAR(10) NULL DEFAULT NULL COMMENT '认证类型 可选值：password,PublicKey',
  username VARCHAR(100) NULL DEFAULT NULL COMMENT '认证用户名',
  password VARCHAR(100) NULL DEFAULT NULL COMMENT '认证密码',
  add_time bigint(10) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (id)
);

drop table if exists plugin;
CREATE TABLE plugin
(
    id BIGINT(10) auto_increment NOT NULL COMMENT '主键',
    name VARCHAR(100) NULL DEFAULT NULL COMMENT '插件名称',
    plugin_id VARCHAR(100) NULL DEFAULT NULL COMMENT '插件唯一id',
    desc varchar(255) NULL DEFAULT NULL COMMENT '插件描述',
    author VARCHAR(100) NULL DEFAULT NULL COMMENT '作者',
    author_url  varchar(255) NULL DEFAULT NULL COMMENT '作者网址',
    kind varchar(10) NULL DEFAULT NULL COMMENT '插件类型',
    path varchar(255) NULL DEFAULT NULL COMMENT '插件路径',
    path_type varchar(20) NULL DEFAULT 'resource' COMMENT '插件路径类型',
    path varchar(255) NULL DEFAULT NULL COMMENT '插件类型',
    status varchar(10) NULL DEFAULT 'OPEN' COMMENT '插件状态',
    sequence int(10) NULL DEFAULT -1 COMMENT '顺序',
    PRIMARY KEY (id)
);

drop table if exists deployment;
CREATE TABLE deployment
(
  id BIGINT(10) auto_increment NOT NULL COMMENT '部署id',
  name VARCHAR(100) NULL DEFAULT NULL COMMENT '部署名称',
  depend_repo VARCHAR(255) NULL DEFAULT NULL COMMENT '需要仓库',
  repository_id bigint(10) NULL DEFAULT NULL COMMENT '仓库id',
  branch VARCHAR(100) NULL DEFAULT NULL COMMENT '分支',
  environment_id BIGINT(10) NULL DEFAULT NULL COMMENT '环境变量id',
  tag_id BIGINT(10) NULL DEFAULT NULL COMMENT '标签id',
  group_id BIGINT(10) NULL DEFAULT NULL COMMENT '分组id',
  add_time bigint(10) NULL DEFAULT NULL COMMENT '创建时间',
  way VARCHAR(20) NULL DEFAULT NULL COMMENT '部署方式',
  PRIMARY KEY (id)
);
drop table if exists deployment_set;

CREATE TABLE deployment_set
(
  id BIGINT(10) NOT NULL AUTO_INCREMENT COMMENT '部署集id',
  name VARCHAR(100) NULL DEFAULT NULL COMMENT '部署集名称',
  status int(2) NULL DEFAULT NULL COMMENT '状态:0:正常;1:异常;',
  add_time bigint(10) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (id)
);

drop table if exists deployment_set_rel;

CREATE TABLE deployment_set_rel
(
  id BIGINT(10) auto_increment NOT NULL COMMENT '主键',
  deployment_set_id bigint(10) NULL DEFAULT NULL COMMENT '部署集id',
  deployment_id bigint(10) NULL DEFAULT NULL COMMENT '部署id',
  PRIMARY KEY (id)
);
drop table if exists deployment_depend;

CREATE TABLE deployment_depend
(
  id BIGINT(10) auto_increment NOT NULL COMMENT '主键',
  deployment_set_id bigint(10) NULL DEFAULT NULL COMMENT '部署集id',
  deployment_set_rel_id bigint(10) NULL DEFAULT NULL COMMENT '部署id',
  depend_id bigint(10) NULL DEFAULT NULL COMMENT '依赖部署id',
  PRIMARY KEY (id)
);

drop table if exists step;

CREATE TABLE step
(
  id BIGINT(10) auto_increment NOT NULL COMMENT '步骤id',
  name VARCHAR(100) NULL DEFAULT NULL COMMENT '步骤名称',
  sequence int(10) NULL DEFAULT -1 COMMENT '执行顺序',
  executor VARCHAR(100) NULL DEFAULT NULL COMMENT '执行器',
  executor_params CLOB NULL DEFAULT NULL COMMENT '执行器参数',
  command VARCHAR(255) NULL DEFAULT NULL COMMENT '执行命令',
  check_type VARCHAR(20) NULL DEFAULT NULL COMMENT '校验器，可选值：port，command，process',
  checker_params CLOB NULL DEFAULT NULL COMMENT '检测器参数',
  port VARCHAR(255) NULL DEFAULT NULL COMMENT '端口号，多个端口号英文逗号隔开',
  deployment_id BIGINT(10) NOT NULL COMMENT '部署id',
  add_time BIGINT(10) NULL DEFAULT NULL COMMENT '创建时间',
  is_skip INT(10) NULL DEFAULT 0 COMMENT '是否跳过执行 0不跳过 1跳过',
  PRIMARY KEY (id)
);

drop table if exists deployment_machine;

CREATE TABLE deployment_machine
(
  id BIGINT(10) auto_increment NOT NULL COMMENT '主键',
  deployment_id BIGINT(10) NULL DEFAULT NULL COMMENT '部署id',
  machine_id BIGINT(10) NULL DEFAULT NULL COMMENT '机器id',
  PRIMARY KEY (id)
);

drop table if exists tag;

CREATE TABLE tag
(
  id BIGINT(10) auto_increment NOT NULL COMMENT '主键',
  name varchar(50) NULL DEFAULT NULL COMMENT '标签',
  PRIMARY KEY (id)
);

drop table if exists machine_tag;

CREATE TABLE machine_tag
(
  id BIGINT(10) auto_increment NOT NULL COMMENT '主键',
  tag_id BIGINT(10) NULL DEFAULT NULL COMMENT '标签id',
  machine_id BIGINT(10) NULL DEFAULT NULL COMMENT '机器id',
  PRIMARY KEY (id)
);

CREATE TABLE env_group
(
  id BIGINT(10) auto_increment NOT NULL COMMENT '主键',
  name VARCHAR(100) NULL DEFAULT NULL COMMENT '分组名字',
  project_id BIGINT(10) NULL DEFAULT NULL COMMENT '项目id',
  PRIMARY KEY (id)
);

drop table if exists env_project;
CREATE TABLE env_project
(
    id   BIGINT(10) auto_increment NOT NULL COMMENT '主键',
    name VARCHAR(100) NULL DEFAULT NULL COMMENT '项目名字',
    PRIMARY KEY (id)
);

drop table if exists user;
CREATE TABLE user
(
    id   BIGINT(10) auto_increment NOT NULL COMMENT '主键',
    name VARCHAR(100) NULL DEFAULT NULL COMMENT '姓名',
    username VARCHAR(100) NULL DEFAULT NULL COMMENT '用户名',
    password VARCHAR(100) NULL DEFAULT NULL COMMENT '密码',
    PRIMARY KEY (id)
);

CREATE TABLE env_variables
(
  id BIGINT(10) auto_increment NOT NULL COMMENT '主键',
  project_id BIGINT(10) NULL DEFAULT NULL COMMENT '项目id',
  group_id BIGINT(10) NULL DEFAULT NULL COMMENT '分组id',
  name VARCHAR(100) NULL DEFAULT NULL COMMENT '变量名字',
  value VARCHAR(100) NULL DEFAULT NULL COMMENT '变量值',
  PRIMARY KEY (id)
);

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

drop table if exists config_project;
CREATE TABLE config_project
(
  id BIGINT(10) auto_increment NOT NULL COMMENT '主键',
  name varchar(50) NULL DEFAULT NULL COMMENT '项目名字',
  edit_time varchar(20) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (id)
);

drop table if exists config_file;
CREATE TABLE config_file
(
  id BIGINT(10) auto_increment NOT NULL COMMENT '主键',
  config_project_id BIGINT(10) NULL DEFAULT NULL COMMENT '项目主键',
  name varchar(50) NULL DEFAULT NULL COMMENT '文件名字',
  content CLOB NULL DEFAULT NULL COMMENT '文件内容',
  edit_time varchar(20) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (id)
);

CREATE TABLE message
(
    id BIGINT(10) auto_increment NOT NULL COMMENT '主键',
    content VARCHAR(255) NULL DEFAULT NULL COMMENT '消息内容',
    jump_url   VARCHAR(50)  NULL DEFAULT NULL COMMENT '跳转地址',
    create_time BIGINT(10) NULL DEFAULT NULL COMMENT '创建时间',
    status   VARCHAR(20)  NULL DEFAULT NULL COMMENT '状态',
    PRIMARY KEY (id)
);
drop table if exists secret_key;
CREATE TABLE secret_key
(
    id BIGINT(10) auto_increment NOT NULL COMMENT '密钥对id',
    name VARCHAR(100) NULL DEFAULT NULL COMMENT '密钥对名称',
    public_key CLOB NULL DEFAULT NULL COMMENT '公钥',
    private_key CLOB NULL DEFAULT NULL COMMENT '私钥',
    type INT(10) NULL DEFAULT NULL COMMENT '密钥类型',
    PRIMARY KEY (id)
);
drop table if exists base_group;
CREATE TABLE base_group
(
  id BIGINT(10) auto_increment NOT NULL COMMENT '主键',
  name varchar(50) NULL DEFAULT NULL COMMENT '分组名称',
  type varchar(10) NULL DEFAULT NULL COMMENT '分组类型',
  PRIMARY KEY (id)
);

ALTER TABLE rsa_keys  RENAME TO secret_key
ALTER TABLE machine ALTER COLUMN filepath RENAME TO secretkey_id
ALTER TABLE machine ADD COLUMN  private_key varchar NULL DEFAULT NULL COMMENT '密钥'
ALTER TABLE machine ADD COLUMN  port int NULL DEFAULT NULL COMMENT '端口号'

ALTER TABLE user ADD COLUMN  add_time bigint NULL DEFAULT NULL COMMENT '添加时间'
ALTER TABLE env_project ADD COLUMN  add_time bigint NULL DEFAULT NULL COMMENT '添加时间'
ALTER TABLE base_group ADD COLUMN  add_time bigint NULL DEFAULT NULL COMMENT '添加时间'
