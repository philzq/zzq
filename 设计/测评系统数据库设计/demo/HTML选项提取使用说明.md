# HTML选项提取使用说明

## 问题修复

选项提取已支持**从 JavaScript 数据提取**（推荐），数据来源为页面内嵌的 JSON，不依赖 HTML 结构。

### 从 JavaScript 数据提取的内容

1. **application/ld+json**（`<script type="application/ld+json">`）
   - Product 的 `name`、`sku`、`offers.price`、`offers.url`
   - 从 URL 中解析 `itemId`、`vendorItemId`

2. **self.__next_f.push**（Next.js 序列化数据）
   - payload 中的 `urlQuery.itemId`、`urlQuery.vendorItemId`

### 从 HTML 结构提取的内容（备用）

1. **颜色选项** - 从 `tab-selector__tab-image-title` 提取  
2. **数量/价格/发货方式** - 从 `option-table-list__option-*`、`data-badge-id` 提取  

## 使用方法

### 方法1: 从 JavaScript 数据提取（推荐）

```java
String html = new String(Files.readAllBytes(Paths.get("7643741908.html")), "UTF-8");
List<ProductOption> options = HtmlProductParser.parseHtmlFromJavaScriptData(html);
```

这个方法会：
- 解析所有 `type="application/ld+json"` 中的 Product，得到 name、price、itemId、vendorItemId
- 解析 `self.__next_f.push([1,"..."])` 中的 urlQuery，补充 itemId/vendorItemId
- 按 vendorItemId 去重，优先使用 ld+json 的完整信息

### 方法2: 从 HTML 结构提取

```java
List<ProductOption> options = HtmlProductParser.parseHtmlFromStructure(html);
```

- 从 `option-table-list__option-name`、`option-table-list__option-price`、`data-badge-id` 等 DOM 结构提取
- 自动组合颜色 × 数量等选项名称

### 方法3: 从 vendorItemId 提取（备用）

```java
List<ProductOption> options = HtmlProductParser.parseHtml(html);
```

这个方法会：
- 从URL和参数中提取vendorItemId
- 在上下文中查找发货方式和价格
- 提取选项名称

### 完整示例

```java
public static void main(String[] args) {
    try {
        String filePath = "src/main/resources/7643741908.html";
        String html = new String(Files.readAllBytes(Paths.get(filePath)), "UTF-8");
        
        // 从HTML结构提取
        List<ProductOption> options = HtmlProductParser.parseHtmlFromStructure(html);
        
        // 生成报告
        String report = HtmlProductParser.generateReport(options);
        System.out.println(report);
        
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

## 提取的正则表达式

### 颜色提取
```regex
class="tab-selector__tab-image-title"[^>]*>\s*([가-힣]+)\s*<
```

### 数量提取
```regex
class="option-table-list__option-name"[^>]*>\s*([^<\n]+?)\s*<
```

### 价格提取
```regex
class="option-table-list__option-price"[^>]*>\s*([\d,]+원)
```

### 发货方式提取
```regex
data-badge-id="(ROCKET[^"]*)"
```

## 输出示例

```
==========================================
HTML产品属性与发货方式分析报告
==========================================

【一、产品选项汇总】
------------------------------------------
共找到 9 个选项:

1. 레인보우 × 1세트
   发货方式: ROCKET (火箭配送（쿠팡 직접 배송）)
   价格: 38,170원
   配送徽章: ROCKET, TOMORROW

2. 레인보우 × 2개
   发货方式: ROCKET_MERCHANT (火箭商家配送（판매자 직접 배송）)
   价格: 72,190원
   配送徽章: ROCKET_MERCHANT, TOMORROW

3. 레인보우 × 4개
   发货方式: ROCKET_MERCHANT (火箭商家配送（판매자 직접 배송）)
   价格: 134,070원
   配送徽章: ROCKET_MERCHANT, TOMORROW

...

【二、按发货方式分组】
------------------------------------------

ROCKET (火箭配送（쿠팡 직접 배송）): 3 个选项
  - 레인보우 × 1세트 (价格: 38,170원)
  - 우드 × 1세트 (价格: 38,170원)
  - 파스텔 × 1세트 (价格: 38,170원)

ROCKET_MERCHANT (火箭商家配送（판매자 직접 배송）): 6 个选项
  - 레인보우 × 2개 (价格: 72,190원)
  - 레인보우 × 4개 (价格: 134,070원)
  ...
```

## 注意事项

1. **HTML结构变化**: 如果网站更新HTML结构，可能需要调整正则表达式
2. **选项组合**: 当前实现会将所有颜色与所有数量组合，如果实际HTML中有限制，可能需要进一步优化
3. **vendorItemId**: 从结构提取的方法可能无法直接获取vendorItemId，需要结合方法2的结果

## 运行测试

编译项目后，运行：

```bash
java -cp "target/classes;依赖路径" com.baotongit.webdriver.utils.HtmlProductParser
```

或者直接在IDE中运行 `HtmlProductParser.main()` 方法。

