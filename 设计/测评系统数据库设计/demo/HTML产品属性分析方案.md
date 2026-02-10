# HTML产品属性与发货方式分析方案

## 一、分析目标

从HTML文件中提取：
1. 产品基本信息（产品ID、产品名称）
2. 产品选项（属性组合、vendorItemId、itemId）
3. 发货方式（ROCKET、ROCKET_MERCHANT）
4. 价格信息

## 二、正则表达式提取方案

### 1. 产品基本信息提取

#### 产品ID
```regex
data-product-id="(\d+)"
```
**示例匹配**: `data-product-id="7643741908"`

#### 产品名称
```regex
<title>([^<]+)</title>
```
**示例匹配**: `<title>벨베이비 영유아 자석블럭 빅사이즈 35mm, 파스텔, 4개 - 자석블록 | 쿠팡</title>`

#### vendorItemId（从URL或参数中提取）
```regex
vendorItemId[=:](\d+)
```
**示例匹配**: 
- `vendorItemId=90529279265`
- `vendorItemId:90529279265`

### 2. 发货方式提取

#### 发货方式徽章标识
```regex
data-badge-id="(ROCKET[^"]*)"
```
**匹配类型**:
- `data-badge-id="ROCKET"` → 火箭配送
- `data-badge-id="ROCKET_MERCHANT"` → 火箭商家配送
- `data-badge-id="TOMORROW"` → 次日达标识

#### 发货方式组合识别
```regex
data-badge-id="(ROCKET[^"]*)"[^>]*>.*?data-badge-id="(TOMORROW)"
```
用于识别同时包含ROCKET和TOMORROW的组合

### 3. 选项信息提取

#### vendorItemId和itemId组合（从URL）
```regex
href="[^"]*itemId=(\d+)[^"]*vendorItemId=(\d+)[^"]*"
```
**示例匹配**: `href="/vp/products/7643741908?itemId=25192718708&vendorItemId=90529279265"`

#### 选项名称（从链接文本）
```regex
href="[^"]*vendorItemId=(\d+)[^"]*"[^>]*>([^<]+)</a>
```
提取链接中的文本作为选项名称

#### 选项名称（从属性文本）
```regex
([가-힣]+\s*[×x]\s*\d+[가-힣]*|[가-힣]+\s*\d+[가-힣]*)
```
**示例匹配**: 
- `레인보우 × 1세트`
- `파스텔 4개`

### 4. 价格信息提取

#### 价格格式
```regex
(\d{1,3}(?:,\d{3})*)\s*원
```
**示例匹配**: 
- `38,170원`
- `101,850원`

## 三、实现代码

### 核心提取逻辑

```java
// 1. 提取所有vendorItemId及其上下文
Pattern vendorPattern = Pattern.compile("vendorItemId[=:](\\d+)");
Matcher matcher = vendorPattern.matcher(html);

Map<String, String> contexts = new LinkedHashMap<>();
while (matcher.find()) {
    String vendorItemId = matcher.group(1);
    if (!contexts.containsKey(vendorItemId)) {
        // 提取前后500字符的上下文
        int start = Math.max(0, matcher.start() - 500);
        int end = Math.min(html.length(), matcher.end() + 500);
        String context = html.substring(start, end);
        contexts.put(vendorItemId, context);
    }
}

// 2. 在每个上下文中提取发货方式
Pattern badgePattern = Pattern.compile("data-badge-id=\"(ROCKET[^\"]*)\"");
Matcher badgeMatcher = badgePattern.matcher(context);

Set<String> deliveryTypes = new LinkedHashSet<>();
while (badgeMatcher.find()) {
    String badge = badgeMatcher.group(1);
    if ("ROCKET".equals(badge)) {
        deliveryTypes.add("ROCKET");
    } else if ("ROCKET_MERCHANT".equals(badge)) {
        deliveryTypes.add("ROCKET_MERCHANT");
    }
}

// 3. 提取价格
Pattern pricePattern = Pattern.compile("(\\d{1,3}(?:,\\d{3})*)\\s*원");
Matcher priceMatcher = pricePattern.matcher(context);
if (priceMatcher.find()) {
    String price = priceMatcher.group(1) + "원";
}
```

