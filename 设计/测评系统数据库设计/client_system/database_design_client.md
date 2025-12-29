# 测评系统数据库设计文档

## 一、系统概述

**测评系统**是客户使用的系统，用于管理店铺、产品、创建订单、填写订单、查询订单等。

## 二、数据库设计原则

1. 基于 eladmin 框架的数据库设计规范
2. 使用统一的字段命名规范（下划线命名）
3. 所有表包含创建时间、更新时间、创建人、更新人等基础字段
4. 支持逻辑删除（deleted 字段）
5. 客户只能查看和管理自己的数据（通过 tenant_id 关联后台系统的租户表）

## 三、表结构设计

### 3.1 店铺管理模块

#### 3.1.1 店铺表 (bt_store)
- 存储客户自己的店铺信息
- 关联后台系统的平台表和租户表
- 支持韩文名称和自定义备注名称
- **客户权限**：添加、修改自定义备注名称、删除店铺
- **字段说明**：
  - `tenant_id`：租户ID（关联后台系统的bt_tenant表）
  - `platform_id`：平台ID（关联后台系统的平台）
  - `store_name_kr`：店铺韩文名称
  - `store_name_custom`：店铺自定义备注名称
  - `status`：状态（0-禁用，1-启用）
- **业务逻辑**：删除店铺时，如果已有订单，则只标记删除，后台仍保留

### 3.2 产品管理模块

#### 3.2.1 产品表 (bt_product)
- 存储客户自己的产品信息
- 支持自动添加（输入产品链接爬取）和手动添加
- **客户权限**：添加、修改、删除产品
- **字段说明**：
  - `store_id`：店铺ID
  - `platform_id`：平台ID
  - `product_id`：产品ID（唯一，不可修改）
  - `product_title`：产品标题
  - `product_link`：产品链接
  - `selling_price`：售价
  - `add_type`：添加方式（1-自动添加，2-手动添加）
  - `status`：状态（0-禁用，1-启用）
- **业务逻辑**：已下单的产品信息不随产品修改而改变

#### 3.2.2 产品属性表 (bt_product_attribute)
- 存储产品属性信息
- 支持多属性
- **字段说明**：
  - `product_id`：产品ID
  - `attribute_name`：属性名称
  - `sort`：排序

#### 3.2.3 产品图片表 (bt_product_image)
- 存储产品图片信息
- 支持主图上传
- **字段说明**：
  - `product_id`：产品ID
  - `image_url`：图片URL
  - `image_type`：图片类型（1-主图，2-详情图）
  - `sort`：排序

### 3.3 订单创建和填写模块

#### 3.3.1 批次订单表 (bt_order_batch)
- 存储客户创建的批次订单
- 支持点击、加购等批量订单创建
- **客户权限**：创建批次订单、查看自己的批次订单、取消未开始的订单
- **字段说明**：
  - `batch_no`：批次订单号（唯一）
  - `tenant_id`：租户ID（关联后台系统的bt_tenant表）
  - `order_type_id`：订单类型ID（关联后台系统）
  - `order_status`：订单状态（pending-待开始，processing-进行中，pending_confirm-待确认，completed-已完成）
  - `payment_status`：支付状态（unpaid-未支付，paid-已支付，partial_paid-部分支付，refunded-已退款）
  - `total_quantity`：总数量
  - `total_amount`：总金额（预算）
  - `actual_amount`：实际金额
  - `currency`：币种
  - `start_date`：开始日期
  - `end_date`：结束日期

#### 3.3.2 明细订单表 (bt_order_detail)
- 存储客户填写的明细订单
- 支持多种填写方式：
  - 某一天，单一产品的订单信息
  - 某一天，不同产品的订单信息
  - 某单一产品某几天的订单信息
  - 不同产品某几天的订单信息
- **客户权限**：填写订单、导入订单、重新提交订单、查看自己的明细订单
- **字段说明**：
  - `order_batch_id`：批次订单ID
  - `detail_no`：明细订单号（唯一）
  - `product_id`：产品ID
  - `store_id`：店铺ID
  - `platform_id`：平台ID
  - `order_type_id`：订单类型ID（关联后台系统）
  - `order_status`：订单状态（pending-待开始，processing-进行中，pending_confirm-待确认，completed-已完成）
  - `order_date`：订单日期
  - `keyword`：关键词
  - `quantity`：数量
  - `budget_amount`：预算金额
  - `actual_amount`：实际金额
  - `transaction_order_no`：成交订单号（返回信息）
  - `transaction_price`：实际成交价格（返回信息）
  - `is_resubmit`：是否重新提交（0-否，1-是）
  - `original_detail_id`：原订单ID（重新提交时关联）

### 3.4 订单查询模块

#### 3.4.1 订单查询视图
- 待开始订单：提前填写，但还未到日期开始（order_status = 'pending'）
- 进行中订单：当天订单，订单任务已经分配（order_status = 'processing'）
- 待确认订单：还未上评的订单（order_status = 'pending_confirm'）
- 已完成订单：订单已经评价完毕，订单终结（order_status = 'completed'，包含成交订单号、实际成交价格等返回信息）
- 批次订单：查看批次订单汇总
- 明细订单：查看明细订单详情

## 四、关键业务逻辑

1. **店铺管理**：
   - 添加店铺：输入店铺韩文名称+自定义备注名称
   - 修改店铺：仅支持修改自定义备注名称
   - 删除店铺：客户端删除，但如果已有订单，后台仍保留

2. **产品管理**：
   - 自动添加：输入产品链接，爬取产品信息自动添加
   - 手动添加：输入属性名称、产品主图、售价等
   - 修改产品：可以修改除产品ID外的所有信息

3. **订单创建**：
   - 创建批次：某一产品或某几个产品，未来某段时间内的点击/加购数量总数
   - 填写订单：支持多种填写方式，每个产品的关键词和测评订单类型可能不一样
   - 导入订单：可以选择任意已提交的订单重新提交

4. **订单查询**：
   - 按状态分类查询
   - 查看订单详情和返回信息

## 五、权限说明

### 客户权限
- 查看和管理自己的店铺数据（通过 tenant_id 过滤）
- 查看和管理自己的产品数据（通过店铺关联）
- 创建和填写订单
- 查看自己的订单数据

## 六、与后台管理系统的数据共享

### 6.1 数据关联

- **租户关联**：通过 `tenant_id` 关联后台系统的 `bt_tenant` 表
  - `bt_store.tenant_id` → `bt_tenant.id`
  - `bt_order_batch.tenant_id` → `bt_tenant.id`
- **平台关联**：通过 `platform_id` 关联后台系统的 `bt_platform` 表
- **订单类型关联**：通过 `order_type_id` 关联后台系统的 `bt_order_type` 表

### 6.2 数据同步

- 租户数据：由后台系统创建，客户端通过 tenant_id 关联
- 平台数据：由后台系统管理，客户端通过 platform_id 关联
- 订单类型数据：由后台系统管理，客户端通过 order_type_id 关联
- 订单数据：由客户端创建，后台系统通过订单ID关联查看和管理

详见 `system_relationship.md` 文档。
