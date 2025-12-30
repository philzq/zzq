# 测评系统数据库设计文档

## 一、系统概述

**测评系统**是客户使用的系统，用于管理店铺、产品、创建订单、填写订单、查询订单等核心业务功能。

### 1.1 系统定位
- **使用人员**：客户（企业）
- **主要职责**：店铺管理、产品管理、订单创建和填写、订单查询
- **数据权限**：只能查看和管理自己的数据（通过 tenant_id 关联后台系统的租户表）

### 1.2 核心功能模块
根据思维导图，系统包含以下核心模块：
1. **平台管理**：选择平台（coupang、naver等），方便后续拓展新平台
2. **订单类型管理**：订单类型维护，支持测评、点击、加购等操作场景
3. **店铺管理**：添加、修改、删除店铺
4. **产品管理**：自动添加、手动添加、修改产品
5. **订单创建和填写**：创建批次订单、填写明细订单（测评、点击、加购）
6. **订单查询**：待开始、进行中、待确认、已完成订单查询
7. **财务账单**：提交订单时支付佣金，查看账单详情

## 二、数据库设计原则

1. **基于 eladmin 框架**：遵循 eladmin 框架的数据库设计规范
2. **统一命名规范**：使用下划线命名（snake_case）
3. **基础字段统一**：所有表包含创建时间、更新时间、创建人、更新人等基础字段
4. **物理删除**：不支持逻辑删除，使用物理删除
5. **数据冗余**：订单表中冗余关键字段，保证历史数据的准确性
6. **索引优化**：为关键查询字段建立索引，提升查询性能

## 三、表结构设计

### 3.1 平台管理模块

#### 3.1.1 平台表 (bt_platform)
- **业务说明**：存储平台信息（coupang、naver等），对应思维导图中的"选择平台"
- **客户权限**：查看平台列表
- **字段说明**：
  - `platform_code`：平台编码（唯一，如coupang、naver）
  - `platform_name`：平台名称
  - `status`：状态（0-禁用，1-启用）
  - `sort`：排序
- **索引**：
  - `uk_platform_code`：平台编码唯一索引
  - `idx_status`：状态索引
- **业务逻辑**：
  - 调整为平台管理，方便后续拓展用户端新平台
  - 现在只拉出来一个平台即可

### 3.2 订单类型管理模块

#### 3.2.1 订单类型表 (bt_order_type)
- **业务说明**：存储订单类型信息，对应思维导图中的"费用等基础参数维护-订单类型维护"
- **客户权限**：查看订单类型列表
- **字段说明**：
  - `operation_scene`：操作场景（0-测评，1-点击，2-加购）
  - `first_level_type_name`：一级类型名称
  - `second_level_type_name`：二级类型名称
  - `commission_type`：佣金计算方式（percentage-按比例，fixed-固定金额）
  - `commission_rate`：佣金率（百分比，如10.5表示10.5%）
  - `commission_amount`：固定佣金金额（当commission_type为fixed时使用）
  - `currency`：币种（默认KRW）
  - `sort`：排序
  - `status`：状态（0-禁用，1-启用）
- **索引**：
  - `idx_operation_scene`：操作场景索引
  - `idx_status`：状态索引
- **佣金计算逻辑**：
  - 按比例计算：佣金 = 订单金额 × commission_rate / 100
  - 固定金额：佣金 = commission_amount

### 3.3 店铺管理模块

#### 3.3.1 店铺表 (bt_store)
- **业务说明**：存储客户自己的店铺信息，对应思维导图中的"店铺管理"
- **客户权限**：添加、修改自定义备注名称、删除店铺
- **字段说明**：
  - `tenant_id`：租户ID（关联后台系统的bt_tenant表）
  - `platform_code`：平台编码（关联本系统的bt_platform表的platform_code）
  - `store_name_kr`：店铺韩文名称
  - `store_name_custom`：店铺自定义备注名称
  - `status`：状态（0-禁用，1-启用）
