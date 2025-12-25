-- 测评填单系统数据库设计
-- 版本: 2.0 - 支持多租户数据隔离
CREATE DATABASE IF NOT EXISTS evaluation_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE evaluation_system;

-- ==================== 系统核心表（全局共享，租户间隔离） ====================

-- 1. 租户表（客户主表）
CREATE TABLE `sys_tenant` (
                              `tenant_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '租户ID',
                              `tenant_name` VARCHAR(100) NOT NULL COMMENT '租户名称（公司名称）',
                              `contact_person` VARCHAR(50) DEFAULT NULL COMMENT '联系人',
                              `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
                              `contact_email` VARCHAR(100) DEFAULT NULL COMMENT '联系邮箱',
                              `company_name` VARCHAR(100) DEFAULT NULL COMMENT '公司名称',
                              `manager_wechat` VARCHAR(100) DEFAULT NULL COMMENT '企业负责人微信号码',
                              `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-正常 0-禁用',
                              `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
                              `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
                              `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`tenant_id`),
                              INDEX `idx_status` (`status`),
                              INDEX `idx_tenant_name` (`tenant_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户表（客户主表）';

-- 2. 用户表（支持多租户）
CREATE TABLE `sys_user` (
                            `user_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                            `tenant_id` BIGINT DEFAULT NULL COMMENT '所属租户ID（NULL表示系统用户，非NULL表示租户用户，根据创建者自动确定）',
                            `username` VARCHAR(50) NOT NULL COMMENT '用户名',
                            `nick_name` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
                            `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
                            `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
                            `gender` TINYINT(1) DEFAULT 1 COMMENT '性别 1-男 0-女',
                            `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
                            `password` VARCHAR(100) NOT NULL COMMENT '密码',
                            `enabled` TINYINT(1) DEFAULT 1 COMMENT '状态：1-启用 0-禁用',
                            `is_admin` TINYINT(1) DEFAULT 0 COMMENT '是否为超管：系统用户时is_admin=1表示系统超管，租户用户时is_admin=1表示租户超管',
                            `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
                            `pwd_reset_time` DATETIME DEFAULT NULL COMMENT '密码重置时间',
                            `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
                            `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
                            `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`user_id`),
                            UNIQUE KEY `uk_username_tenant` (`username`, `tenant_id`) COMMENT '同一租户下用户名唯一',
                            UNIQUE KEY `uk_phone` (`phone`),
                            UNIQUE KEY `uk_email` (`email`),
                            INDEX `idx_tenant_id` (`tenant_id`),
                            INDEX `idx_tenant_enabled` (`tenant_id`, `enabled`),
                            INDEX `idx_tenant_admin` (`tenant_id`, `is_admin`),
                            INDEX `idx_enabled` (`enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表（两套用户体系：系统用户tenant_id=NULL，租户用户tenant_id!=NULL）';

-- 3. 菜单表（支持两套菜单体系）
CREATE TABLE `sys_menu` (
                            `menu_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
                            `pid` BIGINT DEFAULT NULL COMMENT '上级菜单ID',
                            `menu_type` TINYINT NOT NULL DEFAULT 1 COMMENT '菜单类型：1-系统菜单 2-租户菜单',
                            `title` VARCHAR(50) NOT NULL COMMENT '菜单标题',
                            `name` VARCHAR(50) DEFAULT NULL COMMENT '组件名称',
                            `component` VARCHAR(100) DEFAULT NULL COMMENT '组件路径',
                            `menu_sort` INT DEFAULT 999 COMMENT '排序',
                            `icon` VARCHAR(50) DEFAULT NULL COMMENT '图标',
                            `path` VARCHAR(100) DEFAULT NULL COMMENT '路由路径',
                            `iframe` TINYINT(1) DEFAULT 0 COMMENT '是否外链',
                            `cache` TINYINT(1) DEFAULT 0 COMMENT '缓存',
                            `hidden` TINYINT(1) DEFAULT 0 COMMENT '隐藏',
                            `permission` VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
                            `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建者',
                            `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新者',
                            `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`menu_id`),
                            INDEX `idx_menu_type` (`menu_type`),
                            INDEX `idx_menu_type_hidden_sort` (`menu_type`, `hidden`, `menu_sort`),
                            INDEX `idx_menu_type_pid` (`menu_type`, `pid`),
                            INDEX `idx_pid` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单表（两套菜单体系：系统菜单menu_type=1、租户菜单menu_type=2）';

-- 4. 角色表（支持系统角色和租户角色）
CREATE TABLE `sys_role` (
                            `role_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
                            `tenant_id` BIGINT DEFAULT NULL COMMENT '租户ID（NULL表示系统角色，非NULL表示租户角色）',
                            `name` VARCHAR(50) NOT NULL COMMENT '角色名称',
                            `level` INT DEFAULT 999 COMMENT '角色级别',
                            `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
                            `data_scope` VARCHAR(20) DEFAULT 'SELF' COMMENT '数据权限范围',
                            `is_system` TINYINT(1) DEFAULT 0 COMMENT '是否为系统内置角色',
                            `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建者',
                            `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新者',
                            `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`role_id`),
                            UNIQUE KEY `uk_name_tenant` (`name`, `tenant_id`) COMMENT '同一租户下角色名唯一',
                            INDEX `idx_tenant_id` (`tenant_id`),
                            INDEX `idx_tenant_system` (`tenant_id`, `is_system`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表（两套角色体系：系统角色tenant_id=NULL，租户角色tenant_id!=NULL）';

-- 5. 用户角色关联表
CREATE TABLE `sys_users_roles` (
                                   `user_id` BIGINT NOT NULL COMMENT '用户ID',
                                   `role_id` BIGINT NOT NULL COMMENT '角色ID',
                                   PRIMARY KEY (`user_id`, `role_id`),
                                   INDEX `idx_user_id` (`user_id`),
                                   INDEX `idx_role_id` (`role_id`),
                                   INDEX `idx_user_role` (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联';

-- 6. 角色菜单关联表
CREATE TABLE `sys_roles_menus` (
                                   `role_id` BIGINT NOT NULL COMMENT '角色ID',
                                   `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
                                   PRIMARY KEY (`role_id`, `menu_id`),
                                   INDEX `idx_role_id` (`role_id`),
                                   INDEX `idx_menu_id` (`menu_id`),
                                   INDEX `idx_role_menu` (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联（需保证：系统角色关联系统菜单，租户角色关联租户菜单）';

-- ==================== 业务表（租户数据隔离） ====================

-- 7. 平台表（全局共享，系统管理员可编辑，客户只读）
CREATE TABLE `sys_platform` (
                                `platform_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '平台ID',
                                `platform_code` VARCHAR(50) NOT NULL COMMENT '平台编码',
                                `platform_name` VARCHAR(100) NOT NULL COMMENT '平台名称',
                                `platform_icon` VARCHAR(255) DEFAULT NULL COMMENT '平台图标',
                                `description` VARCHAR(500) DEFAULT NULL COMMENT '描述',
                                `sort` INT DEFAULT 0 COMMENT '排序',
                                `enabled` TINYINT(1) DEFAULT 1 COMMENT '是否启用',
                                `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
                                `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
                                `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                PRIMARY KEY (`platform_id`),
                                UNIQUE KEY `uk_platform_code` (`platform_code`),
                                INDEX `idx_enabled` (`enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='平台表（全局共享，客户只读）';

-- 8. 店铺表（租户隔离）
CREATE TABLE `bt_shop` (
                            `shop_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '店铺ID',
                            `tenant_id` BIGINT NOT NULL COMMENT '租户ID',
                            `shop_kr_name` VARCHAR(200) NOT NULL COMMENT '店铺韩文名称',
                            `shop_custom_name` VARCHAR(200) NOT NULL COMMENT '店铺自定义名称',
                            `platform_id` BIGINT NOT NULL COMMENT '平台ID',
                            `status` TINYINT DEFAULT 1 COMMENT '状态：1-正常 0-停用',
                            `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
                            `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
                            `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`shop_id`),
                            UNIQUE KEY `uk_tenant_kr_name` (`tenant_id`, `shop_kr_name`, `platform_id`) COMMENT '同一租户下平台内韩文名唯一',
                            INDEX `idx_tenant_id` (`tenant_id`),
                            INDEX `idx_platform_id` (`platform_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='店铺表（租户隔离）';

-- 9. 产品表（租户隔离）
CREATE TABLE `bt_product` (
                               `product_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '产品ID',
                               `tenant_id` BIGINT NOT NULL COMMENT '租户ID',
                               `shop_id` BIGINT NOT NULL COMMENT '所属店铺ID',
                               `product_title` VARCHAR(500) NOT NULL COMMENT '产品标题',
                               `product_url` VARCHAR(1000) DEFAULT NULL COMMENT '产品链接',
                               `main_image` VARCHAR(1000) DEFAULT NULL COMMENT '产品主图',
                               `sale_price` DECIMAL(10,2) DEFAULT NULL COMMENT '售价',
                               `platform_product_id` VARCHAR(100) DEFAULT NULL COMMENT '平台产品ID',
                               `attribute_names` VARCHAR(1000) DEFAULT NULL COMMENT '属性名称（逗号分隔）',
                               `status` TINYINT DEFAULT 1 COMMENT '状态：1-正常 0-下架',
                               `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
                               `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
                               `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               PRIMARY KEY (`product_id`),
                               INDEX `idx_tenant_id` (`tenant_id`),
                               INDEX `idx_shop_id` (`shop_id`),
                               INDEX `idx_status` (`status`),
                               FULLTEXT KEY `ft_product_title` (`product_title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品表（租户隔离）';

-- 10. 订单类型表（全局共享，系统管理员可编辑，客户只读）
CREATE TABLE `sys_order_type` (
                                  `type_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '类型ID',
                                  `type_name` VARCHAR(100) NOT NULL COMMENT '类型名称',
                                  `description` VARCHAR(500) DEFAULT NULL COMMENT '描述',
                                  `commission_rate` DECIMAL(5,2) DEFAULT 0.00 COMMENT '佣金比例',
                                  `enabled` TINYINT(1) DEFAULT 1 COMMENT '是否启用',
                                  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
                                  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
                                  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  PRIMARY KEY (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单类型表（全局共享，客户只读）';

-- 11. 订单主表（租户隔离）
CREATE TABLE `bt_order` (
                             `order_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单ID',
                             `tenant_id` BIGINT NOT NULL COMMENT '租户ID',
                             `shop_id` BIGINT NOT NULL COMMENT '店铺ID',
                             `type_id` BIGINT NOT NULL COMMENT '订单类型ID',
                             `order_status` TINYINT NOT NULL DEFAULT 0 COMMENT '订单状态：0-待开始 1-进行中 2-待确认 3-已完成 4-已取消',
                             `total_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '订单总金额',
                             `commission_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '佣金金额',
                             `paid_amount` DECIMAL(10,2) DEFAULT 0.00 COMMENT '已支付金额',
                             `payment_status` TINYINT DEFAULT 0 COMMENT '支付状态：0-未支付 1-已支付 2-部分支付',
                             `order_dates` VARCHAR(2000) DEFAULT NULL COMMENT '订单日期（多个日期用逗号分隔，格式：YYYY-MM-DD）',
                             `keywords` VARCHAR(1000) DEFAULT NULL COMMENT '关键词（多个用逗号分隔）',
                             `remark` VARCHAR(1000) DEFAULT NULL COMMENT '备注',
                             `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
                             `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
                             `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             PRIMARY KEY (`order_id`),
                             INDEX `idx_tenant_id` (`tenant_id`),
                             INDEX `idx_shop_id` (`shop_id`),
                             INDEX `idx_order_status` (`order_status`),
                             INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单主表（租户隔离）';

-- 12. 订单明细表（租户隔离）
CREATE TABLE `bt_order_detail` (
                                    `detail_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '明细ID',
                                    `tenant_id` BIGINT NOT NULL COMMENT '租户ID',
                                    `order_id` BIGINT NOT NULL COMMENT '订单ID',
                                    `product_id` BIGINT NOT NULL COMMENT '产品ID',
                                    `quantity` INT NOT NULL DEFAULT 1 COMMENT '数量',
                                    `unit_price` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '单价',
                                    `total_price` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '总价',
                                    `date` DATE NOT NULL COMMENT '日期',
                                    `day_quantity` INT DEFAULT 1 COMMENT '当日数量',
                                    `status` TINYINT DEFAULT 0 COMMENT '状态：0-未开始 1-进行中 2-已完成',
                                    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
                                    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    PRIMARY KEY (`detail_id`),
                                    INDEX `idx_tenant_id` (`tenant_id`),
                                    INDEX `idx_order_id` (`order_id`),
                                    INDEX `idx_product_id` (`product_id`),
                                    INDEX `idx_date` (`date`),
                                    UNIQUE KEY `uk_order_product_date` (`order_id`, `product_id`, `date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单明细表（租户隔离）';

-- 13. 财务账单表（租户隔离）
CREATE TABLE `bt_bill` (
                            `bill_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '账单ID',
                            `tenant_id` BIGINT NOT NULL COMMENT '租户ID',
                            `bill_type` TINYINT NOT NULL COMMENT '账单类型：1-订单佣金 2-充值 3-退款',
                            `amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '金额',
                            `balance` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '余额',
                            `payment_method` VARCHAR(50) DEFAULT NULL COMMENT '支付方式',
                            `payment_time` DATETIME DEFAULT NULL COMMENT '支付时间',
                            `order_id` BIGINT DEFAULT NULL COMMENT '关联订单ID',
                            `status` TINYINT DEFAULT 0 COMMENT '状态：0-待支付 1-已支付 2-已取消',
                            `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
                            `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`bill_id`),
                            INDEX `idx_tenant_id` (`tenant_id`),
                            INDEX `idx_bill_type` (`bill_type`),
                            INDEX `idx_order_id` (`order_id`),
                            INDEX `idx_payment_time` (`payment_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='财务账单表（租户隔离）';

-- 14. 帮助文档表
CREATE TABLE `sys_help_doc` (
                                `doc_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '文档ID',
                                `menu_id` BIGINT DEFAULT NULL COMMENT '关联菜单ID（可选，关联到具体功能模块）',
                                `title` VARCHAR(200) NOT NULL COMMENT '标题',
                                `summary` VARCHAR(500) DEFAULT NULL COMMENT '摘要/简介（用于列表展示）',
                                `content` LONGTEXT NOT NULL COMMENT '内容（支持Markdown或HTML）',
                                `content_type` TINYINT DEFAULT 1 COMMENT '内容类型：1-纯文本 2-Markdown 3-HTML',
                                `category` VARCHAR(50) DEFAULT NULL COMMENT '分类（如：快速开始、功能说明、常见问题、操作指南等）',
                                `tags` VARCHAR(200) DEFAULT NULL COMMENT '标签（多个用逗号分隔，用于搜索）',
                                `icon` VARCHAR(100) DEFAULT NULL COMMENT '图标（用于分类展示）',
                                `sort` INT DEFAULT 0 COMMENT '排序',
                                `level` TINYINT DEFAULT 1 COMMENT '重要级别：1-普通 2-重要 3-置顶',
                                `view_count` INT DEFAULT 0 COMMENT '查看次数',
                                `helpful_count` INT DEFAULT 0 COMMENT '有帮助次数（用户反馈）',
                                `unhelpful_count` INT DEFAULT 0 COMMENT '无帮助次数（用户反馈）',
                                `enabled` TINYINT(1) DEFAULT 1 COMMENT '是否启用',
                                `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
                                `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
                                `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                PRIMARY KEY (`doc_id`),
                                INDEX `idx_menu_id` (`menu_id`),
                                INDEX `idx_category` (`category`),
                                INDEX `idx_category_sort` (`category`, `sort`),
                                INDEX `idx_level_sort` (`level`, `sort`),
                                FULLTEXT KEY `ft_title_content` (`title`, `content`, `tags`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='帮助文档表（全局共享，所有用户共用）';

-- ==================== 数据库设计说明文档 ====================
/*
【系统概述】
测评填单系统是一个支持多租户的SaaS系统，采用一套系统、两套用户体系、两套菜单体系的设计架构。
系统支持系统管理员/员工和租户（客户）两种用户类型，每种用户类型拥有独立的菜单体系和权限体系。

【核心设计特点】
1. 多租户架构：通过tenant_id字段实现数据隔离，每个租户的数据完全独立
2. 双用户体系：系统用户（tenant_id=NULL）和租户用户（tenant_id!=NULL）
3. 双菜单体系：系统菜单（menu_type=1）和租户菜单（menu_type=2）
4. 双角色体系：系统角色（tenant_id=NULL）和租户角色（tenant_id!=NULL）


【表结构分类】

一、系统核心表（全局共享，租户间隔离）
1. sys_tenant - 租户表
   - 存储租户（客户）基本信息
   - 字段：租户名称、联系人、联系方式、公司名称、负责人微信等
   - 索引：status、tenant_name

2. sys_user - 用户表
   - 支持系统用户和租户用户两套体系
   - tenant_id=NULL：系统用户（系统管理员、系统员工）
   - tenant_id!=NULL：租户用户（租户管理员、租户员工）
   - is_admin字段：系统用户时表示系统超管，租户用户时表示租户超管
   - 索引：tenant_id、tenant_enabled、tenant_admin

3. sys_menu - 菜单表
   - 支持系统菜单和租户菜单两套体系
   - menu_type=1：系统菜单（供系统用户使用）
   - menu_type=2：租户菜单（供租户用户使用）
   - 支持树形结构（pid字段）
   - 索引：menu_type、menu_type_hidden_sort、menu_type_pid

4. sys_role - 角色表
   - 支持系统角色和租户角色两套体系
   - tenant_id=NULL：系统角色（供系统用户使用）
   - tenant_id!=NULL：租户角色（供租户用户使用）
   - 索引：tenant_id、tenant_system

5. sys_users_roles - 用户角色关联表
   - 多对多关系：用户和角色的关联
   - 索引：user_id、role_id、user_role

6. sys_roles_menus - 角色菜单关联表
   - 多对多关系：角色和菜单的关联
   - 需保证：系统角色关联系统菜单，租户角色关联租户菜单
   - 索引：role_id、menu_id、role_menu

二、业务表（租户数据隔离）
7. sys_platform - 平台表
   - 全局共享，所有租户共用
   - 系统管理员可编辑，租户用户只读
   - 存储电商平台信息（如：Coupang、Gmarket等）

8. bt_shop - 店铺表
   - 租户隔离，每个租户的店铺数据独立
   - 关联平台表，一个租户可在多个平台开设店铺
   - 唯一约束：同一租户下，同一平台内店铺韩文名唯一
   - 索引：tenant_id、platform_id

9. bt_product - 产品表
   - 租户隔离，每个租户的产品数据独立
   - 关联店铺表，产品属于某个店铺
   - 支持全文搜索（product_title）
   - 索引：tenant_id、shop_id、status

10. sys_order_type - 订单类型表
    - 全局共享，所有租户共用
    - 系统管理员可编辑，租户用户只读
    - 存储订单类型及佣金比例

11. bt_order - 订单主表
    - 租户隔离，每个租户的订单数据独立
    - 支持多日期订单（order_dates字段，逗号分隔）
    - 订单状态：0-待开始 1-进行中 2-待确认 3-已完成 4-已取消
    - 支付状态：0-未支付 1-已支付 2-部分支付
    - 索引：tenant_id、shop_id、order_status、create_time

12. bt_order_detail - 订单明细表
    - 租户隔离，每个租户的订单明细数据独立
    - 关联订单主表和产品表
    - 支持按日期拆分订单（date字段）
    - 唯一约束：同一订单、同一产品、同一日期只能有一条明细
    - 索引：tenant_id、order_id、product_id、date

13. bt_bill - 财务账单表
    - 租户隔离，每个租户的账单数据独立
    - 账单类型：1-订单佣金 2-充值 3-退款
    - 关联订单表（可选）
    - 索引：tenant_id、bill_type、order_id、payment_time

14. sys_help_doc - 帮助文档表
    - 全局共享，所有用户（系统用户和租户用户）共用同一套帮助文档
    - 支持关联菜单（menu_id），实现上下文帮助
    - 支持分类、标签、全文搜索
    - 支持用户反馈统计（helpful_count、unhelpful_count）
    - 索引：menu_id、category、category_sort、level_sort、全文索引

【数据隔离机制】

1. 租户数据隔离
   - 所有业务表（bt_*）均包含tenant_id字段
   - 查询时必须加上tenant_id条件，确保数据隔离
   - 示例：SELECT * FROM bt_order WHERE tenant_id = ? AND ...

2. 全局共享数据
   - sys_platform：平台表，所有租户共用
   - sys_order_type：订单类型表，所有租户共用
   - sys_help_doc：帮助文档表，所有用户共用

3. 系统数据隔离
   - sys_user：通过tenant_id区分系统用户和租户用户
   - sys_role：通过tenant_id区分系统角色和租户角色
   - sys_menu：通过menu_type区分系统菜单和租户菜单

【用户体系设计】

1. 系统用户（tenant_id IS NULL）
   - 由系统超管或系统用户创建
   - is_admin=1：系统超管，拥有系统最高权限
   - is_admin=0：系统员工，由系统超管分配权限
   - 使用系统菜单（menu_type=1）
   - 使用系统角色（tenant_id IS NULL）

2. 租户用户（tenant_id IS NOT NULL）
   - 由系统用户创建租户时同步创建（租户超管）
   - 或由租户用户创建（租户员工）
   - is_admin=1：租户超管，拥有该租户的最高权限
   - is_admin=0：租户员工，由租户超管分配权限
   - 使用租户菜单（menu_type=2）
   - 使用租户角色（tenant_id = 用户的tenant_id）

3. 用户创建规则
   - 系统用户创建用户 → tenant_id = NULL（系统用户）
   - 系统超管创建租户 → 同步创建租户超管，tenant_id = 租户ID，is_admin = 1
   - 租户用户创建用户 → tenant_id = 创建者的tenant_id（租户用户），is_admin = 0

【权限体系设计】

1. 角色权限
   - 系统角色：供系统用户使用，关联系统菜单
   - 租户角色：供租户用户使用，关联租户菜单
   - 角色与菜单通过sys_roles_menus表关联

2. 用户权限
   - 用户通过sys_users_roles表分配角色
   - 用户权限 = 用户所有角色的菜单权限的并集

3. 数据权限
   - 系统用户：可访问所有租户数据（系统管理需要）
   - 租户用户：只能访问本租户数据（通过tenant_id隔离）

【索引设计说明】

1. 单字段索引
   - tenant_id：所有业务表必备，用于租户数据隔离查询
   - status/enabled：状态字段索引，用于筛选有效数据
   - create_time：创建时间索引，用于时间范围查询

2. 复合索引
   - tenant_id + enabled：按租户和启用状态查询
   - tenant_id + is_admin：按租户和管理员标识查询
   - menu_type + hidden + menu_sort：按菜单类型查询可见菜单并排序
   - category + sort：按分类查询并排序

3. 全文索引
   - bt_product.product_title：产品标题全文搜索
   - sys_help_doc(title, content, tags)：帮助文档全文搜索

4. 唯一索引
   - uk_username_tenant：同一租户下用户名唯一
   - uk_tenant_kr_name：同一租户下平台内店铺韩文名唯一
   - uk_order_product_date：同一订单、同一产品、同一日期唯一

【数据一致性规则】（应用层必须严格校验）

1. 用户与角色关系
   - 系统用户只能分配系统角色
   - 租户用户只能分配租户角色（且tenant_id必须匹配）

2. 角色与菜单关系
   - 系统角色只能关联系统菜单
   - 租户角色只能关联租户菜单

3. 用户创建规则
   - 系统用户创建的用户必须是系统用户
   - 系统超管创建租户时自动创建租户超管
   - 租户用户创建的用户必须是该租户的用户

4. 业务数据隔离
   - 所有业务表查询必须加上tenant_id条件
   - 系统用户可跨租户查询（管理需要）
   - 租户用户只能查询本租户数据


【应用层实现建议】

1. 数据访问层
   - 统一封装租户数据查询，自动添加tenant_id条件
   - 系统用户可指定tenant_id进行跨租户查询
   - 租户用户自动使用当前用户的tenant_id

2. 权限控制层
   - 统一封装用户类型判断逻辑
   - 统一封装菜单查询逻辑，根据用户类型自动选择对应菜单
   - 在拦截器或AOP中统一处理权限校验

3. 数据校验层
   - 在Service层严格校验数据一致性规则
   - 用户创建时自动设置tenant_id
   - 角色分配时校验用户类型和角色类型匹配

4. 枚举类定义
   - 用户类型：系统用户、租户用户
   - 菜单类型：系统菜单、租户菜单
   - 订单状态、支付状态等业务枚举

5. 异常处理
   - 租户数据访问越权异常
   - 用户类型与角色类型不匹配异常
   - 角色与菜单类型不匹配异常

【性能优化建议】

1. 查询优化
   - 所有租户数据查询必须使用tenant_id索引
   - 避免全表扫描，合理使用复合索引
   - 大数据量查询使用分页

2. 缓存策略
   - 菜单数据缓存（按用户类型）
   - 角色权限缓存（按用户）
   - 平台、订单类型等全局数据缓存

3. 数据库优化
   - 定期分析表统计信息
   - 根据查询模式调整索引
   - 考虑分区表（按tenant_id或时间）

【安全建议】

1. 数据隔离
   - 应用层必须严格校验tenant_id
   - 防止SQL注入，使用参数化查询
   - 租户用户不能修改tenant_id

2. 权限控制
   - 系统用户和租户用户权限完全隔离
   - 系统用户可管理所有租户数据
   - 租户用户只能管理本租户数据

3. 审计日志
   - 记录所有数据变更操作
   - 记录用户登录和权限变更
   - 记录跨租户数据访问

【版本说明】
- 版本：2.0
- 设计特点：支持多租户数据隔离、双用户体系、双菜单体系

*/

