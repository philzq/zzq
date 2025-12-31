# 京东APP自动化爬虫Demo

这是一个使用Java + Appium实现的京东Android APP自动化爬虫demo项目，通过模拟用户行为（滑动、点击、搜索等）来操作京东APP并获取商品信息。

## 技术栈

- **Java 11+**
- **Maven** - 项目构建工具
- **Appium** - 移动应用自动化测试框架
- **Selenium** - WebDriver支持
- **Lombok** - 简化Java代码
- **Logback** - 日志框架

## 项目结构

```
app/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── crawler/
│       │           └── douyin/
│       │               ├── Main.java                    # 主程序入口
│       │               ├── automation/
│       │               │   └── JdAppiumCrawler.java     # Appium自动化操作类
│       │               ├── config/
│       │               │   └── AppiumConfig.java        # Appium配置类
│       │               └── model/
│       │                   └── ProductInfo.java         # 商品信息模型
│       └── resources/
│           └── logback.xml                              # 日志配置
├── docs/
│   └── APPIUM_GUIDE.md                                 # Appium使用指南
├── pom.xml                                             # Maven配置文件
└── README.md                                           # 项目说明文档
```

## 功能特性

- ✅ 通过Appium控制真实Android设备
- ✅ 模拟用户行为（滑动、点击、搜索等）
- ✅ 自动获取商品信息（名称、价格、店铺、评价数等）
- ✅ 支持搜索商品功能
- ✅ 支持批量爬取多个商品
- ✅ 更接近真实用户行为，不易被检测
- ✅ 支持点击商品查看详情等操作

## 快速开始

### 环境要求

- JDK 11 或更高版本
- Maven 3.6 或更高版本
- Node.js（用于安装Appium）
- Android SDK（用于adb工具）
- Android设备（真实手机或模拟器）

### 安装步骤

1. **安装Appium**
   ```bash
   npm install -g appium
   appium driver install uiautomator2
   ```

2. **启动Appium服务器**
   ```bash
   appium
   ```

3. **连接Android设备**
   - 开启USB调试
   - 连接手机或启动模拟器
   - 确认连接：`adb devices`

4. **编译并运行**
   ```bash
   mvn clean compile
   mvn exec:java -Dexec.mainClass="com.crawler.douyin.Main"
   ```

**详细步骤请查看：[docs/APPIUM_GUIDE.md](docs/APPIUM_GUIDE.md)**

## 使用说明

### 核心类说明

1. **JdAppiumCrawler** - Appium自动化操作类
   - `initDriver()` - 初始化Appium驱动，启动京东APP
   - `searchProduct(String keyword)` - 搜索商品
   - `crawlProducts(int count)` - 滑动并爬取多个商品
   - `getCurrentProducts()` - 获取当前可见的商品列表
   - `getProductDetail()` - 获取商品详情页信息
   - `clickProduct(String title)` - 点击商品进入详情页
   - `swipeUp()` / `swipeDown()` - 滑动页面
   - `clickHomeTab()` / `clickCategoryTab()` - 点击底部标签
   - `goBack()` - 返回上一页

2. **AppiumConfig** - Appium配置类
   - 配置Appium服务器地址、设备信息等
   - 默认配置京东APP包名和Activity

3. **ProductInfo** - 商品信息数据模型
   - 包含商品ID、名称、价格、店铺、评价数、销量等字段

### 使用示例

```java
// 创建Appium配置
AppiumConfig config = new AppiumConfig();
config.setServerUrl("http://127.0.0.1:4723");
config.setPlatformVersion("11.0");

// 创建Appium爬虫实例
JdAppiumCrawler crawler = new JdAppiumCrawler(config);

try {
    // 初始化驱动（会启动京东APP）
    crawler.initDriver();
    
    // 搜索商品
    crawler.searchProduct("手机");
    
    // 获取当前可见的商品列表
    List<ProductInfo> products = crawler.getCurrentProducts();
    
    // 滑动并爬取多个商品
    List<ProductInfo> productList = crawler.crawlProducts(10);
    
    // 点击商品查看详情
    if (!productList.isEmpty()) {
        crawler.clickProduct(productList.get(0).getTitle());
        ProductInfo detail = crawler.getProductDetail();
        crawler.goBack();
    }
    
} finally {
    crawler.close();  // 关闭驱动
}
```

## 注意事项

⚠️ **重要提示**：

1. **合法性**：仅用于学习研究，遵守法律法规
2. **设备准备**：需要Android设备（真实手机或模拟器）已连接
3. **Appium服务器**：运行前必须启动Appium服务器
4. **APP更新**：京东APP更新后，元素ID可能变化，需要使用Appium Inspector查看并更新代码
5. **频率控制**：不要操作过快，避免被限制
6. **稳定性**：网络波动、APP加载慢等情况可能影响稳定性
7. **元素定位**：代码中使用XPath定位，如果定位失败，请使用Appium Inspector查看实际元素结构
8. **包名确认**：京东APP包名为 `com.jingdong.app.mall`，如果连接失败请确认APP已正确安装

## 常见问题

### Q1: 连接失败，提示无法连接到Appium服务器

**A**: 
- 确认Appium服务器已启动（`appium`命令）
- 检查服务器地址和端口是否正确（默认：http://127.0.0.1:4723）

### Q2: 找不到设备

**A**:
- 运行 `adb devices` 确认设备已连接
- 如果是USB连接，检查USB调试是否开启

### Q3: 找不到元素（NoSuchElementException）

**A**:
- APP版本可能已更新，元素ID发生变化
- 使用Appium Inspector查看当前元素的ID
- 修改代码中的元素定位器

## 相关资源

- Appium官方文档：http://appium.io/docs/en/about-appium/intro/
- Appium Java客户端：https://github.com/appium/java-client
- Appium Desktop：https://github.com/appium/appium-desktop
- [Appium Inspector配置指南](docs/APPIUM_INSPECTOR_SETUP.md) - 详细说明如何连接手机

## 许可证

本项目仅用于学习和研究目的。
