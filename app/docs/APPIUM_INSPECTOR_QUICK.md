# Appium Inspector 快速配置指南（Nova9）

## 关键配置点

### Appium 2.x版本配置

**连接设置：**
- Remote Host: `127.0.0.1`
- Remote Port: `4723`
- **Remote Path: `/`** ⚠️ 重要：Appium 2.x使用根路径，**不是**`/wd/hub`

**Desired Capabilities（JSON格式）：**

**推荐配置（Appium 2.x）：**
```json
{
  "platformName": "Android",
  "appium:udid": "你的UDID",
  "appium:automationName": "UiAutomator2",
  "appium:appPackage": "com.ss.android.ugc.aweme",
  "appium:appActivity": ".main.MainActivity",
  "appium:adbExecTimeout": 120000
}
```

**完整配置（可选参数）：**
```json
{
  "platformName": "Android",
  "appium:udid": "你的UDID",
  "appium:automationName": "UiAutomator2",
  "appium:appPackage": "com.ss.android.ugc.aweme",
  "appium:appActivity": ".main.MainActivity",
  "appium:adbExecTimeout": 120000,
  "appium:noReset": true,
  "appium:newCommandTimeout": 300
}
```

**参数说明：**
- `platformName`: 固定为 "Android"（不需要appium:前缀）
- `appium:udid`: 设备UDID（从 `adb devices` 获取）
- `appium:automationName`: 固定为 "UiAutomator2"
- `appium:appPackage`: 抖音APP包名
- `appium:appActivity`: 抖音主Activity
- `appium:adbExecTimeout`: ADB命令超时时间（毫秒），建议120000

### 获取UDID

```bash
adb devices
```

第一列就是UDID，例如：`ABC123XYZ456`

### 获取Android版本

```bash
adb shell getprop ro.build.version.release
```

## 常见错误

❌ **错误配置：**
- Remote Path: `/wd/hub` （这是Appium 1.x的路径）

✅ **正确配置：**
- Remote Path: `/` （Appium 2.x使用根路径）

## 完整步骤

1. 启动Appium服务器：`appium`
2. 打开Appium Inspector
3. 设置连接：
   - Host: `127.0.0.1`
   - Port: `4723`
   - Path: `/`
4. 粘贴上面的JSON配置（修改udid和platformVersion）
5. 点击"Start Session"

