# 测评后台管理系统数据库设计文档

## 一、系统概述

**测评后台管理系统**是管理员和员工使用的管理系统，用于管理租户、平台、测评账号、财务账单、帮助文档等。

## 二、数据库设计原则

1. 基于 eladmin 框架的数据库设计规范
2. 使用统一的字段命名规范（下划线命名）
3. 所有表包含创建时间、更新时间、创建人、更新人等基础字段
4. 管理员和员工可以查看和管理所有数据

## 三、表结构设计

### 3.1 租户管理模块

#### 3.1.1 租户表 (bt_tenant)
- 存储租户（企业）基本信息
- 管理员创建租户账号
- **管理员权限**：创建、编辑、禁用/启用租户账号
- **字段说明**：
  - `tenant_name`：租户名称（公司名称）
  - `email`：邮箱（唯一）
  - `wechat_id`：企业负责人微信号码
  - `contact_phone`：负责人电话号码
  - `status`：状态（0-禁用，1-启用）
  - `expire_time`：到期时间

### 3.2 平台管理模块

#### 3.2.1 平台表 (bt_platform)
- 存储平台信息（coupang、naver等）
- 支持平台扩展
- **管理员权限**：创建、编辑、禁用/启用平台
- **字段说明**：
  - `platform_code`：平台编码（唯一）
  - `platform_name`：平台名称
  - `status`：状态（0-禁用，1-启用）
  - `sort`：排序

### 3.3 测评账号管理模块

#### 3.3.1 测评账号表 (bt_review_account)
- 存储测评账号信息
- 支持自动分配等
- **管理员权限**：创建、编辑、禁用/启用测评账号
- **字段说明**：
  - `account_name`：账号名称
  - `password`：账号密码（明文存储）
  - `platform_code`：平台编码（关联平台表的platform_code）
  - `device_id`：设备ID（关联设备表的device_id）
  - `is_auto_assign`：是否自动分配（0-否，1-是）
  - `execution_status`：执行状态（idle-空闲，executing-执行中）
  - `current_tasks`：当前任务数
- **订单类型关联**：通过 `bt_review_account_order_type` 关联表支持一个账号关联多个订单类型
- **设备关联**：通过 `device_id` 字段关联设备表，一个账号对应一个设备

#### 3.3.1.1 账号订单类型关联表 (bt_review_account_order_type)
- 存储账号与订单类型的多对多关联关系
- **管理员权限**：为账号分配订单类型
- **字段说明**：
  - `review_account_id`：测评账号ID
  - `order_type_id`：订单类型ID（关联订单类型表）
  - `sort`：排序
- **业务逻辑**：一个账号可以关联多个订单类型，用于订单分配时的匹配

#### 3.3.2 设备表 (bt_device)
- 存储设备信息
- **管理员权限**：创建、编辑、禁用/启用设备
- **字段说明**：
  - `device_id`：设备ID（设备唯一标识，唯一键约束）
  - `device_name`：设备名称
  - `device_type`：设备类型
  - `status`：状态（0-禁用，1-启用）
- **业务逻辑**：设备与账号的关联关系通过账号表的 `device_id` 字段实现，一个账号对应一个设备
- **唯一性约束**：`device_id` 字段具有唯一键约束，确保每个设备ID在系统中唯一

### 3.4 财务账单管理模块

#### 3.4.1 账单表 (bt_bill)
- 存储账单信息
- 关联租户和订单
- **管理员权限**：查看所有租户的账单，支付状态管理
- **字段说明**：
  - `tenant_id`：租户ID
  - `order_batch_id`：批次订单ID（关联测评系统的订单）
  - `bill_type`：账单类型
  - `budget_amount`：预算金额
  - `actual_amount`：实际金额
  - `currency`：币种（默认KRW）
  - `payment_status`：支付状态（unpaid-未支付，paid-已支付，partial_paid-部分支付，refunded-已退款）
  - `payment_time`：支付时间
  - `payment_method`：支付方式
  - `payment_remark`：支付备注
  - **业务逻辑**：管理员点击状态流转，记录具体金额、币种等信息

