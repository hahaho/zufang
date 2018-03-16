/*==============================================================*/
/* Table: T_CRM_USERS                                          */
/*==============================================================*/
CREATE TABLE t_rbac_users (
  ID bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID标识',
  USER_NAME varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
  PASSWORD varchar(100) NOT NULL DEFAULT '' COMMENT '密码',
  REAL_NAME varchar(50) NOT NULL DEFAULT '' COMMENT '真实姓名',
  MOBILE varchar(20) NOT NULL DEFAULT '' COMMENT '手机',
  EMAIL varchar(50) NOT NULL DEFAULT '' COMMENT '电子邮件地址',
  STATUS varchar(5) NOT NULL DEFAULT '' COMMENT '有效状态 1-有效 0-无效',
  DEPARTMENT varchar(20) NOT NULL DEFAULT '' COMMENT '部门',
  CREATED_BY varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  created_time datetime NOT NULL DEFAULT '1900-01-01 00:00:00' COMMENT '创建日期',
  UPDATED_BY varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  updated_time datetime NOT NULL DEFAULT '1900-01-01 00:00:00' COMMENT '更新日期',
  APARTMENT_CODE varchar(64) NOT NULL DEFAULT '' COMMENT '用来关联公寓code',
  PRIMARY KEY (ID),
  UNIQUE KEY UNIQUE_USER_NAME (USER_NAME)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户信息表';


/*==============================================================*/
/* Table: T_CRM_ROLES                                          */
/*==============================================================*/

CREATE TABLE t_rbac_roles (
  ID bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键标识',
  ROLE_CODE varchar(50) NOT NULL DEFAULT '' COMMENT '角色编码',
  ROLE_NAME varchar(100) NOT NULL DEFAULT '' COMMENT '角色名称',
  DESCRIPTION varchar(500) NOT NULL DEFAULT '' COMMENT '描述',
  CREATED_BY varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  created_time datetime NOT NULL DEFAULT '1900-01-01 00:00:00' COMMENT '创建日期',
  UPDATED_BY varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  updated_time datetime NOT NULL DEFAULT '1900-01-01 00:00:00' COMMENT '更新日期',
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

/*==============================================================*/
/* Table: T_CRM_MENUS                                          */
/*==============================================================*/
CREATE TABLE t_rbac_menus (
  ID bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键标识ID',
  TEXT varchar(50) NOT NULL DEFAULT '' COMMENT '文本标题',
  ICON_CLS varchar(50) NOT NULL DEFAULT '' COMMENT '图标样式',
  URL varchar(100) NOT NULL DEFAULT '' COMMENT '打开地址',
  PARENT_ID varchar(50) NOT NULL DEFAULT '' COMMENT '父节点',
  DISPLAY int(11) NOT NULL DEFAULT '0' COMMENT '显示顺序',
  CREATED_BY varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  created_time datetime NOT NULL DEFAULT '1900-01-01 00:00:00' COMMENT '创建日期',
  UPDATED_BY varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  updated_time datetime NOT NULL DEFAULT '1900-01-01 00:00:00' COMMENT '更新日期',
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单表';


/*==============================================================*/
/* Table: T_CRM_USER_ROLE                                      */
/*==============================================================*/
CREATE TABLE t_rbac_user_role (
  ID bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  USER_ID bigint(20) NOT NULL DEFAULT '0' COMMENT '用户编码',
  ROLE_ID bigint(20) NOT NULL DEFAULT '0' COMMENT '角色ID',
  CREATED_BY varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  created_time datetime NOT NULL DEFAULT '1900-01-01 00:00:00' COMMENT '创建日期',
  UPDATED_BY varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  updated_time datetime NOT NULL DEFAULT '1900-01-01 00:00:00' COMMENT '更新日期',
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色表';

/*==============================================================*/
/* Table: T_RBAC_ROLE_MENU                                      */
/*==============================================================*/
CREATE TABLE t_rbac_role_menu (
  ID bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  ROLE_ID bigint(20) NOT NULL DEFAULT '0' COMMENT '角色ID',
  MENU_ID bigint(20) NOT NULL DEFAULT '0' COMMENT '菜单ID',
  CREATED_BY varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  created_time datetime NOT NULL DEFAULT '1900-01-01 00:00:00' COMMENT '创建日期',
  UPDATED_BY varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  updated_time datetime NOT NULL DEFAULT '1900-01-01 00:00:00' COMMENT '更新日期',
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色菜单表';


/*==============================================================*/
/* Table: T_RBAC_PERMISSION                                    */
/*==============================================================*/
CREATE TABLE t_rbac_permissions (
  ID bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  PERMISSION_CODE varchar(100) NOT NULL DEFAULT '' COMMENT '权限编码',
  PERMISSION_NAME varchar(100) NOT NULL DEFAULT '' COMMENT '权限名称',
  DESCRIPTION varchar(200) NOT NULL DEFAULT '' COMMENT '权限描述',
  CREATED_BY varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  created_time datetime NOT NULL DEFAULT '1900-01-01 00:00:00' COMMENT '创建日期',
  UPDATED_BY varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  updated_time datetime NOT NULL DEFAULT '1900-01-01 00:00:00' COMMENT '更新日期',
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限表';


/*==============================================================*/
/* Table: T_RBAC_ROLE_PERMISSION                               */
/*==============================================================*/
CREATE TABLE t_rbac_role_permission (
  ID bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  ROLE_ID bigint(20) NOT NULL DEFAULT '0' COMMENT '角色ID',
  PERMISSION_ID bigint(20) NOT NULL DEFAULT '0' COMMENT '权限ID',
  CREATED_BY varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  created_time datetime NOT NULL DEFAULT '1900-01-01 00:00:00' COMMENT '创建日期',
  UPDATED_BY varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  updated_time datetime NOT NULL DEFAULT '1900-01-01 00:00:00' COMMENT '更新日期',
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='角色权限表';
