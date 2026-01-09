# 电商产品图片分析工具

基于Java JDK11和Maven的产品图片分析工具，支持主图和详情页图片的智能分析。

## 功能特性

### 主图分析维度
- ✅ 明确产品主题（什么产品）
- ✅ 人货景三要素分析
- ✅ 白底图/场景图识别
- ✅ 渲染图/实拍图判断
- ✅ 单个产品/组合产品识别
- ✅ 产品展示视角分析
- ✅ 文字卖点提取（OCR）
- ✅ 明显"记忆点"识别

### 详情页分析维度
- ✅ 产品识别（是什么）
- ✅ 产品卖点提取
  - 核心卖点
  - 其他卖点
  - 解决的痛点
  - 卖点重复检测
- ✅ 产品参数识别
- ✅ 使用场景覆盖分析
- ✅ 参数与事实表达评估
- ✅ 信任与合规信息检测
- ✅ 使用与售后说明识别
- ✅ 信息密度与阅读负担评估
- ✅ 情绪与说服方式分析
- ✅ 风险暴露与用户误解点评估

## 技术栈

- **Java**: JDK 11
- **构建工具**: Maven
- **OCR引擎**: Tess4J (基于Tesseract)
- **图片处理**: Java ImageIO
- **网页爬取**: Jsoup (HTML解析)
- **HTTP客户端**: Apache HttpClient (图片下载)
- **JSON处理**: Jackson
- **日志**: SLF4J + Logback
- **工具库**: Lombok, Commons IO

## 环境要求

