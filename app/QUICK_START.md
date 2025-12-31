# 快速开始指南

## 一、准备工作

### 1. 环境要求
- Java 11+
- Maven 3.6+
- 一台Android手机
- 电脑和手机在同一WiFi网络

### 2. 安装抓包工具（推荐Charles）

1. 下载Charles：https://www.charlesproxy.com/download/
2. 安装并启动Charles

## 二、配置手机代理

### 1. 获取电脑IP地址

**Windows**:
```bash
ipconfig
# 查看"IPv4 地址"，例如：192.168.1.100
```

**Mac/Linux**:
```bash
ifconfig
# 查找en0或wlan0的inet地址
```

### 2. 手机设置代理

1. 打开手机 **设置 -> WLAN**
2. 长按当前连接的WiFi
3. 选择"修改网络"或"网络详情"
4. 高级选项 -> 代理 -> 手动
5. 填写：
   - 主机名：电脑IP地址（如：192.168.1.100）
   - 端口：8888（Charles默认端口）
6. 保存

### 3. 安装SSL证书（用于HTTPS抓包）

1. 手机浏览器访问：`chls.pro/ssl`
2. 下载证书并安装
3. **Android 7.0+需要额外步骤**：
   - 设置 -> 安全 -> 加密与凭据
   - 从存储设备安装
   - 选择下载的证书文件
   - 命名后确定

## 三、抓包获取API信息

### 1. 开始抓包

1. 打开Charles
2. 打开手机上的抖音APP
3. 在抖音APP中浏览视频、查看用户主页等操作
4. 在Charles中查看请求列表

### 2. 查找关键请求

在Charles中：
1. 筛选域名：`aweme.snssdk.com`
2. 找到用户视频列表请求（通常是 `/aweme/v1/aweme/post/`）
3. 点击查看详情

### 3. 记录关键信息

记录以下信息：

**请求URL示例**：
```
https://aweme.snssdk.com/aweme/v1/aweme/post/?sec_user_id=MS4wLjABAAAA...
```

**请求头（Headers）中需要记录**：
- `User-Agent`
- `X-Gorgon`
- `X-Argus`
- `X-Tt-Token`（如果有）

**请求参数（Query Parameters）中需要记录**：
- `sec_user_id`（用户ID）
- `device_id`（设备ID）
- `max_cursor`（分页参数）

## 四、配置Java程序

### 1. 修改AppConfig.java

编辑 `src/main/java/com/crawler/douyin/config/AppConfig.java`：

```java
// 填入从抓包中获取的device_id
private String deviceId = "你从抓包中获取的device_id";

// 可以调整其他参数，如设备型号、Android版本等
```

### 2. 修改AppHttpUtil.java（可选，如果签名参数为空）

编辑 `src/main/java/com/crawler/douyin/util/AppHttpUtil.java`：

找到以下代码并填入从抓包中获取的值：

```java
.addHeader("X-Gorgon", "从抓包中获取的X-Gorgon值")
.addHeader("X-Argus", "从抓包中获取的X-Argus值")
.addHeader("X-Tt-Token", "从抓包中获取的Token（如果有）")
```

**注意**：这些参数有时效性（通常几分钟到几小时），过期后需要重新抓包获取。

### 3. 修改Main.java

编辑 `src/main/java/com/crawler/douyin/Main.java`：

```java
// 替换为从抓包中获取的真实sec_user_id
String secUserId = "MS4wLjABAAAA...";  // 你的真实sec_user_id
```

## 五、运行程序

### 1. 编译项目

```bash
cd d:\IdeaProjects\zzq\app
mvn clean compile
```

### 2. 运行程序

```bash
mvn exec:java -Dexec.mainClass="com.crawler.douyin.Main"
```

或者打包后运行：

```bash
mvn clean package
java -jar target/douyin-crawler-1.0.0.jar
```

### 3. 查看输出

程序运行后会在控制台输出爬取到的视频信息：

```
2024-01-01 12:00:00 [main] INFO  com.crawler.douyin.Main - 抖音爬虫Demo启动...
2024-01-01 12:00:00 [main] INFO  com.crawler.douyin.DouyinCrawler - 开始爬取用户视频: secUserId=MS4wLjABAAAA...
2024-01-01 12:00:01 [main] INFO  com.crawler.douyin.Main - 成功爬取 5 个视频
2024-01-01 12:00:01 [main] INFO  com.crawler.douyin.Main - 视频标题: 示例视频标题 1
2024-01-01 12:00:01 [main] INFO  com.crawler.douyin.Main - 视频链接: https://example.com/video_1.mp4
...
```

## 六、常见问题排查

### 问题1：连接失败

**症状**：`java.net.ConnectException`

**解决**：
- 检查电脑防火墙是否阻止了连接
- 确认手机和电脑在同一WiFi网络
- 检查代理设置是否正确

### 问题2：401/403错误

**症状**：请求返回401或403状态码

**解决**：
- 签名参数（X-Gorgon、X-Argus）可能已过期，重新抓包获取
- 检查所有请求头是否正确填写
- Token可能已失效，需要重新获取

### 问题3：返回空数据

**症状**：程序运行成功但返回空列表

**解决**：
- 检查sec_user_id是否正确
- 查看日志中的详细错误信息
- 确认API接口地址是否正确

### 问题4：抓包看不到HTTPS请求

**症状**：Charles中看不到抖音的请求

**解决**：
- 确认SSL证书已正确安装
- Android 7.0+需要手动信任证书（见步骤二.3）
- 检查代理设置是否正确
- 尝试重启Charles和手机APP

## 七、下一步

1. **深入了解**：查看 `docs/CAPTURE_GUIDE.md` 了解详细抓包方法
2. **扩展功能**：查看 `README.md` 了解如何扩展功能
3. **实现签名算法**：如果需要长期使用，可以逆向分析APP实现签名算法

## 八、重要提示

⚠️ **请遵守法律法规**：
- 本项目仅用于学习研究
- 不要用于商业用途
- 控制请求频率，避免对服务器造成压力
- 遵守抖音的服务条款

