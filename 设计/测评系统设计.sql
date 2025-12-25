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
                              `menu_type` TINYINT NOT NULL DEFAULT 2 COMMENT '菜单类型：1-系统菜单 2-租户菜单',
                              `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
                              `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
                              `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`tenant_id`),
                              INDEX `idx_status` (`status`),
                              INDEX `idx_menu_type` (`menu_type`),
                              INDEX `idx_tenant_name` (`tenant_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户表（客户主表）';

-- 2. 用户表（支持多租户）
CREATE TABLE `sys_user` (
                            `user_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                            `tenant_id` BIGINT DEFAULT NULL COMMENT '所属租户ID（NULL表示系统用户）',
                            `username` VARCHAR(50) NOT NULL COMMENT '用户名',
                            `nick_name` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
                            `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
                            `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
                            `gender` TINYINT(1) DEFAULT 1 COMMENT '性别 1-男 0-女',
                            `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
                            `password` VARCHAR(100) NOT NULL COMMENT '密码',
                            `enabled` TINYINT(1) DEFAULT 1 COMMENT '状态：1-启用 0-禁用',
                            `is_admin` TINYINT(1) DEFAULT 0 COMMENT '是否为租户管理员',
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
                            INDEX `idx_enabled` (`enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表（多租户）';

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
                            INDEX `idx_pid` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单表（两套菜单体系：系统菜单、租户菜单）';

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
                            INDEX `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表（两套角色体系）';

-- 5. 用户角色关联表
CREATE TABLE `sys_users_roles` (
                                   `user_id` BIGINT NOT NULL COMMENT '用户ID',
                                   `role_id` BIGINT NOT NULL COMMENT '角色ID',
                                   PRIMARY KEY (`user_id`, `role_id`),
                                   INDEX `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联';

-- 6. 角色菜单关联表
CREATE TABLE `sys_roles_menus` (
                                   `role_id` BIGINT NOT NULL COMMENT '角色ID',
                                   `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
                                   PRIMARY KEY (`role_id`, `menu_id`),
                                   INDEX `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联';

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
CREATE TABLE `biz_shop` (
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
CREATE TABLE `biz_product` (
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
                                  `type_code` VARCHAR(50) NOT NULL COMMENT '类型编码',
                                  `type_name` VARCHAR(100) NOT NULL COMMENT '类型名称',
                                  `description` VARCHAR(500) DEFAULT NULL COMMENT '描述',
                                  `commission_rate` DECIMAL(5,2) DEFAULT 0.00 COMMENT '佣金比例',
                                  `sort` INT DEFAULT 0 COMMENT '排序',
                                  `enabled` TINYINT(1) DEFAULT 1 COMMENT '是否启用',
                                  `is_system` TINYINT(1) DEFAULT 1 COMMENT '是否为系统数据',
                                  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  PRIMARY KEY (`type_id`),
                                  UNIQUE KEY `uk_type_code` (`type_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单类型表（全局共享，客户只读）';

-- 11. 订单主表（租户隔离）
CREATE TABLE `biz_order` (
                             `order_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单ID',
                             `tenant_id` BIGINT NOT NULL COMMENT '租户ID',
                             `user_id` BIGINT NOT NULL COMMENT '创建用户ID',
                             `order_no` VARCHAR(50) NOT NULL COMMENT '订单编号',
                             `shop_id` BIGINT NOT NULL COMMENT '店铺ID',
                             `type_id` BIGINT NOT NULL COMMENT '订单类型ID',
                             `order_status` TINYINT NOT NULL DEFAULT 0 COMMENT '订单状态：0-待开始 1-进行中 2-待确认 3-已完成 4-已取消',
                             `total_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '订单总金额',
                             `commission_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '佣金金额',
                             `paid_amount` DECIMAL(10,2) DEFAULT 0.00 COMMENT '已支付金额',
                             `payment_status` TINYINT DEFAULT 0 COMMENT '支付状态：0-未支付 1-已支付 2-部分支付',
                             `start_date` DATE DEFAULT NULL COMMENT '开始日期',
                             `end_date` DATE DEFAULT NULL COMMENT '结束日期',
                             `keywords` VARCHAR(1000) DEFAULT NULL COMMENT '关键词（多个用逗号分隔）',
                             `remark` VARCHAR(1000) DEFAULT NULL COMMENT '备注',
                             `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
                             `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
                             `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             PRIMARY KEY (`order_id`),
                             UNIQUE KEY `uk_order_no` (`order_no`),
                             INDEX `idx_tenant_id` (`tenant_id`),
                             INDEX `idx_user_id` (`user_id`),
                             INDEX `idx_shop_id` (`shop_id`),
                             INDEX `idx_order_status` (`order_status`),
                             INDEX `idx_start_date` (`start_date`),
                             INDEX `idx_end_date` (`end_date`),
                             INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单主表（租户隔离）';

-- 12. 订单明细表（租户隔离）
CREATE TABLE `biz_order_detail` (
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
CREATE TABLE `biz_bill` (
                            `bill_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '账单ID',
                            `tenant_id` BIGINT NOT NULL COMMENT '租户ID',
                            `bill_no` VARCHAR(50) NOT NULL COMMENT '账单编号',
                            `user_id` BIGINT NOT NULL COMMENT '用户ID',
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
                            UNIQUE KEY `uk_bill_no` (`bill_no`),
                            INDEX `idx_tenant_id` (`tenant_id`),
                            INDEX `idx_user_id` (`user_id`),
                            INDEX `idx_bill_type` (`bill_type`),
                            INDEX `idx_order_id` (`order_id`),
                            INDEX `idx_payment_time` (`payment_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='财务账单表（租户隔离）';

-- 14. 帮助文档表（系统级）
CREATE TABLE `sys_help_doc` (
                                `doc_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '文档ID',
                                `doc_type` TINYINT NOT NULL DEFAULT 1 COMMENT '文档类型：1-系统帮助 2-租户帮助',
                                `tenant_id` BIGINT DEFAULT NULL COMMENT '租户ID（NULL表示系统帮助）',
                                `title` VARCHAR(200) NOT NULL COMMENT '标题',
                                `content` LONGTEXT NOT NULL COMMENT '内容',
                                `category` VARCHAR(50) DEFAULT NULL COMMENT '分类',
                                `sort` INT DEFAULT 0 COMMENT '排序',
                                `view_count` INT DEFAULT 0 COMMENT '查看次数',
                                `enabled` TINYINT(1) DEFAULT 1 COMMENT '是否启用',
                                `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
                                `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
                                `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                PRIMARY KEY (`doc_id`),
                                INDEX `idx_doc_type` (`doc_type`),
                                INDEX `idx_tenant_id` (`tenant_id`),
                                INDEX `idx_category` (`category`),
                                FULLTEXT KEY `ft_title_content` (`title`, `content`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='帮助文档表';

-- 15. 短信/邮箱验证码表
CREATE TABLE `sys_verify_code` (
                                   `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                   `receiver` VARCHAR(100) NOT NULL COMMENT '接收者（手机/邮箱）',
                                   `code_type` VARCHAR(50) NOT NULL COMMENT '验证码类型：REGISTER/RESET_PWD',
                                   `code` VARCHAR(20) NOT NULL COMMENT '验证码',
                                   `expire_time` DATETIME NOT NULL COMMENT '过期时间',
                                   `used` TINYINT(1) DEFAULT 0 COMMENT '是否已使用',
                                   `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   PRIMARY KEY (`id`),
                                   INDEX `idx_receiver_type` (`receiver`, `code_type`),
                                   INDEX `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='验证码表';

-- 16. 系统日志表
CREATE TABLE `sys_log` (
                           `log_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
                           `tenant_id` BIGINT DEFAULT NULL COMMENT '租户ID',
                           `log_type` VARCHAR(255) DEFAULT NULL COMMENT '日志类型',
                           `description` VARCHAR(255) DEFAULT NULL,
                           `method` VARCHAR(255) DEFAULT NULL,
                           `params` TEXT DEFAULT NULL,
                           `request_ip` VARCHAR(255) DEFAULT NULL,
                           `time` BIGINT DEFAULT NULL,
                           `username` VARCHAR(255) DEFAULT NULL,
                           `address` VARCHAR(255) DEFAULT NULL,
                           `browser` VARCHAR(255) DEFAULT NULL,
                           `exception_detail` TEXT DEFAULT NULL,
                           `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
                           PRIMARY KEY (`log_id`),
                           INDEX `idx_tenant_id` (`tenant_id`),
                           INDEX `idx_log_type` (`log_type`),
                           INDEX `idx_username` (`username`),
                           INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统日志表';

-- 17. 数据字典表（系统级）
CREATE TABLE `sys_dict` (
                            `dict_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
                            `name` VARCHAR(255) NOT NULL COMMENT '字典名称',
                            `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
                            `create_by` VARCHAR(255) DEFAULT NULL COMMENT '创建者',
                            `update_by` VARCHAR(255) DEFAULT NULL COMMENT '更新者',
                            `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
                            `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`dict_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据字典表';

-- 18. 字典详情表
CREATE TABLE `sys_dict_detail` (
                                   `detail_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                   `dict_id` BIGINT DEFAULT NULL COMMENT '字典id',
                                   `label` VARCHAR(255) NOT NULL COMMENT '字典标签',
                                   `value` VARCHAR(255) NOT NULL COMMENT '字典值',
                                   `dict_sort` INT DEFAULT NULL COMMENT '排序',
                                   `create_by` VARCHAR(255) DEFAULT NULL COMMENT '创建者',
                                   `update_by` VARCHAR(255) DEFAULT NULL COMMENT '更新者',
                                   `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
                                   `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   PRIMARY KEY (`detail_id`),
                                   INDEX `idx_dict_id` (`dict_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典详情表';