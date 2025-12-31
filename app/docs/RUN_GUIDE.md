# 抖音APP爬虫运行指南

## 运行原理说明

这个Java爬虫程序是运行在**电脑上**的，它的工作原理是：

1. **抓包分析**：通过抓包工具（Charles、mitmproxy等）捕获手机上抖音APP的网络请求
2. **模拟请求**：Java程序模拟这些网络请求，向抖音服务器发送请求获取数据
3. **解析数据**：解析服务器返回的JSON数据，提取视频信息

**注意**：程序本身不是直接在手机上运行的，而是通过模拟手机APP的网络请求来获取数据。

## 方法一：通过抓包获取API接口（推荐）

### 步骤1：安装抓包工具

#### 使用Charles（推荐，界面友好）

1. 下载安装Charles Proxy：https://www.charlesproxy.com/
2. 启动Charles

#### 或使用mitmproxy（免费开源）

1. 安装mitmproxy：
   ```bash
   pip install mitmproxy
   ```

### 步骤2：配置手机代理

1. **确保手机和电脑在同一WiFi网络**

2. **获取电脑IP地址**
   - Windows: `ipconfig` 查看IPv4地址
   - Mac/Linux: `ifconfig` 查看IP地址

3. **手机WiFi设置代理**
   - 打开手机WiFi设置
   - 找到当前连接的WiFi
   - 点击"高级选项"或"修改网络"
   - 设置代理：
     - 代理主机名：电脑IP地址（如：192.168.1.100）
     - 代理端口：8888（Charles默认）或8080（mitmproxy默认）

### 步骤3：安装SSL证书（用于HTTPS抓包）

#### Charles方式：
1. 手机浏览器访问：`chls.pro/ssl`
2. 下载证书并安装
3. **Android 7.0+**：需要在"设置 -> 安全 -> 加密与凭据 -> 从存储设备安装"中信任证书

#### mitmproxy方式：
1. 电脑启动mitmproxy
2. 手机浏览器访问：`mitm.it`
3. 下载Android证书并安装

### 步骤4：抓包分析

1. **打开抖音APP**，浏览视频、查看用户主页等
2. **在Charles中查看请求**：
   - 筛选域名：`aweme.snssdk.com` 或 `aweme-hl.snssdk.com`
   - 找到用户视频列表请求，查看：
     - 请求URL
     - 请求头（Headers）
     - 请求参数（Query Parameters）

3. **记录关键信息**：
   - API地址（如：`https://aweme.snssdk.com/aweme/v1/aweme/post/`）
   - 请求参数（sec_user_id、max_cursor等）
   - 请求头（X-Gorgon、X-Argus、User-Agent等）

### 步骤5：配置并运行Java程序

1. **修改AppConfig.java**，填入从抓包中获取的信息：
   ```java
   private String deviceId = "从抓包中获取的device_id";
   private String userAgent = "从抓包中获取的User-Agent";
   ```

2. **修改AppHttpUtil.java**，填入签名参数：
   ```java
   .addHeader("X-Gorgon", "从抓包中获取的值")
   .addHeader("X-Argus", "从抓包中获取的值")
   .addHeader("X-Tt-Token", "从抓包中获取的Token")
   ```

3. **修改Main.java**，填入真实的sec_user_id：
   ```java
   String secUserId = "从抓包中获取的sec_user_id";
   ```

4. **编译运行**：
   ```bash
   mvn clean compile
   mvn exec:java -Dexec.mainClass="com.crawler.douyin.Main"
   ```

## 方法二：使用Appium自动化（高级）

如果需要直接控制手机APP进行操作，可以使用Appium：

### 安装Appium

1. 安装Node.js
2. 安装Appium：
   ```bash
   npm install -g appium
   npm install -g appium-doctor
   ```

3. 安装Appium Java客户端依赖（在pom.xml中添加）：
   ```xml
   <dependency>
       <groupId>io.appium</groupId>
       <artifactId>java-client</artifactId>
       <version>8.5.0</version>
   </dependency>
   ```

### 使用Appium的注意事项

- Appium主要用于自动化测试，不是直接爬取API数据
- 可以通过Appium操作APP，同时配合抓包工具获取API请求
- 配置较复杂，需要开启USB调试等

