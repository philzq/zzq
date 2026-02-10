package com.baotongit.webdriver.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

/**
 * HTML产品属性分析工具
 * 使用正则表达式从HTML中提取产品属性和发货方式
 *
 * @author baotongit
 */
public class HtmlProductAttributeAnalyzer {
    
    public static void main(String[] args) {
        try {
            Path htmlPath = Paths.get("src/main/resources/7643741908.html");
            if (!Files.exists(htmlPath)) {
                System.err.println("HTML文件不存在: " + htmlPath);
                return;
            }
            
            String htmlContent = new String(Files.readAllBytes(htmlPath), "UTF-8");
            
            System.out.println("==========================================");
            System.out.println("HTML产品属性与发货方式分析报告");
            System.out.println("==========================================\n");
            
            // 分析产品信息
            analyzeProductFromHtml(htmlContent);
            
        } catch (Exception e) {
            System.err.println("分析失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 从HTML中分析产品信息
     */
    private static void analyzeProductFromHtml(String html) {
        // 1. 提取产品基本信息
        extractProductBasicInfo(html);
        
        // 2. 提取产品选项和发货方式
        extractProductOptions(html);
        
        // 3. 提取发货方式统计
        analyzeDeliveryTypes(html);
        
        // 4. 生成分析方案
        generateAnalysisPlan();
    }
    
    /**
     * 提取产品基本信息
     */
    private static void extractProductBasicInfo(String html) {
        System.out.println("【一、产品基本信息】");
        System.out.println("------------------------------------------");
        
        // 提取产品ID
        Pattern productIdPattern = Pattern.compile("data-product-id=\"(\\d+)\"");
        Matcher productIdMatcher = productIdPattern.matcher(html);
        if (productIdMatcher.find()) {
            System.out.println("产品ID: " + productIdMatcher.group(1));
        }
        
        // 提取产品名称（从title标签）
        Pattern titlePattern = Pattern.compile("<title>([^<]+)</title>");
        Matcher titleMatcher = titlePattern.matcher(html);
        if (titleMatcher.find()) {
            String title = titleMatcher.group(1);
            System.out.println("产品名称: " + title.split(" - ")[0]);
        }
        
        // 提取vendorItemId（从URL或meta标签）
        Pattern vendorItemPattern = Pattern.compile("vendorItemId[=:](\\d+)");
        Matcher vendorItemMatcher = vendorItemPattern.matcher(html);
        Set<String> vendorItemIds = new LinkedHashSet<>();
        while (vendorItemMatcher.find()) {
            vendorItemIds.add(vendorItemMatcher.group(1));
        }
        if (!vendorItemIds.isEmpty()) {
            System.out.println("供应商商品ID列表: " + String.join(", ", vendorItemIds));
        }
        
        System.out.println();
    }
    
    /**
     * 提取产品选项和发货方式
     */
    private static void extractProductOptions(String html) {
        System.out.println("【二、产品选项与发货方式分析】");
        System.out.println("------------------------------------------");
        
        // 方案1: 从data-badge-id提取发货方式标识
        extractDeliveryBadges(html);
        
        // 方案2: 从JavaScript数据提取选项信息（ld+json、__next_f 等）
        extractOptionsFromJavaScriptData(html);

        // 方案2c: 直接从 "items":[...] 中提取选项（不依赖 tabList）
        extractOptionsFromItems(html);
        
        // 方案3: 从链接URL提取vendorItemId和选项信息
        extractOptionsFromUrls(html);
        
        System.out.println();
    }
    
    /**
     * 提取配送徽章（发货方式标识）
     */
    private static void extractDeliveryBadges(String html) {
        System.out.println("\n1. 配送徽章分析（发货方式标识）:");
        
        // 匹配 data-badge-id 属性
        Pattern badgePattern = Pattern.compile("data-badge-id=\"([^\"]+)\"");
        Matcher badgeMatcher = badgePattern.matcher(html);
        
        Map<String, Integer> badgeCount = new LinkedHashMap<>();
        Set<String> uniqueBadges = new LinkedHashSet<>();
        
        while (badgeMatcher.find()) {
            String badgeId = badgeMatcher.group(1);
            uniqueBadges.add(badgeId);
            badgeCount.put(badgeId, badgeCount.getOrDefault(badgeId, 0) + 1);
        }
        
        System.out.println("   发现的配送徽章类型:");
        for (String badge : uniqueBadges) {
            int count = badgeCount.get(badge);
            String description = getBadgeDescription(badge);
            System.out.println("     - " + badge + " (" + description + "): " + count + " 次");
        }
        
        // 分析发货方式组合
        analyzeBadgeCombinations(html);
    }
    
    /**
     * 分析徽章组合（通常ROCKET和ROCKET_MERCHANT会与TOMORROW一起出现）
     */
    private static void analyzeBadgeCombinations(String html) {
        System.out.println("\n   配送徽章组合分析:");
        
        // 查找连续的data-badge-id
        Pattern consecutiveBadgePattern = Pattern.compile(
            "data-badge-id=\"(ROCKET[^\"]*)\"[^>]*>.*?data-badge-id=\"(TOMORROW)\"",
            Pattern.DOTALL
        );
        Matcher matcher = consecutiveBadgePattern.matcher(html);
        
        Map<String, Integer> combinationCount = new LinkedHashMap<>();
        while (matcher.find()) {
            String combination = matcher.group(1) + " + " + matcher.group(2);
            combinationCount.put(combination, combinationCount.getOrDefault(combination, 0) + 1);
        }
        
        for (Map.Entry<String, Integer> entry : combinationCount.entrySet()) {
            System.out.println("     - " + entry.getKey() + ": " + entry.getValue() + " 次");
        }
    }
    
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    /**
     * 从JavaScript数据提取选项信息
     * 数据来源：1) script type="application/ld+json" 中的 Product 结构
     *         2) self.__next_f.push([1,"..."]) 中的 urlQuery（itemId、vendorItemId）
     */
    private static void extractOptionsFromJavaScriptData(String html) {
        System.out.println("\n2. 从JavaScript数据提取选项:");
        
        // 2.1 从 application/ld+json 的 Product 中提取
        extractOptionsFromLdJson(html);
        
        // 2.2 从 self.__next_f.push 的 payload 中提取 urlQuery（itemId、vendorItemId）
        extractOptionsFromNextF(html);
    }
    
    /**
     * 从 script type="application/ld+json" 中解析 Product，提取选项（name、price、itemId、vendorItemId）
     */
    private static void extractOptionsFromLdJson(String html) {
        // 匹配 <script ... type="application/ld+json">...</script> 中的 JSON 内容
        Pattern scriptPattern = Pattern.compile(
            "<script[^>]*type\\s*=\\s*[\"']application/ld\\+json[\"'][^>]*>\\s*(\\{[^<]+?})\\s*</script>",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
        );
        Matcher scriptMatcher = scriptPattern.matcher(html);
        
        int count = 0;
        while (scriptMatcher.find()) {
            String jsonStr = scriptMatcher.group(1).trim();
            if (!jsonStr.contains("\"@type\":\"Product\"") && !jsonStr.contains("\"@type\": \"Product\"")) {
                continue;
            }
            try {
                JsonNode root = JSON_MAPPER.readTree(jsonStr);
                if (root.has("@type") && "Product".equals(root.path("@type").asText(null))) {
                    String name = root.has("name") ? root.get("name").asText("") : "";
                    String sku = root.has("sku") ? root.get("sku").asText("") : "";
                    String price = "";
                    String itemId = "";
                    String vendorItemId = "";
                    if (root.has("offers") && root.get("offers").isObject()) {
                        JsonNode offers = root.get("offers");
                        if (offers.has("price")) {
                            price = offers.get("price").asText("") + "원";
                        }
                        if (offers.has("url")) {
                            String url = offers.get("url").asText("");
                            java.util.regex.Matcher mid = Pattern.compile("itemId=([0-9]+)").matcher(url);
                            if (mid.find()) itemId = mid.group(1);
                            java.util.regex.Matcher mvid = Pattern.compile("vendorItemId=([0-9]+)").matcher(url);
                            if (mvid.find()) vendorItemId = mvid.group(1);
                        }
                    }
                    if (sku != null && sku.contains("-")) {
                        String[] parts = sku.split("-");
                        if (parts.length >= 2 && itemId.isEmpty()) itemId = parts[1];
                    }
                    count++;
                    System.out.println("   [ld+json] 选项: name=\"" + name + "\", sku=" + sku + ", price=" + price + ", itemId=" + itemId + ", vendorItemId=" + vendorItemId);
                }
            } catch (Exception e) {
                // 单条解析失败不影响其他
            }
        }
        if (count > 0) {
            System.out.println("   从 ld+json 共解析 " + count + " 个选项");
        }
    }
    
    /**
     * 从 self.__next_f.push 内嵌的 urlQuery 提取 itemId、vendorItemId
     * 直接在整段 HTML 上匹配固定格式，避免提取超大 payload 导致正则灾难性回溯（StackOverflow）
     */
    private static void extractOptionsFromNextF(String html) {
        Set<String> seen = new LinkedHashSet<>();
        int count = 0;
        // 固定格式匹配，无 * 或 + 在可变长片段上，避免回溯爆炸
        // 转义形式: \"itemId\":\"123\",\"vendorItemId\":\"456\"
        Pattern urlQueryEscaped = Pattern.compile("\\\\\"itemId\\\\\":\\\\\"(\\d+)\\\\\",\\\\\"vendorItemId\\\\\":\\\\\"(\\d+)\\\\\"");
        Matcher m = urlQueryEscaped.matcher(html);
        while (m.find()) {
            String itemId = m.group(1);
            String vendorItemId = m.group(2);
            if (seen.add(itemId + "," + vendorItemId)) {
                count++;
                System.out.println("   [__next_f] itemId=" + itemId + ", vendorItemId=" + vendorItemId);
            }
        }
        // 未转义形式: "itemId":"123","vendorItemId":"456"
        if (count == 0) {
            Pattern urlQueryPlain = Pattern.compile("\"itemId\"\\s*:\\s*\"(\\d+)\"\\s*,\\s*\"vendorItemId\"\\s*:\\s*\"(\\d+)\"");
            m = urlQueryPlain.matcher(html);
            while (m.find()) {
                String itemId = m.group(1);
                String vendorItemId = m.group(2);
                if (seen.add(itemId + "," + vendorItemId)) {
                    count++;
                    System.out.println("   [__next_f] itemId=" + itemId + ", vendorItemId=" + vendorItemId);
                }
            }
        }
        if (count > 0) {
            System.out.println("   从 __next_f 共解析 " + count + " 个选项组合");
        }
    }
    
    /**
     * 从 tabList[attributeId].items 提取并输出（通常是 valueName 列表）
     */
    private static void extractOptionsFromItems(String html) {
        System.out.println("\n2c. 从 \\\"items\\\" 数组提取选项（不依赖 tabList）:");
        java.util.List<HtmlProductParser.ProductOption> options = HtmlProductParser.parseHtmlFromItemsArrays(html);
        if (options.isEmpty()) {
            System.out.println("   未解析到包含 itemBasicInfo/valueName 的 items 选项");
            return;
        }
        System.out.println("   解析出 " + options.size() + " 个选项:");
        for (int i = 0; i < options.size(); i++) {
            HtmlProductParser.ProductOption opt = options.get(i);
            System.out.println("     " + (i + 1) + ". " + (opt.getItemName() != null ? opt.getItemName() : "-")
                + " | vendorItemId=" + (opt.getVendorItemId() != null ? opt.getVendorItemId() : "-")
                + " | itemId=" + (opt.getItemId() != null ? opt.getItemId() : "-")
                + " | price=" + (opt.getPrice() != null ? opt.getPrice() : "-")
                + " | delivery=" + (opt.getDeliveryType() != null ? opt.getDeliveryType() : "-"));
        }
    }
    
    /**
     * 从URL中提取选项信息
     */
    private static void extractOptionsFromUrls(String html) {
        System.out.println("\n3. 从URL提取选项信息:");
        
        // 匹配包含itemId和vendorItemId的URL
        Pattern urlPattern = Pattern.compile(
            "href=\"[^\"]*itemId=(\\d+)[^\"]*vendorItemId=(\\d+)[^\"]*\"",
            Pattern.CASE_INSENSITIVE
        );
        Matcher urlMatcher = urlPattern.matcher(html);
        
        Set<String> uniqueCombinations = new LinkedHashSet<>();
        while (urlMatcher.find()) {
            String itemId = urlMatcher.group(1);
            String vendorItemId = urlMatcher.group(2);
            uniqueCombinations.add("itemId=" + itemId + ", vendorItemId=" + vendorItemId);
        }
        
        if (!uniqueCombinations.isEmpty()) {
            System.out.println("   找到 " + uniqueCombinations.size() + " 个唯一的选项组合:");
            int index = 1;
            for (String combo : uniqueCombinations) {
                System.out.println("     " + index++ + ". " + combo);
            }
        }
    }
    
    /**
     * 分析发货方式
     */
    private static void analyzeDeliveryTypes(String html) {
        System.out.println("\n【三、发货方式详细分析】");
        System.out.println("------------------------------------------");
        
        // 统计ROCKET和ROCKET_MERCHANT的出现次数
        int rocketCount = countOccurrences(html, "data-badge-id=\"ROCKET\"");
        int rocketMerchantCount = countOccurrences(html, "data-badge-id=\"ROCKET_MERCHANT\"");
        int tomorrowCount = countOccurrences(html, "data-badge-id=\"TOMORROW\"");
        
        System.out.println("发货方式统计:");
        System.out.println("  - ROCKET (火箭配送): " + rocketCount + " 次");
        System.out.println("  - ROCKET_MERCHANT (火箭商家配送): " + rocketMerchantCount + " 次");
        System.out.println("  - TOMORROW (次日达): " + tomorrowCount + " 次");
        
        // 分析发货方式与选项的关联
        analyzeDeliveryOptionMapping(html);
    }
    
    /**
     * 分析发货方式与选项的映射关系
     */
    private static void analyzeDeliveryOptionMapping(String html) {
        System.out.println("\n发货方式与选项映射:");
        
        // 查找每个vendorItemId附近的徽章信息
        Pattern vendorItemPattern = Pattern.compile("vendorItemId=(\\d+)");
        Matcher vendorMatcher = vendorItemPattern.matcher(html);
        
        Map<String, List<String>> vendorDeliveryMap = new LinkedHashMap<>();
        
        while (vendorMatcher.find()) {
            String vendorItemId = vendorMatcher.group(1);
            int start = Math.max(0, vendorMatcher.start() - 500);
            int end = Math.min(html.length(), vendorMatcher.end() + 500);
            String context = html.substring(start, end);
            
            // 在上下文中查找徽章
            Pattern badgeInContextPattern = Pattern.compile("data-badge-id=\"(ROCKET[^\"]*)\"");
            Matcher badgeMatcher = badgeInContextPattern.matcher(context);
            
            Set<String> badges = new LinkedHashSet<>();
            while (badgeMatcher.find()) {
                badges.add(badgeMatcher.group(1));
            }
            
            if (!badges.isEmpty()) {
                vendorDeliveryMap.put(vendorItemId, new ArrayList<>(badges));
            }
        }
        
        if (!vendorDeliveryMap.isEmpty()) {
            System.out.println("  找到 " + vendorDeliveryMap.size() + " 个选项的发货方式:");
            for (Map.Entry<String, List<String>> entry : vendorDeliveryMap.entrySet()) {
                System.out.println("    - vendorItemId: " + entry.getKey() + 
                                 " -> " + String.join(", ", entry.getValue()));
            }
        }
    }
    
    /**
     * 生成分析方案
     */
    private static void generateAnalysisPlan() {
        System.out.println("\n【四、HTML分析方案】");
        System.out.println("------------------------------------------");
        
        System.out.println("\n1. 正则表达式提取方案:");
        System.out.println("   a) 产品ID: data-product-id=\"(\\d+)\"");
        System.out.println("   b) 产品名称: <title>([^<]+)</title>");
        System.out.println("   c) vendorItemId: vendorItemId[=:](\\d+)");
        System.out.println("   d) 发货方式标识: data-badge-id=\"([^\"]+)\"");
        System.out.println("   e) 选项链接: href=\"[^\"]*vendorItemId=(\\d+)[^\"]*\"");
        
        System.out.println("\n2. 发货方式识别:");
        System.out.println("   - ROCKET: 火箭配送（쿠팡 직접 배송）");
        System.out.println("   - ROCKET_MERCHANT: 火箭商家配送（판매자 직접 배송）");
        System.out.println("   - TOMORROW: 次日达标识");
        
        System.out.println("\n3. 数据提取策略:");
        System.out.println("   a) 使用正则表达式提取所有vendorItemId");
        System.out.println("   b) 在每个vendorItemId的上下文中查找发货方式徽章");
        System.out.println("   c) 从URL参数中提取itemId和vendorItemId的关联");
        System.out.println("   d) 从HTML结构（class、data属性）中提取选项名称");
        
        System.out.println("\n4. 实现建议:");
        System.out.println("   a) 创建HtmlProductParser类，使用正则表达式提取数据");
        System.out.println("   b) 创建ProductOption实体类存储选项信息");
        System.out.println("   c) 建立vendorItemId与发货方式的映射关系");
        System.out.println("   d) 提供查询接口：根据vendorItemId查询发货方式");
        
        System.out.println("\n5. 注意事项:");
        System.out.println("   - HTML结构可能变化，需要定期更新正则表达式");
        System.out.println("   - 某些数据可能在JavaScript中动态加载");
        System.out.println("   - 建议结合JSON数据（如果存在）进行验证");
        System.out.println("   - 考虑使用HTML解析库（如Jsoup）作为补充");
        
        System.out.println("\n==========================================");
    }
    
    /**
     * 获取徽章描述
     */
    private static String getBadgeDescription(String badgeId) {
        switch (badgeId) {
            case "ROCKET":
                return "火箭配送";
            case "ROCKET_MERCHANT":
                return "火箭商家配送";
            case "TOMORROW":
                return "次日达";
            default:
                return "未知徽章";
        }
    }
    
    /**
     * 统计字符串出现次数
     */
    private static int countOccurrences(String text, String pattern) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(pattern, index)) != -1) {
            count++;
            index += pattern.length();
        }
        return count;
    }
}