#### 3.4.2 账单明细表 (bt_bill_detail)
- 存储账单明细信息
- **管理员权限**：查看账单明细
- **字段说明**：
  - `bill_id`：账单ID
  - `order_detail_id`：明细订单ID（关联测评系统的订单）
  - `item_name`：明细项名称
  - `item_type`：明细项类型
  - `quantity`：数量
  - `unit_price`：单价
  - `budget_amount`：预算金额
  - `actual_amount`：实际金额
  - `currency`：币种

### 3.5 订单类型管理模块

#### 3.5.1 订单类型表 (bt_order_type)
- 存储订单类型信息（点击、加购、测评等）
- 支持根据订单类型计算佣金
- **管理员权限**：创建、编辑、禁用/启用订单类型，配置佣金参数
- **字段说明**：
  - `type_name`：类型名称
  - `commission_type`：佣金计算方式（percentage-按比例，fixed-固定金额）
  - `commission_rate`：佣金率（百分比，如10.5表示10.5%）
  - `commission_amount`：固定佣金金额（当commission_type为fixed时使用）
  - `currency`：币种（默认KRW）
  - `sort`：排序
  - `status`：状态（0-禁用，1-启用）
- **佣金计算逻辑**：
  - 按比例计算：佣金 = 订单金额 × commission_rate / 100
  - 固定金额：佣金 = commission_amount

### 3.6 帮助和公共管理模块

#### 3.6.1 帮助文档表 (bt_help_document)
- 存储帮助文档信息，支持目录层级结构
- **管理员权限**：创建、编辑帮助文档和目录
- **字段说明**：
  - `parent_id`：父级ID（用于构建目录层级结构，NULL表示根目录）
  - `is_directory`：是否是目录（0-文档，1-目录）
  - `doc_title`：文档标题/目录名称
  - `doc_content`：文档内容（目录时可为空）
  - `doc_type`：文档类型
  - `doc_category`：文档分类
  - `sort`：排序
  - `status`：状态（0-禁用，1-启用）
  - `view_count`：查看次数
- **业务逻辑**：
  - 支持多级目录结构，通过 `parent_id` 构建树形结构
  - 目录和文档可以混合在同一层级
  - 通过 `is_directory` 字段区分目录和文档

## 四、关键业务逻辑

1. **租户账号创建**：管理员创建租户账号
2. **订单分配**：
   - 自动分配：定时任务根据账号订单类型匹配自动分配
   - 人工处理：管理员点击人工处理按钮，直接修改实际成交订单、付款信息、付款人等
3. **设备绑定**：设备可以绑定账号，也可以不绑定，需均匀分布
4. **支付状态流转**：管理员点击状态流转，记录具体金额、币种等信息

## 五、权限说明

### 管理员账号权限
- 创建员工账号，编辑各项权限
- 各填写字段的编辑修改权限
- 查看所有租户订单数据
- 创建租户账号
- 用户状态维护（禁用/启用）

### 员工账号权限
- 根据管理员分配的权限进行操作
- 查看和管理分配的数据

## 六、表关系说明

### 6.1 核心表关系

```
bt_tenant (租户表)
    ↓ (1:N)
bt_bill (账单表)
    ↓ (1:N)
bt_bill_detail (账单明细表)

bt_platform (平台表)
    ↓ (1:N)
bt_review_account (测评账号表)
    ↓ (1:1)
bt_device (设备表)
    ↓ (N:M)
bt_review_account_order_type (账号订单类型关联表)
    ↓ (N:1)
bt_order_type (订单类型表)

bt_order_type (订单类型表) - 独立表，供测评系统关联引用
```

### 6.2 与测评系统的关联

- `bt_bill.order_batch_id` → 关联测评系统的批次订单
- `bt_bill_detail.order_detail_id` → 关联测评系统的明细订单
- `bt_order_type.id` → 测评系统的订单表通过 `order_type_id` 关联

## 七、数据字典

详见 SQL 脚本中的表注释和字段注释。
