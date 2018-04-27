CREATE TABLE `t_zfang_spider_house` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `apartment_id` bigint(20) NOT NULL COMMENT '公寓id',
  city varchar(16) NOT NULL DEFAULT '' COMMENT '城市',
  url varchar(255) NOT NULL DEFAULT '' COMMENT '房源url',
  ext_house_id varchar(32) NOT NULL DEFAULT '' COMMENT '外部房源id',
  last_job_time datetime NOT NULL COMMENT '最近一次跑job时间',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `updated_time` datetime NOT NULL COMMENT '更新时间',
  `is_delete` varchar(2) NOT NULL DEFAULT '00' COMMENT '是否删除(00 默认值,  01 删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='爬虫房源表';

ALTER TABLE t_zfang_house add column ext_house_id varchar(32) NOT NULL DEFAULT '' COMMENT '外部房源id';


DROP TABLE IF EXISTS `t_zfang_submit_message`;
CREATE TABLE `t_zfang_submit_message` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(10) NOT NULL COMMENT '用户id',
  phone varchar(64) NOT NULL DEFAULT '' COMMENT '用户手机号',
  `user_name` varchar(20) NOT NULL DEFAULT '' COMMENT '用户名',
  `submit_message` varchar(255) NOT NULL COMMENT '反馈提交内容',
  `submit_time` datetime NOT NULL COMMENT '反馈提交时间',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `updated_time` datetime NOT NULL COMMENT '更新时间',
  `created_user` varchar(20) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_user` varchar(20) NOT NULL DEFAULT '' COMMENT '更新人',
  `picture_url` varchar(255) NOT NULL COMMENT '反馈图片地址',
  `is_delete` varchar(2) NOT NULL DEFAULT '00' COMMENT '是否删除(00 默认值,  01 删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='反馈意见表';

DROP TABLE IF EXISTS `t_zfang_banner`;
CREATE TABLE `t_zfang_banner` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `banner_name` varchar(100)  NOT NULL DEFAULT '' COMMENT 'banner名称',
  `banner_img_url` varchar(100) NOT NULL COMMENT '图片地址',
  `banner_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT 'banner类型(1.首页 2.品牌公寓)',
  `banner_sort` bigint(20) NOT NULL COMMENT 'banner排序',
  `activity_url` varchar(128) NOT NULL  DEFAULT '' COMMENT '首页banner图跳转url',
  `create_user` varchar(50) NOT NULL COMMENT '创建人',
  `update_user` varchar(50) NOT NULL COMMENT '修改人',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `updated_time` datetime NOT NULL COMMENT '修改时间',
  `is_delete` varchar(2) NOT NULL DEFAULT '00' COMMENT '是否删除(00 默认值,  01 删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='banner信息表';



ALTER TABLE `t_zfang_house`
ADD COLUMN `traffic`  varchar(255)  NOT NULL DEFAULT '' COMMENT '交通',
ADD COLUMN `around`  varchar(255) NOT NULL DEFAULT '' COMMENT '周边';

CREATE TABLE `t_zfang_reserve_record` (
  `id` bigint(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `reserve_house_id` bigint(10) NOT NULL COMMENT '预约看房表id外键',
  `operate_type` tinyint(4) NOT NULL COMMENT '记录变更操作类型，1:预约看房，2:变更看房信息，3：取消行程',
  `operate_time` datetime NOT NULL COMMENT '记录变更操作时间',
  `remark` varchar(255) NOT NULL COMMENT '操作备注：用户第1次预约成功，用户第2次变更看房信息，用户第3次取消看房行程 等等。。',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `updated_time` datetime NOT NULL COMMENT '更新时间',
  `created_user` varchar(20) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_user` varchar(20) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 COMMENT='预约看房记录变更表';

ALTER TABLE `t_zfang_reserve_house` ADD COLUMN `reserve_status` tinyint(4) NOT NULL COMMENT '预约看房状态 1：已预约（首次预约成功默认状态），2：已变更（编辑预约行程，变更为此状态），3：已取消（删除预约行程，变更为此状态），4：已失效（看房时间过期，变更为此状态），';

ALTER TABLE `t_zfang_reserve_record` ADD COLUMN `reserve_date` datetime NOT NULL COMMENT '变更记录看房时间冗余字段，每次变更看房信息记录变更前看房时间' AFTER `operate_type`