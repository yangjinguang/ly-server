CREATE TABLE `account` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `account_id` varchar(45) NOT NULL DEFAULT '' COMMENT '帐号ID',
  `username` varchar(16) NOT NULL DEFAULT '' COMMENT '帐号',
  `password` varchar(32) NOT NULL DEFAULT '' COMMENT '密码',
  `salt` varchar(5) NOT NULL DEFAULT '' COMMENT '盐值',
  `name` varchar(45) DEFAULT '' COMMENT '姓名',
  `email` varchar(255) DEFAULT '' COMMENT '邮箱',
  `phone` varchar(20) DEFAULT '' COMMENT '手机号',
  `avatar` varchar(255) DEFAULT '' COMMENT '头像Url',
  `wx_open_id` varchar(45) DEFAULT '' COMMENT '微信OPEN ID',
  `is_admin` tinyint(4) DEFAULT '0' COMMENT '是否是管理员',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '账户表';

CREATE TABLE `organization` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `organization_id` varchar(45) NOT NULL DEFAULT '' COMMENT '组织ID',
  `parent_id` varchar(45) NOT NULL DEFAULT '' COMMENT '父级ID',
  `name` varchar(45) DEFAULT '' COMMENT '名称',
  `description` varchar(255) DEFAULT '' COMMENT '描述',
  `avatar` varchar(255) DEFAULT '' COMMENT '头像Url',
  `is_root` tinyint(4) DEFAULT '0' COMMENT '是否是租户根组织',
  `is_class` tinyint(4) DEFAULT '0' COMMENT '是否是班级',
  `enabled` tinyint(4) DEFAULT '1' COMMENT '是否启用',
  `order` int(11) DEFAULT '0' COMMENT '排序',
  `tenant_id` varchar(45) NOT NULL DEFAULT '' COMMENT '租户ID',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '组织架构表';

CREATE TABLE `role` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'ID',
  `role_id` varchar(45) NOT NULL DEFAULT '' COMMENT '角色ID',
  `name` varchar(45) DEFAULT '' COMMENT '名称',
  `description` varchar(255) DEFAULT '' COMMENT '描述',
  `tenant_id` varchar(45) NOT NULL DEFAULT '' COMMENT '租户ID',
  `enabled` tinyint(4) DEFAULT '1' COMMENT '是否启用',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '角色表';

CREATE TABLE `student` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'ID',
  `student_id` varchar(45) NOT NULL DEFAULT '' COMMENT '学生ID',
  `name` varchar(255) DEFAULT '' COMMENT '姓名',
  `avatar` varchar(255) DEFAULT '' COMMENT '头像Url',
  `profile_id` varchar(255) DEFAULT '' COMMENT '学生详细资料,对应Mongo student_profile表',
  `enabled` tinyint(4) DEFAULT '1' COMMENT '是否启用',
  `tenant_id` varchar(30) NOT NULL DEFAULT '' COMMENT '租户ID',
  `organization_id` varchar(45) NOT NULL DEFAULT '' COMMENT '组织ID',
  `account_id` varchar(45) NOT NULL DEFAULT '' COMMENT '帐号ID',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '学生表';

CREATE TABLE `tenant` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `tenant_id` varchar(45) NOT NULL DEFAULT '' COMMENT '租户ID',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '姓名',
  `description` varchar(255) DEFAULT '' COMMENT '描述',
  `address` varchar(255) DEFAULT '' COMMENT '地址',
  `avatar` varchar(255) DEFAULT '' COMMENT '头像Url',
  `enabled` tinyint(4) DEFAULT '1' COMMENT '是否启用',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '租户表';

CREATE TABLE `tenant_account` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `tenant_id` varchar(45) NOT NULL DEFAULT '' COMMENT '租户ID',
  `account_id` varchar(45) NOT NULL DEFAULT '' COMMENT '账户ID',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '租户和账户关联表';

CREATE TABLE `organization_account` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `organization_id` varchar(45) NOT NULL DEFAULT '' COMMENT '组织ID',
  `account_id` varchar(45) NOT NULL DEFAULT '' COMMENT '账户ID',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '组织和账户关联表';


CREATE TABLE `role_account` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` varchar(45) NOT NULL DEFAULT '' COMMENT '角色ID',
  `account_id` varchar(45) NOT NULL DEFAULT '' COMMENT '账户ID',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '角色和账户关联表';