- **索引**：
  - `idx_tenant_id`：租户ID索引
  - `idx_platform_code`：平台编码索引
  - `idx_status`：状态索引
- **业务逻辑**：
  - **添加店铺**：输入店铺韩文名称+自定义备注名称
  - **修改店铺**：仅支持修改自定义备注名称。修改名称后，该功能只能客户管理员账号使用
  - **删除店铺**：
    - 在客户端删除该店铺
    - 在管理员账号显示中，如果该店铺已经下过订单，那该店铺依然存在
    - 如果没有产生过订单，那就删除不记录

### 3.4 产品管理模块

#### 3.4.1 产品表 (bt_product)
- **业务说明**：存储客户自己的产品信息，对应思维导图中的"产品管理"
- **客户权限**：添加、修改、删除产品
- **字段说明**：
  - `tenant_id`：租户ID（关联后台系统的bt_tenant表）
  - `store_id`：店铺ID
  - `platform_code`：平台编码（关联本系统的bt_platform表的platform_code）
  - `platform_product_id`：平台产品ID（唯一，不可修改）
  - `product_title`：产品标题
  - `product_link`：产品链接
  - `selling_price`：售价
  - `attribute_name`：属性名称（以","隔开不同的属性名称）
  - `main_image_url`：产品主图URL
  - `status`：状态（0-禁用，1-启用）
- **索引**：
  - `uk_platform_product_id`：平台产品ID唯一索引
  - `idx_tenant_id`：租户ID索引
  - `idx_store_id`：店铺ID索引
  - `idx_platform_code`：平台编码索引
  - `idx_status`：状态索引
- **业务逻辑**：
  - **自动添加**：
    - 输入产品链接，爬取产品链接中的关键信息，自动添加
    - 店铺选择：下拉显示店铺自定义名称，括号中显示韩文名称
    - 产品标题、产品ID、属性名称、产品主图、售价等信息自动填充
  - **手动添加**：手动输入所有产品信息
  - **修改产品**：
    - 展示所有的产品信息，可以根据店铺进行筛选，或者输入产品ID查询
    - 除了产品ID以外，其余的信息都可以手动修改
    - 已经下过单的产品信息不随着产品修改而改变

### 3.5 订单创建和填写模块

#### 3.5.1 批次订单表 (bt_order_batch)
- **业务说明**：存储客户创建的批次订单，对应思维导图中的"创建订单(批次)"
- **客户权限**：创建批次订单、查看自己的批次订单、取消未开始的订单
- **字段说明**：
  - `tenant_id`：租户ID（关联后台系统的bt_tenant表）
  - `batch_order_status`：批次订单状态（0-待提交，1-待支付佣金，2-已取消，3-已支付佣金）
  - `total_quantity`：总数量
  - `total_amount`：总金额（预算）
  - `actual_amount`：实际金额
  - `currency`：币种
  - `batch_text_content`：批次文本内容（记录提交的完整数据）
- **索引**：
  - `idx_tenant_id`：租户ID索引
  - `idx_batch_order_status`：批次订单状态索引
- **业务逻辑**：
  - **创建批次**：某一产品或某几个产品，未来某段时间内的点击/加购数量总数
  - **待支付佣金**：提交订单（汇总）时，需要依据订单类型支付佣金，先显示预算的金额
  - **已取消**：还没开始的可以取消