## 方法三：使用Frida Hook（高级）

Frida可以Hook APP的函数，获取签名算法等：

1. 安装Frida
2. 在手机上安装frida-server
3. 编写Python脚本Hook签名函数
4. 在Java程序中调用签名函数

## 常见问题

### Q1: 签名参数（X-Gorgon、X-Argus）怎么获取？

**A**: 这些参数是动态生成的，有两个方法：

1. **短期使用**：直接从抓包中复制（但有时效性，几分钟到几小时）
2. **长期使用**：逆向分析抖音APP，找到签名算法，在Java中实现

### Q2: 程序运行后返回401或403错误？

**A**: 可能的原因：
- 签名参数过期或错误
- Token无效
- 缺少必要的请求头
- 设备指纹不匹配

解决方法：
- 重新抓包获取最新的参数
- 检查所有请求头是否都添加了
- 确保device_id等设备信息一致

### Q3: 如何获取用户的sec_user_id？

**A**: 
- 方法1：通过抖音网页版，查看用户主页URL，URL中包含sec_user_id
- 方法2：通过抓包，在用户主页相关的API请求中找到sec_user_id
- 方法3：如果知道用户的抖音号，可以通过搜索API获取

### Q4: 可以爬取哪些数据？

**A**: 可以爬取APP能访问的所有数据，包括：
- 用户视频列表
- 视频详情（标题、作者、点赞数、评论数等）
- 视频播放地址
- 用户信息
- 推荐视频列表
- 等等

### Q5: 爬取速度如何控制？

**A**: 代码中已经实现了随机延迟：
```java
randomDelay(1000, 3000);  // 随机延迟1-3秒
```

可以调整延迟时间，避免请求过快被封禁。

## 完整运行示例

### 1. 抓包获取信息

假设通过Charles抓包，发现了一个用户视频列表请求：

```
GET https://aweme.snssdk.com/aweme/v1/aweme/post/
  ?sec_user_id=MS4wLjABAAAA...
  &max_cursor=0
  &count=20
  &device_id=1234567890
  &aid=1128
  ...

Headers:
User-Agent: com.ss.android.ugc.aweme/110000 (Linux; U; Android 11; zh_CN; SM-G991B; ...)
X-Gorgon: 0404a8b000100...
X-Argus: XxXXXXxXxXx...
X-Tt-Token: xxxxxx...
```

### 2. 修改代码配置

修改 `src/main/java/com/crawler/douyin/config/AppConfig.java`：
```java
private String deviceId = "1234567890";  // 从抓包中获取
```

修改 `src/main/java/com/crawler/douyin/Main.java`：
```java
String secUserId = "MS4wLjABAAAA...";  // 从抓包中获取的真实值
```

### 3. 运行程序

```bash
# 编译
mvn clean compile

# 运行
mvn exec:java -Dexec.mainClass="com.crawler.douyin.Main"

# 或打包后运行
mvn clean package
java -jar target/douyin-crawler-1.0.0.jar
```

### 4. 查看结果

程序会在控制台输出爬取到的视频信息，包括：
- 视频标题
- 视频链接
- 作者
- 点赞数
- 等等

## 注意事项

⚠️ **重要提示**：

1. **合法性**：仅用于学习研究，遵守法律法规
2. **频率控制**：不要请求过快，避免被封禁
3. **参数时效性**：签名参数有时效性，需要定期更新
4. **设备一致性**：保持device_id等设备信息一致
5. **Token管理**：登录Token需要定期刷新

## 推荐工作流程

1. **首次使用**：
   - 配置抓包工具
   - 抓包分析，获取API接口和参数
   - 修改代码配置
   - 测试运行

2. **日常使用**：
   - 定期更新签名参数（如果使用抓包方式）
   - 或实现签名算法（长期方案）
   - 控制请求频率
   - 处理异常情况

3. **扩展功能**：
   - 实现签名算法自动生成
   - 添加数据存储（数据库）
   - 实现定时任务
   - 添加代理IP支持

## 获取帮助

如有问题，可以：
1. 查看 `docs/CAPTURE_GUIDE.md` 了解详细抓包方法
2. 查看代码注释了解各个类的作用
3. 通过抓包工具分析更多API接口