1. **JDK 11** 或更高版本
2. **Maven 3.6+**
3. **Tesseract OCR引擎**（用于文字识别）
   - Windows: 下载安装 [Tesseract for Windows](https://github.com/UB-Mannheim/tesseract/wiki)
   - Mac: `brew install tesseract`
   - Linux: `sudo apt-get install tesseract-ocr tesseract-ocr-chi-sim`（支持中文）

## 安装与构建

### 1. 克隆或下载项目

```bash
cd 电商市场调研
```

### 2. 编译项目

```bash
mvn clean compile
```

### 3. 打包项目

```bash
mvn clean package
```

打包完成后，会在 `target/` 目录下生成 `product-image-analyzer-1.0.0.jar` 文件。

## 使用方法

### 🎯 方式一：通过命令行参数传递产品URL（推荐）

**支持多种命令行参数格式，灵活传递产品链接！**

程序会自动：
1. ✅ 访问产品页面并提取页面信息
2. ✅ 识别并下载主图
3. ✅ 识别并下载详情页图片
4. ✅ 对每张图片进行分析
5. ✅ 生成完整的分析报告

### 支持的参数格式

程序支持以下三种方式传递产品URL：

#### 格式1：直接URL作为参数（最简单）

```bash
# 直接传递URL作为第一个参数
java -jar target/product-image-analyzer-1.0.0.jar https://www.coupang.com/vp/products/8729129197

# 带完整参数的URL
java -jar target/product-image-analyzer-1.0.0.jar "https://www.coupang.com/vp/products/8729129197?itemId=25361636153&vendorItemId=92474828864"
```

#### 格式2：使用 --url= 参数

```bash
# 使用 --url= 格式
java -jar target/product-image-analyzer-1.0.0.jar --url=https://www.coupang.com/vp/products/8729129197

# 带引号的URL（如果URL包含特殊字符）
java -jar target/product-image-analyzer-1.0.0.jar --url="https://www.coupang.com/vp/products/8729129197?itemId=25361636153"
```

#### 格式3：使用 -u 参数

```bash
# 使用 -u 短参数格式
java -jar target/product-image-analyzer-1.0.0.jar -u https://www.coupang.com/vp/products/8729129197

# 或者 -u= 格式
java -jar target/product-image-analyzer-1.0.0.jar -u=https://www.coupang.com/vp/products/8729129197
```

### 完整示例

```bash
# 示例1: 最简单的用法（直接URL）
java -jar target/product-image-analyzer-1.0.0.jar https://www.coupang.com/vp/products/8729129197

# 示例2: 使用 --url= 格式
java -jar target/product-image-analyzer-1.0.0.jar --url=https://www.coupang.com/vp/products/8729129197

# 示例3: 使用 -u 格式
java -jar target/product-image-analyzer-1.0.0.jar -u https://www.coupang.com/vp/products/8729129197

# 示例4: 完整URL示例（带查询参数）
java -jar target/product-image-analyzer-1.0.0.jar "https://www.coupang.com/vp/products/8729129197?itemId=25361636153&vendorItemId=92474828864&q=AIRYES&searchId=1d587e9214947&sourceType=search&itemsCount=40&searchRank=1&rank=1&traceId=mk5aieqa"

# 示例5: 使用Maven执行（需要先编译）
mvn exec:java -Dexec.args="https://www.coupang.com/vp/products/8729129197"
```

### 默认行为

- **如果未提供参数**：程序会使用代码中配置的默认URL（用于测试）
- **如果参数格式错误**：程序会显示使用说明并退出

**注意：** 
- URL如果包含特殊字符（如 `&`），建议用引号括起来
- Windows系统建议使用双引号：`"https://..."`
- Linux/Mac系统可以使用单引号或双引号

## 输出结果

分析结果会以JSON格式保存到文件，文件名格式：
- **产品URL分析**：`analysis_url_<产品ID>_<时间戳>.json` ⭐
- 主图：`analysis_main_<图片名>_<时间戳>.json`
- 详情页：`analysis_detail_<图片名>_<时间戳>.json`
- 批量：`analysis_result_batch_<时间戳>.json`

### 产品URL分析结果包含：
- ✅ 产品基本信息（标题、ID、URL）
- ✅ 主图URL列表和分析结果
- ✅ 详情页图片URL列表和分析结果
- ✅ 完整分析总结

### JSON结果示例

```json
{
  "imagePath": "商品详情页/image.jpg",
  "width": 1200,
  "height": 1600,
  "format": "jpg",
  "fileSize": 245678,
  "detailPageAnalyses": [
    {
      "productName": "AIRYES产品",
      "coreSellingPoints": ["4D立体按摩", "无刷电机"],
      "hasProductParams": false,
      "misunderstandingRisk": "HIGH",
      ...
    }
  ],
  "analysisTimestamp": 1234567890,
  "summary": "..."
}
```

## 项目结构

```
电商市场调研/
├── pom.xml                                    # Maven配置文件
├── README.md                                  # 项目说明
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── ecommerce/
│                   └── analyzer/
│                       ├── ProductImageAnalyzerApplication.java  # 主程序入口
│                       ├── model/                                # 数据模型
│                       │   ├── MainImageAnalysis.java
│                       │   ├── DetailPageAnalysis.java
│                       │   └── AnalysisResult.java
│                       └── service/                              # 服务层
│                           ├── ImageProcessor.java              # 图片处理
│                           ├── OCRService.java                  # OCR文字识别
│                           ├── MainImageAnalyzer.java           # 主图分析
│                           ├── DetailPageAnalyzer.java          # 详情页分析
│                           └── AnalysisService.java             # 综合分析服务
└── target/                                  # 编译输出目录
```

## OCR配置说明

如果遇到OCR识别问题，可能需要配置Tesseract数据路径：

1. **Windows**: 通常安装在 `C:\Program Files\Tesseract-OCR\tessdata`
2. **Mac**: 通常为 `/usr/local/share/tessdata` 或 `/opt/homebrew/share/tessdata`
3. **Linux**: 通常为 `/usr/share/tesseract-ocr/4.00/tessdata`

如需修改，编辑 `OCRService.java` 中的 `setDatapath()` 方法。

## 注意事项

1. **OCR准确性**：OCR识别准确度取决于图片质量和文字清晰度
2. **图片格式**：支持常见图片格式（JPG, PNG, GIF, BMP等）
3. **中文识别**：需要安装中文语言包（chi_sim）
4. **性能**：大图片分析可能需要较长时间
5. **网络访问**：URL分析功能需要能够访问目标网站（如Coupang）
6. **爬取限制**：请遵守网站的robots.txt和使用条款，避免频繁请求
7. **图片下载**：下载的图片默认会临时保存在`downloads/`目录，分析完成后自动清理（可选择保留）

## 扩展开发

### 添加新的分析维度

1. 在对应的 `AnalysisResult` 或 `MainImageAnalysis` / `DetailPageAnalysis` 中添加字段
2. 在对应的 `Analyzer` 类中实现分析逻辑
3. 更新结果生成方法

### 自定义OCR配置

修改 `OCRService.java` 中的语言设置：

```java
tesseract.setLanguage("chi_sim+eng+kor"); // 支持中文+英文+韩文
```

## 常见问题

### Q: OCR识别失败？
A: 确保已安装Tesseract OCR引擎，并且数据路径配置正确。

### Q: 中文识别不准确？
A: 确保安装了中文语言包（chi_sim），并且图片清晰度足够高。

### Q: 分析速度慢？
A: 可以优化图片采样算法，或使用更强大的OCR引擎。

## 许可证

本项目仅供学习和研究使用。

## 联系方式

如有问题或建议，请提交Issue。

