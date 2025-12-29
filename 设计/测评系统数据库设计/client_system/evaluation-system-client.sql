-- ============================================
-- 测评系统数据库设计
-- 基于 eladmin 框架
-- 客户使用
-- 包含：店铺表、产品表、订单表
-- ============================================

-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================
-- 1. 店铺管理表
-- ============================================

-- 店铺表（客户视角）
DROP TABLE IF EXISTS `bt_store`;
CREATE TABLE `bt_store` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID（关联后台系统的bt_tenant表）',
  `platform_code` varchar(50) NOT NULL COMMENT '平台编码（关联后台系统的bt_platform表的platform_code）',
  `store_name_kr` varchar(200) NOT NULL COMMENT '店铺韩文名称',
  `store_name_custom` varchar(200) NOT NULL COMMENT '店铺自定义备注名称',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_platform_code` (`platform_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='店铺表-测评系统';

-- ============================================
-- 2. 产品管理表
-- ============================================

-- 产品表（客户视角）
DROP TABLE IF EXISTS `bt_product`;
CREATE TABLE `bt_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID（关联后台系统的bt_tenant表）',
  `store_id` bigint(20) NOT NULL COMMENT '店铺ID',
  `platform_code` varchar(50) NOT NULL COMMENT '平台编码（关联后台系统的bt_platform表的platform_code）',
  `product_id` varchar(100) NOT NULL COMMENT '产品ID（唯一，不可修改）',
  `product_title` varchar(500) NOT NULL COMMENT '产品标题',
  `product_link` varchar(1000) DEFAULT NULL COMMENT '产品链接',
  `selling_price` decimal(10,2) DEFAULT NULL COMMENT '售价',
  `attribute_name` varchar(200) DEFAULT NULL COMMENT '属性名称',
  `main_image_url` varchar(1000) DEFAULT NULL COMMENT '产品主图URL',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_id` (`product_id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_store_id` (`store_id`),
  KEY `idx_platform_code` (`platform_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品表-测评系统';

-- ============================================
-- 3. 订单创建和填写表
-- ============================================

-- 批次订单表（客户创建）
DROP TABLE IF EXISTS `bt_order_batch`;
CREATE TABLE `bt_order_batch` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `batch_no` varchar(100) NOT NULL COMMENT '批次订单号',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID（关联后台系统的bt_tenant表）',
  `order_type_id` bigint(20) NOT NULL COMMENT '订单类型ID（关联后台系统）',
  `batch_order_status` varchar(50) DEFAULT 'pending_payment' COMMENT '批次订单状态：pending_payment-待支付佣金，cancelled-已取消，paid-已支付佣金',
  `total_quantity` int(11) DEFAULT 0 COMMENT '总数量',
  `total_amount` decimal(10,2) DEFAULT 0.00 COMMENT '总金额（预算）',
  `actual_amount` decimal(10,2) DEFAULT NULL COMMENT '实际金额',
  `currency` varchar(10) DEFAULT 'KRW' COMMENT '币种',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_batch_no` (`batch_no`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_order_type_id` (`order_type_id`),
  KEY `idx_batch_order_status` (`batch_order_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='批次订单表-测评系统';

-- 订单表（客户填写）
DROP TABLE IF EXISTS `bt_order`;
CREATE TABLE `bt_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID（关联后台系统的bt_tenant表）',
  `order_batch_id` bigint(20) NOT NULL COMMENT '批次订单ID',
  `product_id` bigint(20) NOT NULL COMMENT '产品ID',
  `store_id` bigint(20) NOT NULL COMMENT '店铺ID',
  `platform_code` varchar(50) NOT NULL COMMENT '平台编码（关联后台系统的bt_platform表的platform_code）',
  `order_type_id` bigint(20) NOT NULL COMMENT '订单类型ID（关联后台系统）',
  `order_status` varchar(50) DEFAULT 'pending' COMMENT '订单状态：pending-待开始，processing-进行中，pending_confirm-待确认，completed-已完成',
  `order_date` date NOT NULL COMMENT '订单日期',
  `keyword` varchar(200) DEFAULT NULL COMMENT '关键词',
  `original_detail_id` bigint(20) DEFAULT NULL COMMENT '原订单ID（重新提交时关联）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_order_batch_id` (`order_batch_id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_store_id` (`store_id`),
  KEY `idx_platform_code` (`platform_code`),
  KEY `idx_order_date` (`order_date`),
  KEY `idx_order_status` (`order_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表-测评系统';

SET FOREIGN_KEY_CHECKS = 1;