#### 3.5.2 订单表 (bt_order)
- **业务说明**：存储客户填写的订单，对应思维导图中的"填写订单"
- **客户权限**：填写订单、导入订单、重新提交订单、查看自己的订单
- **字段说明**：
  - `tenant_id`：租户ID（关联后台系统的bt_tenant表）
  - `order_batch_id`：批次订单ID
  - `product_id`：产品ID
  - `platform_product_id`：平台产品ID（冗余字段，冗余bt_product表）
  - `store_id`：店铺ID
  - `store_name_kr`：店铺韩文名称（冗余字段，冗余bt_store表）
  - `store_name_custom`：店铺自定义备注名称（冗余字段，冗余bt_store表）
  - `platform_code`：平台编码（关联本系统的bt_platform表的platform_code）
  - `platform_name`：平台名称（冗余字段，冗余bt_platform表）
  - `order_type_id`：订单类型ID（关联本系统的bt_order_type表）
  - `operation_scene`：操作场景（冗余字段，冗余bt_order_type表）：0-测评，1-点击，2-加购
  - `first_level_type_name`：一级类型名称（冗余字段，冗余bt_order_type表）
  - `second_level_type_name`：二级类型名称（冗余字段，冗余bt_order_type表）
  - `product_title`：产品标题（冗余字段，冗余bt_product表）
  - `product_link`：产品链接（冗余字段，冗余bt_product表）
  - `attribute_name`：属性名称（冗余字段，冗余bt_product表）
  - `main_image_url`：产品主图URL（冗余字段，冗余bt_product表）
  - `selling_price`：售价（冗余字段，冗余bt_product表）
  - `order_status`：订单状态（0-待开始，1-待分配，2-进行中，3-待确认，4-已完成）
  - `platform_order_no`：平台订单号
  - `express_company`：快递公司
  - `tracking_number`：物流单号
  - `actual_order_price`：实际下单价格
  - `order_account`：下单账号
  - `device_id`：设备ID
  - `keyword`：关键词
  - `click_count`：点击数量
  - `cart_count`：加购数量
  - `execution_date`：执行日期
  - `review_text_content`：评论文字内容
  - `review_image_content`：评论图片内容
  - `execution_result`：执行结果（0-失败，1-成功）
  - `execution_result_desc`：执行结果描述
  - `auto_execution_count`：自动化执行次数（用于重试）
- **索引**：
  - `idx_tenant_id`：租户ID索引
  - `idx_order_batch_id`：批次订单ID索引
  - `idx_product_id`：产品ID索引
  - `idx_store_id`：店铺ID索引
  - `idx_platform_code`：平台编码索引
  - `idx_order_status`：订单状态索引
  - `idx_order_type_id`：订单类型ID索引
  - `idx_platform_order_no`：平台订单号索引
  - `idx_device_id`：设备ID索引
  - `idx_execution_date`：执行日期索引
  - `idx_operation_scene`：操作场景索引
  - `idx_execution_result`：执行结果索引
  - `idx_order_account`：下单账号索引
- **业务逻辑**：
  - **填写订单**：支持多种填写方式
    - 某一天，单一产品的订单信息，提交产品的关键词和测评订单类型可能不一样
    - 某一天，不同产品的订单信息，提交产品的关键词和测评订单类型可能不一样
    - 某单一产品某几天的订单信息，提交产品的关键词和测评订单类型可能不一样
    - 不同产品某几天的订单信息，提交产品的关键词和测评订单类型可能不一样
  - **导入订单**：可以选择任意已提交的订单重新提交
  - **点击/加购**：
    - 某一产品，未来某短时间内的点击数量总数
    - 某几个产品，未来某短时间内的点击数量总数
    - 某一产品，未来某段时间内的加购数量总数
    - 某几个产品，未来某段时间内的加购数量总数
  - **冗余字段**：用于保存订单创建时的快照数据，即使关联表的数据发生变化，订单表中的冗余字段保持不变，保证历史数据的准确性

### 3.6 订单查询模块

#### 3.6.1 订单查询视图
- **待开始订单**：提前填写，但还未到日期开始（order_status = 0）
- **待分配订单**：已到执行日期，等待分配执行账号（order_status = 1）
- **进行中订单**：订单任务已经分配，正在执行中（order_status = 2）
- **待确认订单**：还未上评的订单（order_status = 3）
- **已完成订单**：订单已经评价完毕，订单终结（order_status = 4）
  - 包含成交订单号、实际成交价格等返回信息
- **批次订单**：查看批次订单汇总
- **明细订单**：查看订单详情

