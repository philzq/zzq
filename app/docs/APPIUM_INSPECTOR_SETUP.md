# Appium Inspector 连接Nova9手机配置指南

本文档详细介绍如何使用Appium Inspector连接到华为Nova9手机。

## 一、准备工作

### 1. 安装Appium服务器和Inspector

#### 方式1：使用Appium Desktop（推荐，包含Inspector）

1. 下载Appium Desktop：
   - 访问：https://github.com/appium/appium-desktop/releases
   - 下载最新版本的Windows安装包（.exe文件）
   - 安装并启动

2. Appium Desktop包含：
   - Appium服务器
   - Appium Inspector（集成在界面中）

#### 方式2：命令行安装

```bash
# 安装Appium服务器
npm install -g appium

# 安装UiAutomator2驱动（Android必需）
appium driver install uiautomator2

# 安装Appium Inspector（独立版本）
npm install -g appium-inspector
```

### 2. 准备Nova9手机

#### 步骤1：开启开发者选项

1. 打开手机的"设置"
2. 找到"关于手机"（通常在系统或系统与更新中）
3. 连续点击"版本号"7次
4. 系统会提示"您已成为开发者"

#### 步骤2：开启USB调试

1. 返回设置主界面
2. 找到"系统和更新" -> "开发人员选项"
3. 开启以下选项：
   - ✅ **USB调试**（最重要）
   - ✅ USB调试（安全设置）（如果有）
   - ✅ 保持唤醒状态（可选，方便调试）
   - ✅ 允许通过USB安装应用（可选）

#### 步骤3：连接手机到电脑

1. 使用USB数据线连接Nova9到电脑
2. 手机上会弹出"允许USB调试"提示，勾选"始终允许这台计算机进行调试"，点击"确定"
3. 选择USB连接模式为"文件传输"或"仅充电"（都可以用于调试）

#### 步骤4：验证连接

打开命令行（CMD或PowerShell），运行：

```bash
adb devices
```

应该看到类似输出：
```
List of devices attached
ABC123XYZ456    device
```

如果显示`unauthorized`，需要在手机上再次确认USB调试授权。

### 3. 获取设备信息

运行以下命令获取Nova9的设备信息：

```bash
# 获取设备UDID（设备序列号）
adb devices

# 获取Android版本
adb shell getprop ro.build.version.release

# 获取设备型号
adb shell getprop ro.product.model

# 获取设备品牌
adb shell getprop ro.product.brand

# 获取设备制造商
adb shell getprop ro.product.manufacturer
```

**示例输出（Nova9）：**
- UDID: `ABC123XYZ456`（每次连接可能不同）
- Android版本: `11` 或 `12`
- 设备型号: `nova 9` 或 `ELS-AN00`
- 品牌: `HUAWEI`
- 制造商: `HUAWEI`

### 4. 确认抖音APP已安装

```bash
# 查看已安装的抖音APP
adb shell pm list packages | grep aweme
```

应该输出：`package:com.ss.android.ugc.aweme`

如果没有安装，请先安装抖音APP。

## 二、配置Appium Inspector

### 方式1：使用Appium Desktop的Inspector

#### 步骤1：启动Appium服务器

1. 打开Appium Desktop
2. 默认服务器地址：`127.0.0.1`，端口：`4723`
3. 点击"Start Server"按钮启动服务器
4. 看到"Appium REST http interface listener started on 0.0.0.0:4723"表示启动成功

#### 步骤2：配置连接参数

1. 点击右上角的"Start Inspector Session"按钮（放大镜图标）
2. 在弹出的"Edit Configurations"窗口中，选择"JSON Representation"标签
3. 输入以下JSON配置：

```json
{
  "platformName": "Android",
  "deviceName": "Nova9",
  "platformVersion": "11",
  "udid": "你的设备UDID",
  "appPackage": "com.ss.android.ugc.aweme",
  "appActivity": ".main.MainActivity",
  "automationName": "UiAutomator2",
  "noReset": true,
  "newCommandTimeout": 300
}
```

