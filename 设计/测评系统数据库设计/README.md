# 测评填单系统数据库设计

## 项目概述

本项目是基于 eladmin 框架的测评填单系统数据库设计，包含两个独立的子系统：
1. **测评后台管理系统**：管理员和员工使用
2. **测评系统**：客户使用

## 目录结构

```
.
├── admin_system/                    # 测评后台管理系统
│   ├── database_design_admin.md     # 数据库设计文档
│   └── evaluation-system-admin.sql  # 建表SQL脚本
├── client_system/                   # 测评系统
│   ├── database_design_client.md    # 数据库设计文档
│   └── evaluation-system-client.sql # 建表SQL脚本
├── system_relationship.md           # 系统间数据关系说明
└── README.md                        # 本文件
```

## 系统说明

### 测评后台管理系统（admin_system）

**使用人员**：管理员、员工

**主要功能**：
- 租户管理（创建、编辑、禁用/启用租户账号）
- 平台管理（coupang、naver等）
- 测评账号管理（创建、编辑、设备绑定）
- 财务账单管理（查看所有账单、支付状态管理）
- 帮助和公共管理

**数据库表**：
1. **租户表** (`bt_tenant`) - 存储租户（企业）基本信息
2. **平台表** (`bt_platform`) - 存储平台信息
3. **帮助文档表** (`bt_help_document`) - 存储帮助文档信息
4. **测评账号表** (`bt_review_account`) - 存储测评账号信息
   - 账号订单类型关联表 (`bt_review_account_order_type`)
   - 账号设备绑定表 (`bt_account_device`)
5. **账单表** (`bt_bill`) - 存储账单信息
   - 账单明细表 (`bt_bill_detail`)

**数据库表前缀**：`bt_`

### 测评系统（client_system）

**使用人员**：客户（企业）

**主要功能**：
- 店铺管理（添加、修改、删除）
- 产品管理（自动添加、手动添加、修改）
- 订单创建和填写（批次订单、明细订单）
- 订单查询（待开始、进行中、待确认、已完成）

**数据库表**：
1. **店铺表** (`bt_store`) - 存储客户的店铺信息
2. **产品表** (`bt_product`) - 存储客户的产品信息
3. **订单表** (`bt_order_batch`, `bt_order_detail`) - 存储客户的订单信息

**数据库表前缀**：`bt_`

## 快速开始

### 1. 执行SQL脚本

#### 方式一：分别执行（推荐）

```sql
-- 先执行后台管理系统的SQL（只包含5个核心表）
source admin_system/evaluation-system-admin.sql;

-- 再执行测评系统的SQL
source client_system/evaluation-system-client.sql;
```

#### 方式二：统一执行

```sql
-- 在MySQL中依次执行两个SQL文件
source admin_system/evaluation-system-admin.sql;
source client_system/evaluation-system-client.sql;
```

**注意**：
- 后台管理系统包含5个核心表（租户表、平台表、帮助文档表、测评账号表、账单表）
- 测评系统包含3个核心模块（店铺表、产品表、订单表）
- 两个系统通过 `tenant_id` 关联，客户通过租户ID关联后台系统的租户数据

### 2. 与 eladmin 框架集成

#### 2.1 租户关联

测评系统通过 `tenant_id` 关联后台系统的租户表：

- `bt_store.tenant_id` → `bt_tenant.id`
- `bt_order_batch.tenant_id` → `bt_tenant.id`

**注意**：客户通过租户ID关联后台系统的租户数据，不直接关联 `sys_user` 表。

#### 2.2 权限控制

- 使用 eladmin 的权限体系
- 客户通过 `tenant_id` 过滤数据，只能查看和管理自己的数据
- 两个系统的权限相互独立

#### 2.3 代码生成

1. 在 eladmin 的代码生成器中分别导入两个系统的表结构
2. 配置字段属性（是否显示、是否必填等）
3. 分别生成前后端代码

### 3. 数据同步配置

两个系统之间的数据需要同步，详见 `system_relationship.md` 文档。

**推荐方案**：使用消息队列（RabbitMQ、Kafka等）实现数据同步。

## 数据库设计特点

### 1. 系统分离

- 两个系统使用统一的表前缀（`bt_`）
- 数据相互独立，便于维护和扩展
- 通过数据同步机制保证数据一致性

### 2. 符合 eladmin 规范

- 统一的字段命名（下划线命名）
- 包含创建时间、更新时间、创建人、更新人等基础字段
- 后台管理系统不支持逻辑删除（使用物理删除）

### 3. 业务完整性

- 覆盖思维导图中的所有功能模块
- 支持订单的完整生命周期管理
- 支持财务账单管理

### 4. 扩展性

- 平台管理支持多平台扩展
- 订单类型和状态可配置
- 费用参数可灵活配置

## 关键业务逻辑

### 租户管理
- 管理员在后台系统创建租户账号
- 客户在测评系统通过 `tenant_id` 关联租户数据

### 订单管理
- 客户在测评系统创建订单
- 订单通过 `tenant_id` 关联租户
- 订单状态和支付状态使用字符串类型存储（pending、processing、pending_confirm、completed等）

### 数据关联
- 店铺和订单通过 `tenant_id` 关联后台系统的租户表
- 订单通过 `order_type_id` 关联后台系统的订单类型表
- 店铺和订单通过 `platform_id` 关联后台系统的平台表

## 数据同步

两个系统之间的数据需要同步，主要包括：

1. **租户数据**：后台系统创建租户后，测评系统通过 `tenant_id` 关联
2. **平台数据**：后台系统管理平台，测评系统通过 `platform_id` 关联
3. **订单类型数据**：后台系统管理订单类型，测评系统通过 `order_type_id` 关联
4. **订单数据**：客户在测评系统创建订单后，后台系统通过订单ID关联查看和管理

详细说明请参考 `system_relationship.md` 文档。

## 注意事项

### 1. 数据库部署

- **方案一**：两个系统使用同一个数据库，通过表前缀区分
- **方案二**：两个系统使用不同数据库，通过接口或消息队列同步数据

### 2. 外键约束

- SQL 脚本中未添加外键约束，建议在应用层进行数据完整性校验
- 如需添加外键，请根据实际需求修改 SQL 脚本

### 3. 字符集

- 使用 `utf8mb4` 字符集，支持 emoji 等特殊字符

### 4. 时区

- 时间字段使用 `datetime` 类型
- 建议在应用层统一处理时区问题

### 5. 数据初始化

- SQL 脚本中包含基础数据初始化（平台、订单类型、订单状态、支付状态）
- 可根据实际需求修改初始化数据

## 后续工作

1. 在 eladmin 框架中分别导入两个系统的表结构
2. 使用代码生成器分别生成前后端代码
3. 实现数据同步机制
4. 根据业务需求调整字段和索引
5. 添加必要的业务逻辑
6. 编写单元测试和集成测试

## 文件说明

- `admin_system/database_design_admin.md` - 后台管理系统数据库设计文档
- `admin_system/evaluation-system-admin.sql` - 后台管理系统建表SQL
- `client_system/database_design_client.md` - 测评系统数据库设计文档
- `client_system/evaluation-system-client.sql` - 测评系统建表SQL
- `system_relationship.md` - 系统间数据关系说明文档

## 联系方式

如有问题或建议，请联系开发团队。
