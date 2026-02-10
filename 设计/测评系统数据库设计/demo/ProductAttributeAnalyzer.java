package com.baotongit.webdriver.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.*;
import java.util.*;

/**
 * 产品属性分析工具
 * 分析产品的属性组合和对应的发货方式
 *
 * @author baotongit
 */
public class ProductAttributeAnalyzer {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public static void main(String[] args) {
        try {
            // 读取JSON文件
            Path jsonPath = Paths.get("src/main/resources/json.txt");
            if (!Files.exists(jsonPath)) {
                System.err.println("JSON文件不存在: " + jsonPath);
                return;
            }
            
            String content = new String(Files.readAllBytes(jsonPath), "UTF-8");
            
            // 提取JSON（去掉前缀）
            String jsonContent = extractJson(content);
            
            // 解析JSON
            JsonNode rootNode = objectMapper.readTree(jsonContent);
            
            // 分析产品属性
            analyzeProductAttributes(rootNode);
            
        } catch (Exception e) {
            System.err.println("分析失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 提取JSON内容
     */
    private static String extractJson(String content) {
        int jsonStart = -1;
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c == '{' || c == '[') {
                jsonStart = i;
                break;
            }
        }
        
        if (jsonStart == -1) {
            return content.trim();
        }
        
        char startChar = content.charAt(jsonStart);
        char endChar = (startChar == '{') ? '}' : ']';
        
        int depth = 0;
        int jsonEnd = -1;
        boolean inString = false;
        
        for (int i = jsonStart; i < content.length(); i++) {
            char c = content.charAt(i);
            
            if (c == '"' && (i == 0 || content.charAt(i - 1) != '\\')) {
                inString = !inString;
                continue;
            }
            
            if (!inString) {
                if (c == startChar) {
                    depth++;
                } else if (c == endChar) {
                    depth--;
                    if (depth == 0) {
                        jsonEnd = i + 1;
                        break;
                    }
                }
            }
        }
        
        if (jsonEnd == -1 || jsonEnd <= jsonStart) {
            return content.substring(jsonStart).trim();
        }
        
        return content.substring(jsonStart, jsonEnd).trim();
    }
    
    /**
     * 分析产品属性
     */
    private static void analyzeProductAttributes(JsonNode rootNode) {
        System.out.println("==========================================");
        System.out.println("产品属性与发货方式分析报告");
        System.out.println("==========================================\n");
        
        // 1. 提取产品基本信息
        extractProductBasicInfo(rootNode);
        
        // 2. 分析属性组合
        analyzeAttributeCombinations(rootNode);
        
        // 3. 分析发货方式
        analyzeDeliveryTypes(rootNode);
        
        // 4. 生成汇总报告
        generateSummaryReport(rootNode);
    }
    
    /**
     * 提取产品基本信息
     */
    private static void extractProductBasicInfo(JsonNode rootNode) {
        System.out.println("【一、产品基本信息】");
        System.out.println("------------------------------------------");
        
        if (rootNode.has("itemName")) {
            System.out.println("产品名称: " + rootNode.get("itemName").asText());
        }
        
        if (rootNode.has("itemId")) {
            System.out.println("商品ID: " + rootNode.get("itemId").asLong());
        }
        
        if (rootNode.has("vendorItemId")) {
            System.out.println("供应商商品ID: " + rootNode.get("vendorItemId").asLong());
        }
        
        if (rootNode.has("productId")) {
            System.out.println("产品ID: " + rootNode.get("productId").asLong());
        }
        
        System.out.println();
    }
    