**重要参数说明：**

- `platformName`: 固定为 `"Android"`
- `deviceName`: 可以自定义，如 `"Nova9"` 或 `"华为Nova9"`
- `platformVersion`: 你的Android版本（通过 `adb shell getprop ro.build.version.release` 获取）
- `udid`: 设备的UDID（通过 `adb devices` 获取，第一列就是）
- `appPackage`: 抖音APP包名 `"com.ss.android.ugc.aweme"`
- `appActivity`: 抖音主Activity `".main.MainActivity"`
- `automationName`: 固定为 `"UiAutomator2"`
- `noReset`: `true` 表示不重置APP数据
- `newCommandTimeout`: 命令超时时间（秒）

#### 步骤3：启动会话

1. 点击"Start Session"按钮
2. 如果配置正确，会看到：
   - 手机上抖音APP自动启动
   - Appium Inspector窗口显示手机屏幕
   - 可以查看APP的元素结构

### 方式2：使用独立版Appium Inspector

#### 步骤1：启动Appium服务器

```bash
appium
```

保持命令行窗口打开。

#### 步骤2：启动Appium Inspector

```bash
appium-inspector
```

或者在Windows上直接运行Appium Inspector程序。

#### 步骤3：配置连接

**Appium 2.x版本配置（推荐）：**

1. 在Appium Inspector中，填写以下信息：
   - **Remote Host**: `127.0.0.1`
   - **Remote Port**: `4723`
   - **Remote Path**: `/` （Appium 2.x使用根路径，不是`/wd/hub`）

2. 在"Desired Capabilities"区域，使用JSON格式输入：

```json
{
  "platformName": "Android",
  "appium:udid": "你的设备UDID",
  "appium:automationName": "UiAutomator2",
  "appium:appPackage": "com.ss.android.ugc.aweme",
  "appium:appActivity": ".main.MainActivity",
  "appium:adbExecTimeout": 120000
}
```

**或者使用表格方式添加参数（注意使用appium:前缀）：**

| Capability | Value | 说明 |
|-----------|-------|------|
| platformName | Android | 平台名称（**不需要appium:前缀**） |
| appium:udid | EMH0221A22001952 | 设备UDID（从adb devices获取） |
| appium:automationName | UiAutomator2 | 自动化引擎 |
| appium:appPackage | com.ss.android.ugc.aweme | APP包名 |
| appium:appActivity | .main.MainActivity | 主Activity |
| appium:adbExecTimeout | 120000 | ADB命令超时（毫秒） |
| appium:noReset | true | （可选）不重置APP |
| appium:newCommandTimeout | 300 | （可选）命令超时时间 |

3. 点击"Start Session"按钮

**注意：**
- Appium 2.x版本中，Remote Path必须是 `/`（根路径）
- Appium 1.x版本中，Remote Path是 `/wd/hub`
- 如果使用Appium Desktop，通常会自动处理路径问题

## 三、常见问题解决

### Q1: 连接失败，提示"Unable to create a new remote session"

**可能原因和解决方法：**

1. **设备未连接**
   - 运行 `adb devices` 确认设备已连接
   - 重新插拔USB线
   - 在手机上重新授权USB调试

2. **UDID错误**
   - 重新运行 `adb devices` 获取正确的UDID
   - 确保UDID和配置中的一致

3. **Appium服务器未启动**
   - 确认Appium服务器正在运行
   - 检查端口4723是否被占用

4. **抖音APP未安装**
   - 运行 `adb shell pm list packages | grep aweme` 确认
   - 如果没有，先安装抖音APP

### Q2: 提示"SessionNotCreatedException: A new session could not be created"

**解决方法：**