## 四、关键业务逻辑

### 4.1 店铺管理
- **添加店铺**：输入店铺韩文名称+自定义备注名称
- **修改店铺**：仅支持修改自定义备注名称
- **删除店铺**：使用物理删除，但如果店铺已有订单，在管理员端仍显示

### 4.2 产品管理
- **自动添加**：输入产品链接，爬取产品信息自动添加
- **手动添加**：输入属性名称、产品主图、售价等
- **修改产品**：可以修改除产品ID外的所有信息
- **已下单产品**：已下单的产品信息不随产品修改而改变

### 4.3 订单创建
- **创建批次**：某一产品或某几个产品，未来某段时间内的点击/加购数量总数
- **填写订单**：支持多种填写方式，每个产品的关键词和测评订单类型可能不一样
- **导入订单**：可以选择任意已提交的订单重新提交

### 4.4 订单查询
- **按状态分类查询**：待开始、进行中、待确认、已完成
- **查看订单详情**：包含成交订单号、实际成交价格等返回信息

### 4.5 财务账单
- **提交订单时**：需要依据订单类型支付佣金，先显示预算的金额（根据计算公式得出）
- **实际支付后**：可以显示实际付款金额
- **费用详情**：每一个明细订单也有对应的费用详情

## 五、权限说明

### 5.1 客户权限
- 查看和管理自己的店铺数据（通过 tenant_id 过滤）
- 查看和管理自己的产品数据（通过店铺关联）
- 创建和填写订单
- 查看自己的订单数据
- 查看账单信息

### 5.2 客户管理员账号
- 具备创建子账户
- 具备给予子账户各项权限
- 修改店铺名称（仅客户管理员账号使用）

## 六、与后台管理系统的数据共享

### 6.1 数据关联

- **租户关联**：通过 `tenant_id` 关联后台系统的 `bt_tenant` 表
  - `bt_store.tenant_id` → `bt_tenant.id`
  - `bt_order_batch.tenant_id` → `bt_tenant.id`
  - `bt_product.tenant_id` → `bt_tenant.id`
  - `bt_order.tenant_id` → `bt_tenant.id`
- **平台关联**：通过 `platform_code` 关联本系统的 `bt_platform` 表
- **订单类型关联**：通过 `order_type_id` 关联本系统的 `bt_order_type` 表

### 6.2 数据同步

- **租户数据**：由后台系统创建，客户端通过 tenant_id 关联
- **平台数据**：由客户端系统管理
- **订单类型数据**：由客户端系统管理
- **订单数据**：由客户端创建，后台系统通过订单ID关联查看和管理

详见 `system_relationship.md` 文档。

## 七、数据冗余说明

### 7.1 冗余字段设计
订单表中冗余了以下关键字段，用于保存订单创建时的快照数据：
- **店铺信息**：`store_name_kr`、`store_name_custom`
- **平台信息**：`platform_name`
- **订单类型信息**：`operation_scene`、`first_level_type_name`、`second_level_type_name`
- **产品信息**：`platform_product_id`、`product_title`、`product_link`、`attribute_name`、`main_image_url`、`selling_price`

### 7.2 冗余字段的作用
- **历史数据准确性**：即使关联表的数据发生变化，订单表中的冗余字段保持不变
- **查询性能**：减少关联查询，提升查询性能
- **数据追溯**：可以准确追溯订单创建时的原始数据

## 八、索引说明

所有关键查询字段都已建立索引，包括：
- 外键字段索引（tenant_id、order_batch_id、product_id、store_id等）
- 状态字段索引（status、order_status、batch_order_status等）
- 日期字段索引（execution_date等）
- 业务查询字段索引（platform_code、platform_order_no、device_id、operation_scene等）

索引设计遵循以下原则：
- 为经常用于WHERE条件的字段建立索引
- 为外键关联字段建立索引
- 为排序和分组字段建立索引
- 避免过度索引，平衡查询性能和写入性能