    /**
     * 分析属性组合
     */
    private static void analyzeAttributeCombinations(JsonNode rootNode) {
        System.out.println("【二、产品属性分析】");
        System.out.println("------------------------------------------");
        
        // 从 moduleData 中查找选项表数据
        if (rootNode.has("quantityBase") && rootNode.get("quantityBase").isArray()) {
            JsonNode quantityBase = rootNode.get("quantityBase");
            if (quantityBase.size() > 0) {
                JsonNode firstQuantity = quantityBase.get(0);
                if (firstQuantity.has("moduleData") && firstQuantity.get("moduleData").isArray()) {
                    JsonNode moduleData = firstQuantity.get("moduleData");
                    
                    // 查找选项表数据
                    for (JsonNode module : moduleData) {
                        if (module.has("viewType")) {
                            String viewType = module.get("viewType").asText();
                            
                            // 选项表标签选择器
                            if ("PRODUCT_OPTION_TABLE_TAB_SELECTOR".equals(viewType)) {
                                analyzeTabSelector(module);
                            }
                            
                            // 选项表列表视图
                            if ("PRODUCT_OPTION_TABLE_LIST_VIEW".equals(viewType)) {
                                analyzeOptionList(module);
                            }
                            
                            // 选项列表
                            if ("PRODUCT_DETAIL_OPTION_LIST".equals(viewType)) {
                                analyzeDetailOptionList(module);
                            }
                        }
                    }
                }
            }
        }
        
        System.out.println();
    }
    