1. 检查Android版本号是否正确
2. 尝试不设置`udid`参数，让Appium自动选择设备
3. 检查抖音APP的Activity是否正确：
   ```bash
   adb shell dumpsys package com.ss.android.ugc.aweme | grep -A 5 "android.intent.action.MAIN"
   ```

### Q3: 连接成功但Inspector显示空白

**解决方法：**

1. 等待几秒钟，APP可能需要时间加载
2. 在手机上手动打开抖音APP，然后重新连接
3. 检查`appActivity`是否正确

### Q4: 华为手机特殊问题

**华为手机可能需要的额外设置：**

1. **允许通过USB安装应用**（在开发者选项中）
2. **关闭"仅充电时允许ADB调试"限制**（如果有这个选项）
3. **关闭"监控ADB安装应用"**（如果有）

### Q5: 如何获取正确的appActivity

如果默认的`.main.MainActivity`不起作用，可以尝试：

```bash
# 方法1：查看当前运行的Activity
adb shell dumpsys window | grep mCurrentFocus

# 方法2：启动抖音APP后查看
adb shell dumpsys activity activities | grep "mResumedActivity"

# 方法3：查看APP的主Activity
adb shell dumpsys package com.ss.android.ugc.aweme | grep -A 10 "android.intent.action.MAIN"
```

## 四、使用Inspector查看元素

连接成功后，你可以：

1. **查看元素树**：左侧显示APP的UI元素层次结构
2. **选择元素**：点击屏幕上的元素，会在元素树中高亮
3. **查看元素属性**：右侧显示选中元素的详细信息（resource-id、text、class等）
4. **录制操作**：可以录制点击、滑动等操作
5. **获取元素定位符**：复制元素的XPath或resource-id用于代码中

## 五、快速配置模板

### Nova9通用配置模板

**推荐配置（最小必需参数）：**

```json
{
  "platformName": "Android",
  "appium:udid": "从adb devices获取（如：EMH0221A22001952）",
  "appium:automationName": "UiAutomator2",
  "appium:appPackage": "com.ss.android.ugc.aweme",
  "appium:appActivity": ".main.MainActivity",
  "appium:adbExecTimeout": 120000
}
```

**完整配置（包含可选参数）：**

```json
{
  "platformName": "Android",
  "appium:udid": "从adb devices获取",
  "appium:automationName": "UiAutomator2",
  "appium:appPackage": "com.ss.android.ugc.aweme",
  "appium:appActivity": ".main.MainActivity",
  "appium:adbExecTimeout": 120000,
  "appium:noReset": true,
  "appium:newCommandTimeout": 300
}
```

**Appium Inspector连接设置（Appium 2.x）：**
- Remote Host: `127.0.0.1`
- Remote Port: `4723`
- Remote Path: `/` （重要：Appium 2.x使用根路径，不是`/wd/hub`）

**注意：**
- `platformName` **不需要** `appium:` 前缀
- 其他参数都需要 `appium:` 前缀
- `udid` 参数是必需的，通过 `adb devices` 获取

### 如果不想自动启动APP（仅连接已运行的APP）

```json
{
  "platformName": "Android",
  "deviceName": "Nova9",
  "platformVersion": "11",
  "udid": "从adb devices获取",
  "automationName": "UiAutomator2",
  "noReset": true,
  "newCommandTimeout": 300
}
```

注意：不设置`appPackage`和`appActivity`，但需要手动在手机上打开抖音APP。

## 六、下一步

连接成功后，你可以：

1. 使用Inspector查看抖音APP的元素结构
2. 找到视频标题、作者、点赞数等元素的resource-id或XPath
3. 将这些信息用于更新代码中的元素定位器（`DouyinAppiumCrawler.java`）

## 七、参考资源

- Appium Desktop下载：https://github.com/appium/appium-desktop/releases
- Appium官方文档：http://appium.io/docs/en/about-appium/intro/
- Android ADB文档：https://developer.android.com/studio/command-line/adb

