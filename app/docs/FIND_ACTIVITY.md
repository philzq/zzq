# 如何查找APP的Activity名称

当遇到Activity名称错误时，可以使用以下方法查找正确的Activity名称。

## 方法1：查看当前运行的Activity（推荐）

1. **手动打开京东APP**
2. **运行以下命令**：
```bash
adb shell dumpsys window windows | grep -E 'mCurrentFocus'
```

**示例输出：**
```
mCurrentFocus=Window{... u0 com.jingdong.app.mall/.MainActivity}
```

从输出中可以提取：
- 包名：`com.jingdong.app.mall`
- Activity：`.MainActivity`（注意前面的点）

## 方法2：查看APP的主Activity

```bash
adb shell dumpsys package com.jingdong.app.mall | grep -A 10 "android.intent.action.MAIN"
```

查找包含 `android.intent.action.MAIN` 的部分，找到主Activity。

## 方法3：启动APP后查看

```bash
# 先启动APP
adb shell am start -n com.jingdong.app.mall/.MainActivity

# 然后查看当前Activity
adb shell dumpsys activity activities | grep "mResumedActivity"
```

## 方法4：使用aapt工具（需要Android SDK Build Tools）

```bash
aapt dump badging /path/to/jingdong.apk | grep "launchable-activity"
```

## 常见Activity格式

1. **相对路径（推荐）**：`.MainActivity`
   - 以点开头，Appium会自动拼接包名

2. **完整路径**：`com.jingdong.app.mall.MainActivity`
   - 完整包名+类名

3. **不设置Activity**：
   - 如果Activity为null或空字符串，Appium会尝试自动检测主Activity

## 京东APP常见的Activity名称

- `.MainActivity` - 主Activity（最常用）
- `.activity.MainFrameActivity` - 主框架Activity
- `.activity.SplashActivity` - 启动页Activity

## 在代码中测试

如果Activity名称不确定，可以尝试：

1. **不设置Activity**（让Appium自动检测）：
```java
// 在AppiumConfig中设置appActivity为null或空字符串
config.setAppActivity(null);
```

2. **尝试不同的Activity名称**：
```java
config.setAppActivity(".MainActivity");  // 尝试1
// 如果失败，尝试：
config.setAppActivity(".activity.MainFrameActivity");  // 尝试2
```