## 四、分析流程

### 步骤1: 读取HTML文件
```java
String html = new String(Files.readAllBytes(Paths.get("7643741908.html")), "UTF-8");
```

### 步骤2: 提取vendorItemId列表
使用正则表达式提取所有唯一的vendorItemId

### 步骤3: 为每个vendorItemId提取上下文
提取每个vendorItemId前后500字符的上下文，用于后续分析

### 步骤4: 在上下文中提取相关信息
- 发货方式（通过data-badge-id）
- 价格（通过价格格式匹配）
- 选项名称（通过链接文本或属性文本）
- itemId（通过URL参数）

### 步骤5: 建立映射关系
- vendorItemId → 发货方式
- vendorItemId → 价格
- vendorItemId → 选项名称

### 步骤6: 生成分析报告
按发货方式分组，生成汇总报告

## 五、使用示例

### 使用HtmlProductParser类

```java
// 解析HTML文件
List<ProductOption> options = HtmlProductParser.parseHtmlFile("src/main/resources/7643741908.html");

// 生成报告
String report = HtmlProductParser.generateReport(options);
System.out.println(report);

// 按发货方式查询
List<ProductOption> rocketOptions = options.stream()
    .filter(opt -> "ROCKET".equals(opt.getDeliveryType()))
    .collect(Collectors.toList());
```

### 使用HtmlProductAttributeAnalyzer类

```java
// 直接分析HTML文件
HtmlProductAttributeAnalyzer.main(new String[]{});
```

## 六、输出结果示例

```
==========================================
HTML产品属性与发货方式分析报告
==========================================

【一、产品选项汇总】
------------------------------------------
共找到 8 个选项:

1. 레인보우 × 1세트
   vendorItemId: 93839141897
   itemId: 20318296686
   发货方式: ROCKET (火箭配送（쿠팡 직접 배송）)
   价格: 38,170원
   配送徽章: ROCKET, TOMORROW

2. 레인보우 × 2세트
   vendorItemId: 90529008779
   itemId: 20318296684
   发货方式: ROCKET_MERCHANT (火箭商家配送（판매자 직접 배송）)
   价格: 69,320원
   配送徽章: ROCKET_MERCHANT, TOMORROW

...

【二、按发货方式分组】
------------------------------------------

ROCKET (火箭配送（쿠팡 직접 배송）): 2 个选项
  - 레인보우 × 1세트 (vendorItemId: 93839141897, 价格: 38,170원)
  - 파스텔 × 1세트 (vendorItemId: 93838833518, 价格: 38,170원)

ROCKET_MERCHANT (火箭商家配送（판매자 직접 배송）): 6 个选项
  - 레인보우 × 2세트 (vendorItemId: 90529008779, 价格: 69,320원)
  - 레인보우 × 3세트 (vendorItemId: 90529008737, 价格: 101,850원)
  ...
```

## 七、注意事项

1. **HTML结构变化**: HTML结构可能随网站更新而变化，需要定期更新正则表达式
2. **动态内容**: 某些数据可能在JavaScript中动态加载，静态HTML可能不包含完整信息
3. **上下文范围**: vendorItemId的上下文范围（500字符）可能需要根据实际情况调整
4. **性能考虑**: 对于大型HTML文件，考虑使用流式处理或分块处理
5. **数据验证**: 建议结合JSON数据（如果存在）进行交叉验证

## 八、扩展建议

1. **使用Jsoup库**: 作为正则表达式的补充，使用HTML解析库可以更准确地提取数据
2. **缓存机制**: 对解析结果进行缓存，避免重复解析
3. **错误处理**: 添加完善的错误处理和日志记录
4. **单元测试**: 为每个正则表达式编写单元测试
5. **配置化**: 将正则表达式提取到配置文件，便于维护

## 九、文件说明

- `HtmlProductParser.java`: 完整的HTML解析器，包含ProductOption实体类和报告生成功能
- `HtmlProductAttributeAnalyzer.java`: HTML分析工具，提供详细的分析报告
- `HTML产品属性分析方案.md`: 本文档，包含完整的分析方案和使用说明