    /**
     * 分析标签选择器（属性分类）
     */
    private static void analyzeTabSelector(JsonNode tabSelector) {
        if (tabSelector.has("tabList") && tabSelector.get("tabList").isArray()) {
            JsonNode tabList = tabSelector.get("tabList");
            System.out.println("属性分类数量: " + tabList.size());
            
            for (JsonNode tab : tabList) {
                if (tab.has("attributeId")) {
                    String attributeId = tab.get("attributeId").asText();
                    System.out.println("  属性ID: " + attributeId);
                }
                
                if (tab.has("items") && tab.get("items").isArray()) {
                    JsonNode items = tab.get("items");
                    System.out.println("  该属性下的选项数量: " + items.size());
                    
                    for (JsonNode item : items) {
                        if (item.has("valueName")) {
                            System.out.println("    - " + item.get("valueName").asText());
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 分析选项表列表视图
     */
    private static void analyzeOptionList(JsonNode optionList) {
        if (optionList.has("optionList") && optionList.get("optionList").isArray()) {
            JsonNode options = optionList.get("optionList");
            System.out.println("\n选项表中共有 " + options.size() + " 个选项组合:");
            
            for (int i = 0; i < options.size(); i++) {
                JsonNode option = options.get(i);
                System.out.println("\n  选项 " + (i + 1) + ":");
                
                if (option.has("hoverSelectionText")) {
                    System.out.println("    属性组合: " + option.get("hoverSelectionText").asText());
                }
                
                if (option.has("deliveryType")) {
                    String deliveryType = option.get("deliveryType").asText();
                    System.out.println("    发货方式: " + deliveryType);
                    System.out.println("    发货方式说明: " + getDeliveryTypeDescription(deliveryType));
                }
                
                if (option.has("finalPrice")) {
                    System.out.println("    最终价格: " + option.get("finalPrice").asText());
                }
                
                if (option.has("vendorItemId")) {
                    System.out.println("    供应商商品ID: " + option.get("vendorItemId").asLong());
                }
                
                if (option.has("deliveryUnificationBadgeArea")) {
                    analyzeDeliveryBadge(option.get("deliveryUnificationBadgeArea"));
                }
            }
        }
    }
    
    /**
     * 分析详细选项列表
     */
    private static void analyzeDetailOptionList(JsonNode optionList) {
        if (optionList.has("items") && optionList.get("items").isArray()) {
            JsonNode items = optionList.get("items");
            System.out.println("\n详细选项列表中共有 " + items.size() + " 个选项:");
            
            Map<String, List<Map<String, Object>>> deliveryTypeMap = new LinkedHashMap<>();
            
            for (int i = 0; i < items.size(); i++) {
                JsonNode item = items.get(i);
                Map<String, Object> optionInfo = new LinkedHashMap<>();
                
                String itemName = "";
                if (item.has("itemBasicInfo")) {
                    JsonNode itemBasicInfo = item.get("itemBasicInfo");
                    if (itemBasicInfo.has("itemName")) {
                        itemName = itemBasicInfo.get("itemName").asText();
                        optionInfo.put("itemName", itemName);
                    }
                }
                
                // 提取发货方式（从 deliveryUnificationBadgeArea.badge.badgeList[0].badgeId 读取）
                String deliveryType = extractDeliveryType(item);
                if (deliveryType != null && !deliveryType.isEmpty()) {
                    optionInfo.put("deliveryType", deliveryType);
                }
                
                if (item.has("priceInfo")) {
                    JsonNode priceInfo = item.get("priceInfo");
                    if (priceInfo.has("finalPrice")) {
                        optionInfo.put("finalPrice", priceInfo.get("finalPrice").asText());
                    }
                }
                
                if (item.has("itemBasicInfo")) {
                    JsonNode itemBasicInfo = item.get("itemBasicInfo");
                    if (itemBasicInfo.has("vendorItemId")) {
                        optionInfo.put("vendorItemId", itemBasicInfo.get("vendorItemId").asLong());
                    }
                }
                
                // 按发货方式分组
                if (!deliveryType.isEmpty()) {
                    deliveryTypeMap.computeIfAbsent(deliveryType, k -> new ArrayList<>()).add(optionInfo);
                }
                
                System.out.println("\n  选项 " + (i + 1) + ":");
                System.out.println("    商品名称: " + itemName);
                System.out.println("    发货方式: " + deliveryType + " (" + getDeliveryTypeDescription(deliveryType) + ")");
                if (optionInfo.containsKey("finalPrice")) {
                    System.out.println("    价格: " + optionInfo.get("finalPrice"));
                }
            }
            
            // 按发货方式汇总
            System.out.println("\n【按发货方式汇总】");
            for (Map.Entry<String, List<Map<String, Object>>> entry : deliveryTypeMap.entrySet()) {
                System.out.println("\n  " + entry.getKey() + " (" + getDeliveryTypeDescription(entry.getKey()) + "):");
                System.out.println("    选项数量: " + entry.getValue().size());
                for (Map<String, Object> option : entry.getValue()) {
                    System.out.println("      - " + option.get("itemName") + " | " + option.get("finalPrice"));
                }
            }
        }
    }
    
    /**
     * 分析配送徽章
     */
    private static void analyzeDeliveryBadge(JsonNode badgeArea) {
        if (badgeArea.has("badge") && badgeArea.get("badge").has("badgeList")) {
            JsonNode badgeList = badgeArea.get("badge").get("badgeList");
            if (badgeList.isArray() && badgeList.size() > 0) {
                System.out.print("    配送徽章: ");
                for (int i = 0; i < badgeList.size(); i++) {
                    JsonNode badge = badgeList.get(i);
                    if (badge.has("badgeId")) {
                        if (i > 0) System.out.print(" + ");
                        System.out.print(badge.get("badgeId").asText());
                    }
                }
                System.out.println();
            }
        }
    }
    
    /**
     * 分析发货方式
     */
    private static void analyzeDeliveryTypes(JsonNode rootNode) {
        System.out.println("\n【三、发货方式详细分析】");
        System.out.println("------------------------------------------");
        
        if (rootNode.has("delivery")) {
            JsonNode delivery = rootNode.get("delivery");
            
            if (delivery.has("type")) {
                System.out.println("主要发货类型: " + delivery.get("type").asText());
            }
            
            if (delivery.has("descriptions")) {
                System.out.println("配送描述: " + delivery.get("descriptions").asText().replaceAll("<[^>]+>", ""));
            }
            
            if (delivery.has("badgeData")) {
                JsonNode badgeData = delivery.get("badgeData");
                if (badgeData.has("deliveryUnificationBadgeArea")) {
                    analyzeDeliveryBadge(badgeData.get("deliveryUnificationBadgeArea"));
                }
            }
        }
        
        // 分析配送列表
        if (rootNode.has("deliveryList") && rootNode.get("deliveryList").isArray()) {
            JsonNode deliveryList = rootNode.get("deliveryList");
            System.out.println("\n配送方式列表 (" + deliveryList.size() + " 种):");
            
            for (int i = 0; i < deliveryList.size(); i++) {
                JsonNode delivery = deliveryList.get(i);
                System.out.println("\n  配送方式 " + (i + 1) + ":");
                
                if (delivery.has("type")) {
                    System.out.println("    类型: " + delivery.get("type").asText());
                }
                
                if (delivery.has("descriptions")) {
                    String desc = delivery.get("descriptions").asText().replaceAll("<[^>]+>", "");
                    System.out.println("    描述: " + desc);
                }
            }
        }
        
        System.out.println();
    }
    
    /**
     * 生成汇总报告
     */
    private static void generateSummaryReport(JsonNode rootNode) {
        System.out.println("【四、分析方案与建议】");
        System.out.println("------------------------------------------");
        
        System.out.println("\n1. 属性识别方案:");
        System.out.println("   - 从 moduleData 中查找 PRODUCT_OPTION_TABLE_TAB_SELECTOR 获取属性分类");
        System.out.println("   - 从 PRODUCT_OPTION_TABLE_LIST_VIEW 或 PRODUCT_DETAIL_OPTION_LIST 获取具体选项");
        System.out.println("   - 每个选项包含: 属性组合、价格、vendorItemId、发货方式");
        
        System.out.println("\n2. 发货方式识别方案:");
        System.out.println("   - 检查每个选项的 deliveryType 字段");
        System.out.println("   - 常见类型: ROCKET(火箭配送), ROCKET_MERCHANT(火箭商家配送)");
        System.out.println("   - 通过 deliveryUnificationBadgeArea 获取配送徽章信息");
        
        System.out.println("\n3. 数据提取建议:");
        System.out.println("   - 使用 vendorItemId 作为唯一标识");
        System.out.println("   - 记录属性组合（如：颜色×数量）");
        System.out.println("   - 记录对应的发货方式和价格");
        System.out.println("   - 注意区分不同发货方式的配送时效和费用");
        
        System.out.println("\n4. 实现建议:");
        System.out.println("   - 创建 ProductAttribute 类存储属性信息");
        System.out.println("   - 创建 DeliveryInfo 类存储发货方式信息");
        System.out.println("   - 建立属性组合与发货方式的映射关系");
        System.out.println("   - 提供查询接口：根据属性组合查询发货方式");
        
        System.out.println("\n==========================================");
    }
    
    /**
     * 从 deliveryInfo 中提取配送方式
     * 从 deliveryUnificationBadgeArea.badge.badgeList[0].badgeId 读取
     *
     * @param item 选项节点
     * @return 配送方式，如果不存在则返回空字符串
     */
    private static String extractDeliveryType(JsonNode item) {
        if (!item.has("deliveryInfo")) {
            return "";
        }
        
        JsonNode deliveryInfo = item.get("deliveryInfo");
        if (!deliveryInfo.has("deliveryUnificationBadgeArea")) {
            return "";
        }
        
        JsonNode badgeArea = deliveryInfo.get("deliveryUnificationBadgeArea");
        if (!badgeArea.has("badge")) {
            return "";
        }
        
        JsonNode badge = badgeArea.get("badge");
        if (!badge.has("badgeList") || !badge.get("badgeList").isArray()) {
            return "";
        }
        
        JsonNode badgeList = badge.get("badgeList");
        if (badgeList.size() == 0) {
            return "";
        }
        
        // 读取第一个 badge 的 badgeId
        JsonNode firstBadge = badgeList.get(0);
        if (firstBadge.has("badgeId")) {
            return firstBadge.get("badgeId").asText();
        }
        
        return "";
    }
    
    /**
     * 获取发货方式描述
     */
    private static String getDeliveryTypeDescription(String deliveryType) {
        switch (deliveryType) {
            case "ROCKET":
                return "火箭配送（쿠팡 직접 배송）";
            case "ROCKET_MERCHANT":
                return "火箭商家配送（판매자 직접 배송）";
            case "NORMAL":
                return "一般配送";
            case "FREE":
                return "免费配送";
            default:
                return "未知配送方式";
        }
    }
}

