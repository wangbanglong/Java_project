# Java_CommentServer_project
Java小项目 运用MyBatis完成评论服务的开发

## 使用方法
下载并解压 使用idea打开CommentServer_project文件夹。需要在配置项中配置自己的mysql地址
且mysql中需要comment数据库 及comment表和user表

comment表结构：
CREATE TABLE `comment`(
  `id` bigint UNSIGNED AUTO_INCREMENT NOT NULL,
  `ref_id` VARCHAR(32) NOT NULL,
  `user_id` bigint NOT NULL,
	`content` VARCHAR(1000)  NOT NULL,
	`parent_id` bigint NULL,
  `gmt_created` datetime NOT NULL ,
  `gmt_modified` datetime NOT NULL,

  PRIMARY KEY ( id )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

user表结构：
CREATE TABLE `user`(
  `id` bigint UNSIGNED AUTO_INCREMENT NOT NULL,
  `user_name` VARCHAR(20) NOT NULL,
  `pwd` VARCHAR(32) NOT NULL,
	`nick_name` VARCHAR(20)  NULL,
	`avatar` VARCHAR(200) NULL,
  `gmt_created` datetime NOT NULL ,
  `gmt_modified` datetime NOT NULL,

  PRIMARY KEY ( id )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;



## 项目流程
● 用户注册服务
● 用户登录服务
● 用户发表评论
● 用户回复评论
● 查询文章评论

总结：设计领域模型-->开发基础的DAO-->开发Service-->开发API

## 个别代码解释
Result 通用返回模型

Paging 分页模型

## 难点
MyBatis XML方式对数据库进行增删改查
查询评论时 查询表数据组装树数据结构
CORS解决AJAX同源使用限制




