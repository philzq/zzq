-- ============================================
-- 测评系统数据库设计
-- 基于 eladmin 框架
-- 客户使用
-- 包含：平台表、订单类型表、店铺表、产品表、订单表
-- ============================================

-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================
-- 1. 平台管理表
-- ============================================

-- 平台表
DROP TABLE IF EXISTS `bt_platform`;
CREATE TABLE `bt_platform` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `platform_code` varchar(50) NOT NULL COMMENT '平台编码（coupang、naver等）',
  `platform_name` varchar(100) NOT NULL COMMENT '平台名称',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_platform_code` (`platform_code`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='平台表-测评系统';

-- ============================================
-- 2. 订单类型管理表
-- ============================================

-- 订单类型表
DROP TABLE IF EXISTS `bt_order_type`;
CREATE TABLE `bt_order_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `operation_scene` tinyint(1) DEFAULT NULL COMMENT '操作场景：0-测评，1-点击，2-加购',
  `first_level_type_name` varchar(100) DEFAULT NULL COMMENT '一级类型名称',
  `second_level_type_name` varchar(100) DEFAULT NULL COMMENT '二级类型名称',
  `commission_type` varchar(50) DEFAULT 'percentage' COMMENT '佣金计算方式：percentage-按比例，fixed-固定金额',
  `commission_rate` decimal(10,4) DEFAULT NULL COMMENT '佣金率（百分比，如10.5表示10.5%）',
  `commission_amount` decimal(10,2) DEFAULT NULL COMMENT '固定佣金金额（当commission_type为fixed时使用）',
  `currency` varchar(10) DEFAULT 'KRW' COMMENT '币种',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_operation_scene` (`operation_scene`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单类型表-测评系统';

-- ============================================
-- 3. 店铺管理表
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
  KEY `idx_platform_code` (`platform_code`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='店铺表-测评系统';

-- ============================================
-- 4. 产品管理表
-- ============================================

-- 产品表（客户视角）
DROP TABLE IF EXISTS `bt_product`;
CREATE TABLE `bt_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID（关联后台系统的bt_tenant表）',
  `store_id` bigint(20) NOT NULL COMMENT '店铺ID',
  `platform_code` varchar(50) NOT NULL COMMENT '平台编码（关联后台系统的bt_platform表的platform_code）',
  `platform_product_id` varchar(100) NOT NULL COMMENT '平台产品ID（唯一，不可修改）',
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
  UNIQUE KEY `uk_platform_product_id` (`platform_product_id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_store_id` (`store_id`),
  KEY `idx_platform_code` (`platform_code`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品表-测评系统';

-- ============================================
-- 5. 订单创建和填写表
-- ============================================

-- 批次订单表（客户创建）
DROP TABLE IF EXISTS `bt_order_batch`;
CREATE TABLE `bt_order_batch` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID（关联后台系统的bt_tenant表）',
  `batch_order_status` tinyint(1) DEFAULT 0 COMMENT '批次订单状态：0-待提交，1-待支付佣金，2-已取消，3-已支付佣金',
  `total_quantity` int(11) DEFAULT 0 COMMENT '总数量',
  `total_amount` decimal(10,2) DEFAULT 0.00 COMMENT '总金额（预算）',
  `actual_amount` decimal(10,2) DEFAULT NULL COMMENT '实际金额',
  `currency` varchar(10) DEFAULT 'KRW' COMMENT '币种',
  `batch_text_content` longtext DEFAULT NULL COMMENT '批次文本内容（记录提交的完整数据）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_batch_order_status` (`batch_order_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='批次订单表-测评系统';

-- 订单表（客户填写）
DROP TABLE IF EXISTS `bt_order`;
CREATE TABLE `bt_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID（关联后台系统的bt_tenant表）',
  `order_batch_id` bigint(20) NOT NULL COMMENT '批次订单ID',
  `product_id` bigint(20) NOT NULL COMMENT '产品ID',
  `platform_product_id` varchar(100) DEFAULT NULL COMMENT '平台产品ID（冗余字段，冗余bt_product表）',
  `store_id` bigint(20) NOT NULL COMMENT '店铺ID',
  `store_name_kr` varchar(200) DEFAULT NULL COMMENT '店铺韩文名称（冗余字段）',
  `store_name_custom` varchar(200) DEFAULT NULL COMMENT '店铺自定义备注名称（冗余字段）',
  `platform_code` varchar(50) NOT NULL COMMENT '平台编码（关联后台系统的bt_platform表的platform_code）',
  `platform_name` varchar(100) DEFAULT NULL COMMENT '平台名称（冗余字段）',
  `order_type_id` bigint(20) NOT NULL COMMENT '订单类型ID（关联后台系统）',
  `operation_scene` tinyint(1) DEFAULT NULL COMMENT '操作场景（冗余字段，冗余bt_order_type表）：0-测评，1-点击，2-加购',
  `first_level_type_name` varchar(100) DEFAULT NULL COMMENT '一级类型名称（冗余字段，冗余bt_order_type表）',
  `second_level_type_name` varchar(100) DEFAULT NULL COMMENT '二级类型名称（冗余字段，冗余bt_order_type表）',
  `product_title` varchar(500) DEFAULT NULL COMMENT '产品标题（冗余字段）',
  `product_link` varchar(1000) DEFAULT NULL COMMENT '产品链接（冗余字段）',
  `attribute_name` varchar(200) DEFAULT NULL COMMENT '属性名称（冗余字段）',
  `main_image_url` varchar(1000) DEFAULT NULL COMMENT '产品主图URL（冗余字段）',
  `selling_price` decimal(10,2) DEFAULT NULL COMMENT '售价（冗余字段）',
  `order_status` tinyint(1) DEFAULT 0 COMMENT '订单状态：0-待开始，1-待分配，2-进行中，3-待确认，4-已完成',
  `account_assign_result` tinyint(1) DEFAULT NULL COMMENT '账号分配结果：0-失败，1-成功',
  `account_assign_result_desc` varchar(500) DEFAULT NULL COMMENT '账号分配结果描述',
  `account_assign_time` datetime DEFAULT NULL COMMENT '账号分配时间',
  `platform_order_no` varchar(200) DEFAULT NULL COMMENT '平台订单号',
  `express_company` varchar(100) DEFAULT NULL COMMENT '快递公司',
  `tracking_number` varchar(200) DEFAULT NULL COMMENT '物流单号',
  `actual_order_price` decimal(10,2) DEFAULT NULL COMMENT '实际下单价格',
  `order_account` varchar(100) DEFAULT NULL COMMENT '下单账号',
  `device_id` varchar(100) DEFAULT NULL COMMENT '设备ID',
  `keyword` varchar(200) DEFAULT NULL COMMENT '关键词',
  `planned_click_count` int(11) DEFAULT 0 COMMENT '计划点击次数',
  `actual_click_count` int(11) DEFAULT 0 COMMENT '实际点击次数',
  `cart_count` int(11) DEFAULT 0 COMMENT '加购数量',
  `planned_execution_date` date DEFAULT NULL COMMENT '计划执行日期',
  `execution_time` datetime DEFAULT NULL COMMENT '执行时间',
  `review_text_content` text DEFAULT NULL COMMENT '评论文字内容',
  `review_image_content` text DEFAULT NULL COMMENT '评论图片内容',
  `execution_result` tinyint(1) DEFAULT NULL COMMENT '执行结果：0-失败，1-成功',
  `execution_result_desc` varchar(500) DEFAULT NULL COMMENT '执行结果描述',
  `auto_execution_count` int(11) DEFAULT 0 COMMENT '自动化执行次数（用于重试）',
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
  KEY `idx_order_status` (`order_status`),
  KEY `idx_order_type_id` (`order_type_id`),
  KEY `idx_platform_order_no` (`platform_order_no`),
  KEY `idx_device_id` (`device_id`),
  KEY `idx_planned_execution_date` (`planned_execution_date`),
  KEY `idx_operation_scene` (`operation_scene`),
  KEY `idx_execution_result` (`execution_result`),
  KEY `idx_order_account` (`order_account`),
  KEY `idx_account_assign_result` (`account_assign_result`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表-测评系统';

-- ============================================
-- 初始化数据
-- ============================================

-- 初始化平台数据
INSERT INTO `bt_platform` (`platform_code`, `platform_name`, `status`, `sort`, `create_by`) VALUES
('coupang', 'Coupang', 1, 1, 'system'),
('naver', 'Naver', 1, 2, 'system');

SET FOREIGN_KEY_CHECKS = 1;

