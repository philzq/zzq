-- ============================================
-- 测评后台管理系统数据库设计
-- 基于 eladmin 框架
-- 管理员和员工使用
-- 只包含：租户表、平台表、帮助文档表、测评账号表、账单表
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
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租户表-后台管理系统';

-- ============================================
-- 2. 平台管理表
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
  UNIQUE KEY `uk_platform_code` (`platform_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='平台表-后台管理系统';

-- ============================================
-- 3. 测评账号管理表
-- ============================================

-- 测评账号表
DROP TABLE IF EXISTS `bt_review_account`;
CREATE TABLE `bt_review_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `account_name` varchar(100) NOT NULL COMMENT '账号名称',
  `account_type` varchar(50) DEFAULT NULL COMMENT '账号类型',
  `platform_id` bigint(20) DEFAULT NULL COMMENT '平台ID',
  `is_auto_assign` tinyint(1) DEFAULT 1 COMMENT '是否自动分配：0-否，1-是',
  `execution_status` varchar(50) DEFAULT 'idle' COMMENT '执行状态：idle-空闲，executing-执行中',
  `max_concurrent_tasks` int(11) DEFAULT 1 COMMENT '最大并发任务数',
  `current_tasks` int(11) DEFAULT 0 COMMENT '当前任务数',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_platform_id` (`platform_id`),
  KEY `idx_execution_status` (`execution_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测评账号表-后台管理系统';

-- 账号能力标签表
DROP TABLE IF EXISTS `bt_account_capability`;
CREATE TABLE `bt_account_capability` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `review_account_id` bigint(20) NOT NULL COMMENT '测评账号ID',
  `capability_tag` varchar(100) NOT NULL COMMENT '能力标签',
  `capability_value` varchar(200) DEFAULT NULL COMMENT '能力值',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_review_account_id` (`review_account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账号能力标签表-后台管理系统';

-- 账号设备绑定表
DROP TABLE IF EXISTS `bt_account_device`;
CREATE TABLE `bt_account_device` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `review_account_id` bigint(20) NOT NULL COMMENT '测评账号ID',
  `device_id` varchar(100) NOT NULL COMMENT '设备ID',
  `device_name` varchar(200) DEFAULT NULL COMMENT '设备名称',
  `device_type` varchar(50) DEFAULT NULL COMMENT '设备类型',
  `device_info` text DEFAULT NULL COMMENT '设备信息JSON',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `bind_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '绑定时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_review_account_id` (`review_account_id`),
  UNIQUE KEY `uk_device_id` (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账号设备绑定表-后台管理系统';

-- 账号任务队列表
DROP TABLE IF EXISTS `bt_account_task_queue`;
CREATE TABLE `bt_account_task_queue` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `review_account_id` bigint(20) NOT NULL COMMENT '测评账号ID',
  `order_detail_id` bigint(20) NOT NULL COMMENT '明细订单ID（关联测评系统的订单）',
  `queue_order` int(11) DEFAULT 0 COMMENT '队列顺序',
  `queue_status` varchar(50) DEFAULT 'pending' COMMENT '队列状态：pending-待执行，executing-执行中，completed-已完成，failed-失败',
  `assign_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '分配时间',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `complete_time` datetime DEFAULT NULL COMMENT '完成时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_review_account_id` (`review_account_id`),
  KEY `idx_order_detail_id` (`order_detail_id`),
  KEY `idx_queue_status` (`queue_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账号任务队列表-后台管理系统';

-- ============================================
-- 4. 财务账单管理表
-- ============================================

-- 账单表
DROP TABLE IF EXISTS `bt_bill`;
CREATE TABLE `bt_bill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `bill_no` varchar(100) NOT NULL COMMENT '账单号',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  `order_batch_id` bigint(20) DEFAULT NULL COMMENT '批次订单ID（关联测评系统的订单）',
  `bill_type` varchar(50) DEFAULT NULL COMMENT '账单类型',
  `budget_amount` decimal(10,2) DEFAULT 0.00 COMMENT '预算金额',
  `actual_amount` decimal(10,2) DEFAULT NULL COMMENT '实际金额',
  `currency` varchar(10) DEFAULT 'KRW' COMMENT '币种',
  `payment_status` varchar(50) DEFAULT 'unpaid' COMMENT '支付状态：unpaid-未支付，paid-已支付，partial_paid-部分支付，refunded-已退款',
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
  UNIQUE KEY `uk_bill_no` (`bill_no`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_order_batch_id` (`order_batch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单表-后台管理系统';

-- 账单明细表
DROP TABLE IF EXISTS `bt_bill_detail`;
CREATE TABLE `bt_bill_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `bill_id` bigint(20) NOT NULL COMMENT '账单ID',
  `order_detail_id` bigint(20) DEFAULT NULL COMMENT '明细订单ID（关联测评系统的订单）',
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
  KEY `idx_bill_id` (`bill_id`),
  KEY `idx_order_detail_id` (`order_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单明细表-后台管理系统';

-- ============================================
-- 5. 帮助和公共管理表
-- ============================================

-- 帮助文档表
DROP TABLE IF EXISTS `bt_help_document`;
CREATE TABLE `bt_help_document` (
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帮助文档表-后台管理系统';

-- ============================================
-- 初始化数据
-- ============================================

-- 初始化平台数据
INSERT INTO `bt_platform` (`platform_code`, `platform_name`, `status`, `sort`, `create_by`) VALUES
('coupang', 'Coupang', 1, 1, 'system'),
('naver', 'Naver', 1, 2, 'system');

SET FOREIGN_KEY_CHECKS = 1;
