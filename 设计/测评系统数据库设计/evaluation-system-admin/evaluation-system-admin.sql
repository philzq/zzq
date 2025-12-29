-- ============================================
-- 测评后台管理系统数据库设计
-- 基于 eladmin 框架
-- 管理员和员工使用
-- 包含：租户表、帮助文档表、测评账号表、账单表
-- ============================================

-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================
-- 1. 租户管理表
-- ============================================

-- 租户表（原客户表）
DROP TABLE IF EXISTS `bt_tenant`;
CREATE TABLE `bt_tenant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_name` varchar(200) NOT NULL COMMENT '租户名称（公司名称）',
  `email` varchar(100) NOT NULL COMMENT '邮箱',
  `wechat_id` varchar(100) DEFAULT NULL COMMENT '企业负责人微信号码',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '负责人电话号码',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `expire_time` datetime DEFAULT NULL COMMENT '到期时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_email` (`email`),
  KEY `idx_status` (`status`),
  KEY `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租户表-后台管理系统';

-- ============================================
-- 2. 测评账号管理表
-- ============================================

-- 测评账号表
DROP TABLE IF EXISTS `bt_review_account`;
CREATE TABLE `bt_review_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `account_name` varchar(100) NOT NULL COMMENT '账号名称',
  `password` varchar(255) DEFAULT NULL COMMENT '账号密码（明文存储）',
  `platform_code` varchar(50) DEFAULT NULL COMMENT '平台编码（关联平台表的platform_code）',
  `device_id` varchar(100) DEFAULT NULL COMMENT '设备ID（关联设备表的device_id）',
  `is_auto_assign` tinyint(1) DEFAULT 1 COMMENT '是否自动分配：0-否，1-是',
  `execution_status` varchar(50) DEFAULT 'idle' COMMENT '执行状态：idle-空闲，executing-执行中',
  `current_tasks` int(11) DEFAULT 0 COMMENT '当前任务数',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_platform_code` (`platform_code`),
  KEY `idx_execution_status` (`execution_status`),
  KEY `idx_device_id` (`device_id`),
  KEY `idx_status` (`status`),
  KEY `idx_is_auto_assign` (`is_auto_assign`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测评账号表-后台管理系统';

-- 账号订单类型关联表（支持一个账号多个订单类型）
DROP TABLE IF EXISTS `bt_review_account_order_type`;
CREATE TABLE `bt_review_account_order_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `review_account_id` bigint(20) NOT NULL COMMENT '测评账号ID',
  `order_type_id` bigint(20) NOT NULL COMMENT '订单类型ID',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_account_order_type` (`review_account_id`, `order_type_id`),
  KEY `idx_review_account_id` (`review_account_id`),
  KEY `idx_order_type_id` (`order_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账号订单类型关联表-后台管理系统';

-- 设备表
DROP TABLE IF EXISTS `bt_device`;
CREATE TABLE `bt_device` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `device_id` varchar(100) DEFAULT NULL COMMENT '设备ID（设备唯一标识）',
  `device_name` varchar(200) DEFAULT NULL COMMENT '设备名称',
  `device_type` varchar(50) DEFAULT NULL COMMENT '设备类型',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `has_pending_orders` tinyint(1) DEFAULT 0 COMMENT '是否存在待执行订单：0-否，1-是',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_device_id` (`device_id`),
  KEY `idx_status` (`status`),
  KEY `idx_device_type` (`device_type`),
  KEY `idx_has_pending_orders` (`has_pending_orders`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备表-后台管理系统';

-- ============================================
-- 3. 财务账单管理表
-- ============================================

-- 账单表
DROP TABLE IF EXISTS `bt_bill`;
CREATE TABLE `bt_bill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  `order_batch_id` bigint(20) DEFAULT NULL COMMENT '批次订单ID（关联测评系统的订单）',
  `bill_scene` varchar(50) DEFAULT NULL COMMENT '账单场景',
  `budget_amount` decimal(10,2) DEFAULT 0.00 COMMENT '预算金额',
  `actual_amount` decimal(10,2) DEFAULT NULL COMMENT '实际金额',
  `currency` varchar(10) DEFAULT 'KRW' COMMENT '币种',
  `payment_status` tinyint(1) DEFAULT 0 COMMENT '支付状态：0-未支付，1-已支付，2-部分支付，3-已退款',
  `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
  `payment_method` varchar(50) DEFAULT NULL COMMENT '支付方式',
  `payment_remark` varchar(500) DEFAULT NULL COMMENT '支付备注',
  `bill_date` date DEFAULT NULL COMMENT '账单日期',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_order_batch_id` (`order_batch_id`),
  KEY `idx_bill_scene` (`bill_scene`),
  KEY `idx_payment_status` (`payment_status`),
  KEY `idx_bill_date` (`bill_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单表-后台管理系统';

-- 账单明细表
DROP TABLE IF EXISTS `bt_bill_detail`;
CREATE TABLE `bt_bill_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `bill_id` bigint(20) NOT NULL COMMENT '账单ID',
  `item_name` varchar(200) DEFAULT NULL COMMENT '明细项名称',
  `item_type` varchar(50) DEFAULT NULL COMMENT '明细项类型',
  `quantity` int(11) DEFAULT 1 COMMENT '数量',
  `unit_price` decimal(10,2) DEFAULT 0.00 COMMENT '单价',
  `budget_amount` decimal(10,2) DEFAULT 0.00 COMMENT '预算金额',
  `actual_amount` decimal(10,2) DEFAULT NULL COMMENT '实际金额',
  `currency` varchar(10) DEFAULT 'KRW' COMMENT '币种',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_bill_id` (`bill_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单明细表-后台管理系统';

-- ============================================
-- 4. 帮助和公共管理表
-- ============================================

-- 帮助文档表
DROP TABLE IF EXISTS `bt_help_document`;
CREATE TABLE `bt_help_document` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父级ID（用于构建目录层级结构，NULL表示根目录）',
  `is_directory` tinyint(1) DEFAULT 0 COMMENT '是否是目录：0-文档，1-目录',
  `doc_title` varchar(200) NOT NULL COMMENT '文档标题/目录名称',
  `doc_content` text DEFAULT NULL COMMENT '文档内容（目录时可为空）',
  `doc_type` varchar(50) DEFAULT NULL COMMENT '文档类型',
  `doc_category` varchar(50) DEFAULT NULL COMMENT '文档分类',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `view_count` int(11) DEFAULT 0 COMMENT '查看次数',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_is_directory` (`is_directory`),
  KEY `idx_status` (`status`),
  KEY `idx_doc_type` (`doc_type`),
  KEY `idx_doc_category` (`doc_category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帮助文档表-后台管理系统';

SET FOREIGN_KEY_CHECKS = 1;

