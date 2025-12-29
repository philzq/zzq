-- ============================================
-- 测评系统数据库设计
-- 基于 eladmin 框架
-- 客户使用
-- ============================================

-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================
-- 1. 账号基本信息维护表
-- ============================================

-- 客户账号表
DROP TABLE IF EXISTS `client_customer`;
CREATE TABLE `client_customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '关联sys_user表的用户ID',
  `email` varchar(100) NOT NULL COMMENT '邮箱',
  `company_name` varchar(200) NOT NULL COMMENT '公司名称',
  `wechat_id` varchar(100) DEFAULT NULL COMMENT '企业负责人微信号码',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '负责人电话号码',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `is_first_login` tinyint(1) DEFAULT 1 COMMENT '是否首次登录：0-否，1-是',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_email` (`email`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户账号表-测评系统';

-- 客户子账户表
DROP TABLE IF EXISTS `client_customer_sub_account`;
CREATE TABLE `client_customer_sub_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `customer_id` bigint(20) NOT NULL COMMENT '客户ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '关联sys_user表的用户ID',
  `sub_account_name` varchar(100) NOT NULL COMMENT '子账户名称',
  `email` varchar(100) NOT NULL COMMENT '邮箱',
  `permissions` text DEFAULT NULL COMMENT '权限JSON字符串',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `is_first_login` tinyint(1) DEFAULT 1 COMMENT '是否首次登录：0-否，1-是',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户子账户表-测评系统';

-- ============================================
-- 2. 店铺管理表
-- ============================================

-- 店铺表（客户视角）
DROP TABLE IF EXISTS `client_store`;
CREATE TABLE `client_store` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `customer_id` bigint(20) NOT NULL COMMENT '客户ID',
  `platform_id` bigint(20) NOT NULL COMMENT '平台ID（关联后台系统的平台）',
  `store_name_kr` varchar(200) NOT NULL COMMENT '店铺韩文名称',
  `store_name_custom` varchar(200) NOT NULL COMMENT '店铺自定义备注名称',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除（客户端删除）',
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_platform_id` (`platform_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='店铺表-测评系统';

-- ============================================
-- 3. 产品管理表
-- ============================================

-- 产品表（客户视角）
DROP TABLE IF EXISTS `client_product`;
CREATE TABLE `client_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `store_id` bigint(20) NOT NULL COMMENT '店铺ID',
  `platform_id` bigint(20) NOT NULL COMMENT '平台ID',
  `product_id` varchar(100) NOT NULL COMMENT '产品ID（唯一，不可修改）',
  `product_title` varchar(500) NOT NULL COMMENT '产品标题',
  `product_link` varchar(1000) DEFAULT NULL COMMENT '产品链接',
  `selling_price` decimal(10,2) DEFAULT NULL COMMENT '售价',
  `add_type` tinyint(1) DEFAULT 1 COMMENT '添加方式：1-自动添加，2-手动添加',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_id` (`product_id`),
  KEY `idx_store_id` (`store_id`),
  KEY `idx_platform_id` (`platform_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品表-测评系统';

-- 产品属性表
DROP TABLE IF EXISTS `client_product_attribute`;
CREATE TABLE `client_product_attribute` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `product_id` bigint(20) NOT NULL COMMENT '产品ID',
  `attribute_name` varchar(200) NOT NULL COMMENT '属性名称',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品属性表-测评系统';

-- 产品图片表
DROP TABLE IF EXISTS `client_product_image`;
CREATE TABLE `client_product_image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `product_id` bigint(20) NOT NULL COMMENT '产品ID',
  `image_url` varchar(1000) NOT NULL COMMENT '图片URL',
  `image_type` tinyint(1) DEFAULT 1 COMMENT '图片类型：1-主图，2-详情图',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品图片表-测评系统';

-- ============================================
-- 4. 订单创建和填写表
-- ============================================

-- 批次订单表（客户创建）
DROP TABLE IF EXISTS `client_order_batch`;
CREATE TABLE `client_order_batch` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `batch_no` varchar(100) NOT NULL COMMENT '批次订单号',
  `customer_id` bigint(20) NOT NULL COMMENT '客户ID',
  `order_type_id` bigint(20) NOT NULL COMMENT '订单类型ID（关联后台系统）',
  `order_status_id` bigint(20) NOT NULL COMMENT '订单状态ID（关联后台系统）',
  `payment_status_id` bigint(20) DEFAULT NULL COMMENT '支付状态ID（关联后台系统）',
  `total_quantity` int(11) DEFAULT 0 COMMENT '总数量',
  `total_amount` decimal(10,2) DEFAULT 0.00 COMMENT '总金额（预算）',
  `actual_amount` decimal(10,2) DEFAULT NULL COMMENT '实际金额',
  `currency` varchar(10) DEFAULT 'KRW' COMMENT '币种',
  `start_date` date DEFAULT NULL COMMENT '开始日期',
  `end_date` date DEFAULT NULL COMMENT '结束日期',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_batch_no` (`batch_no`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_order_type_id` (`order_type_id`),
  KEY `idx_order_status_id` (`order_status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='批次订单表-测评系统';

-- 明细订单表（客户填写）
DROP TABLE IF EXISTS `client_order_detail`;
CREATE TABLE `client_order_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_batch_id` bigint(20) NOT NULL COMMENT '批次订单ID',
  `detail_no` varchar(100) NOT NULL COMMENT '明细订单号',
  `product_id` bigint(20) NOT NULL COMMENT '产品ID',
  `store_id` bigint(20) NOT NULL COMMENT '店铺ID',
  `platform_id` bigint(20) NOT NULL COMMENT '平台ID',
  `order_type_id` bigint(20) NOT NULL COMMENT '订单类型ID（关联后台系统）',
  `order_status_id` bigint(20) NOT NULL COMMENT '订单状态ID（关联后台系统）',
  `order_date` date NOT NULL COMMENT '订单日期',
  `keyword` varchar(200) DEFAULT NULL COMMENT '关键词',
  `quantity` int(11) DEFAULT 1 COMMENT '数量',
  `budget_amount` decimal(10,2) DEFAULT 0.00 COMMENT '预算金额',
  `actual_amount` decimal(10,2) DEFAULT NULL COMMENT '实际金额',
  `transaction_order_no` varchar(100) DEFAULT NULL COMMENT '成交订单号（返回信息）',
  `transaction_price` decimal(10,2) DEFAULT NULL COMMENT '实际成交价格（返回信息）',
  `is_resubmit` tinyint(1) DEFAULT 0 COMMENT '是否重新提交：0-否，1-是',
  `original_detail_id` bigint(20) DEFAULT NULL COMMENT '原订单ID（重新提交时关联）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_detail_no` (`detail_no`),
  KEY `idx_order_batch_id` (`order_batch_id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_store_id` (`store_id`),
  KEY `idx_order_date` (`order_date`),
  KEY `idx_order_status_id` (`order_status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='明细订单表-测评系统';

-- ============================================
-- 5. 财务账单表
-- ============================================

-- 账单表（客户视角）
DROP TABLE IF EXISTS `client_bill`;
CREATE TABLE `client_bill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `bill_no` varchar(100) NOT NULL COMMENT '账单号',
  `customer_id` bigint(20) NOT NULL COMMENT '客户ID',
  `order_batch_id` bigint(20) DEFAULT NULL COMMENT '批次订单ID',
  `bill_type` varchar(50) DEFAULT NULL COMMENT '账单类型',
  `budget_amount` decimal(10,2) DEFAULT 0.00 COMMENT '预算金额',
  `actual_amount` decimal(10,2) DEFAULT NULL COMMENT '实际金额',
  `currency` varchar(10) DEFAULT 'KRW' COMMENT '币种',
  `payment_status_id` bigint(20) DEFAULT NULL COMMENT '支付状态ID（关联后台系统）',
  `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
  `payment_method` varchar(50) DEFAULT NULL COMMENT '支付方式',
  `payment_remark` varchar(500) DEFAULT NULL COMMENT '支付备注',
  `bill_date` date DEFAULT NULL COMMENT '账单日期',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_bill_no` (`bill_no`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_order_batch_id` (`order_batch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单表-测评系统';

-- 账单明细表
DROP TABLE IF EXISTS `client_bill_detail`;
CREATE TABLE `client_bill_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `bill_id` bigint(20) NOT NULL COMMENT '账单ID',
  `order_detail_id` bigint(20) DEFAULT NULL COMMENT '明细订单ID',
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
  `deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_bill_id` (`bill_id`),
  KEY `idx_order_detail_id` (`order_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单明细表-测评系统';

-- ============================================
-- 6. 使用帮助表
-- ============================================

-- 帮助文档表（客户视角）
DROP TABLE IF EXISTS `client_help_document`;
CREATE TABLE `client_help_document` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `doc_title` varchar(200) NOT NULL COMMENT '文档标题',
  `doc_content` text DEFAULT NULL COMMENT '文档内容',
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
  `deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帮助文档表-测评系统';

SET FOREIGN_KEY_CHECKS = 1;

