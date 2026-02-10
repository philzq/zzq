package com.baotongit.webdriver.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

/**
 * 产品属性分析报告生成器
 * 直接分析JSON字符串并生成报告
 *
 * @author baotongit
 */
public class ProductAttributeAnalysisReport {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 分析产品JSON数据
     */
    public static String analyzeProductJson(String jsonString) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonString);
            return generateAnalysisReport(rootNode);
        } catch (Exception e) {
            return "分析失败: " + e.getMessage();
        }
    }
    
    /**
     * 生成分析报告
     */
    private static String generateAnalysisReport(JsonNode rootNode) {
        StringBuilder report = new StringBuilder();
        
        report.append("==========================================\n");
        report.append("产品属性与发货方式分析报告\n");
        report.append("==========================================\n\n");
        
        // 1. 产品基本信息
        report.append("【一、产品基本信息】\n");
        report.append("------------------------------------------\n");
        if (rootNode.has("itemName")) {
            report.append("产品名称: ").append(rootNode.get("itemName").asText()).append("\n");
        }
        if (rootNode.has("itemId")) {
            report.append("商品ID: ").append(rootNode.get("itemId").asLong()).append("\n");
        }
        if (rootNode.has("vendorItemId")) {
            report.append("供应商商品ID: ").append(rootNode.get("vendorItemId").asLong()).append("\n");
        }
        report.append("\n");
        
        // 2. 属性分析
        report.append("【二、产品属性分析】\n");
        report.append("------------------------------------------\n");
        
        // 查找选项列表数据
        List<Map<String, Object>> allOptions = new ArrayList<>();
        
        // 从 quantityBase.moduleData 中查找
        if (rootNode.has("quantityBase") && rootNode.get("quantityBase").isArray()) {
            JsonNode quantityBase = rootNode.get("quantityBase");
            if (quantityBase.size() > 0) {
                JsonNode firstQuantity = quantityBase.get(0);
                if (firstQuantity.has("moduleData") && firstQuantity.get("moduleData").isArray()) {
                    JsonNode moduleData = firstQuantity.get("moduleData");
                    
                    for (JsonNode module : moduleData) {
                        if (module.has("viewType")) {
                            String viewType = module.get("viewType").asText();
                            
                            // 详细选项列表
                            if ("PRODUCT_DETAIL_OPTION_LIST".equals(viewType)) {
                                extractOptionsFromDetailList(module, allOptions, report);
                            }
                            
                            // 选项表列表视图
                            if ("PRODUCT_OPTION_TABLE_LIST_VIEW".equals(viewType)) {
                                extractOptionsFromTableView(module, allOptions, report);
                            }
                        }
                    }
                }
            }
        }
        
        // 3. 发货方式汇总
        report.append("\n【三、发货方式汇总】\n");
        report.append("------------------------------------------\n");
        
        Map<String, List<Map<String, Object>>> deliveryTypeMap = new LinkedHashMap<>();
        for (Map<String, Object> option : allOptions) {
            String deliveryType = (String) option.get("deliveryType");
            if (deliveryType != null && !deliveryType.isEmpty()) {
                deliveryTypeMap.computeIfAbsent(deliveryType, k -> new ArrayList<>()).add(option);
            }
        }
        
        for (Map.Entry<String, List<Map<String, Object>>> entry : deliveryTypeMap.entrySet()) {
            String deliveryType = entry.getKey();
            List<Map<String, Object>> options = entry.getValue();
            
            report.append("\n发货方式: ").append(deliveryType);
            report.append(" (").append(getDeliveryTypeDescription(deliveryType)).append(")\n");
            report.append("  选项数量: ").append(options.size()).append("\n");
            report.append("  具体选项:\n");
            
            for (Map<String, Object> option : options) {
                report.append("    - ").append(option.get("itemName"));
                if (option.containsKey("finalPrice")) {
                    report.append(" | 价格: ").append(option.get("finalPrice"));
                }
                if (option.containsKey("vendorItemId")) {
                    report.append(" | vendorItemId: ").append(option.get("vendorItemId"));
                }
                report.append("\n");
            }
        }
        
        // 4. 分析方案
        report.append("\n【四、分析方案】\n");
        report.append("------------------------------------------\n");
        report.append("\n1. 数据结构说明:\n");
        report.append("   - 产品属性存储在 quantityBase[0].moduleData 中\n");
        report.append("   - PRODUCT_DETAIL_OPTION_LIST 包含所有选项的详细信息\n");
        report.append("   - PRODUCT_OPTION_TABLE_LIST_VIEW 包含选项表的视图数据\n");
        report.append("   - 每个选项包含: itemName, deliveryType, finalPrice, vendorItemId\n");
        
        report.append("\n2. 属性提取方案:\n");
        report.append("   a) 遍历 quantityBase[0].moduleData 数组\n");
        report.append("   b) 查找 viewType 为 'PRODUCT_DETAIL_OPTION_LIST' 的模块\n");
        report.append("   c) 提取 items 数组中的每个选项\n");
        report.append("   d) 从 itemBasicInfo 获取 itemName 和 vendorItemId\n");
        report.append("   e) 从 deliveryInfo 获取 deliveryType\n");
        report.append("   f) 从 priceInfo 获取 finalPrice\n");
        
        report.append("\n3. 发货方式识别方案:\n");
        report.append("   - deliveryType 字段值:\n");
        report.append("     * ROCKET: 火箭配送（쿠팡 직접 배송）\n");
        report.append("     * ROCKET_MERCHANT: 火箭商家配送（판매자 직접 배송）\n");
        report.append("   - 可通过 deliveryUnificationBadgeArea 获取配送徽章信息\n");
        report.append("   - 通过 deliveryInfo.descriptions 获取配送描述\n");
        
        report.append("\n4. 实现建议:\n");
        report.append("   a) 创建 ProductOption 实体类:\n");
        report.append("      - itemName: String (属性组合，如 '레인보우 × 1세트')\n");
        report.append("      - vendorItemId: Long (供应商商品ID)\n");
        report.append("      - deliveryType: String (发货方式)\n");
        report.append("      - finalPrice: String (最终价格)\n");
        report.append("      - itemId: Long (商品ID)\n");
        
        report.append("\n   b) 创建 DeliveryType 枚举:\n");
        report.append("      - ROCKET(\"火箭配送\")\n");
        report.append("      - ROCKET_MERCHANT(\"火箭商家配送\")\n");
        
        report.append("\n   c) 创建 ProductAttributeService:\n");
        report.append("      - parseProductOptions(JsonNode): List<ProductOption>\n");
        report.append("      - getOptionsByDeliveryType(String): List<ProductOption>\n");
        report.append("      - getDeliveryTypeByVendorItemId(Long): String\n");
        
        report.append("\n5. 代码示例:\n");
        report.append("   ```java\n");
        report.append("   // 提取所有选项\n");
        report.append("   JsonNode moduleData = rootNode.path(\"quantityBase\").get(0)\n");
        report.append("       .path(\"moduleData\");\n");
        report.append("   \n");
        report.append("   for (JsonNode module : moduleData) {\n");
        report.append("       if (\"PRODUCT_DETAIL_OPTION_LIST\".equals(\n");
        report.append("           module.path(\"viewType\").asText())) {\n");
        report.append("           JsonNode items = module.path(\"items\");\n");
        report.append("           // 处理每个选项...\n");
        report.append("       }\n");
        report.append("   }\n");
        report.append("   ```\n");
        
        report.append("\n==========================================\n");
        
        return report.toString();
    }
    
    /**
     * 从详细选项列表中提取选项
     */
    private static void extractOptionsFromDetailList(JsonNode module, 
                                                     List<Map<String, Object>> allOptions,
                                                     StringBuilder report) {
        if (module.has("items") && module.get("items").isArray()) {
            JsonNode items = module.get("items");
            report.append("\n找到 ").append(items.size()).append(" 个选项:\n");
            
            for (int i = 0; i < items.size(); i++) {
                JsonNode item = items.get(i);
                Map<String, Object> option = new LinkedHashMap<>();
                
                // 提取商品名称
                if (item.has("itemBasicInfo")) {
                    JsonNode itemBasicInfo = item.get("itemBasicInfo");
                    if (itemBasicInfo.has("itemName")) {
                        String itemName = itemBasicInfo.get("itemName").asText();
                        option.put("itemName", itemName);
                        report.append("  ").append(i + 1).append(". ").append(itemName).append("\n");
                    }
                    if (itemBasicInfo.has("vendorItemId")) {
                        option.put("vendorItemId", itemBasicInfo.get("vendorItemId").asLong());
                    }
                    if (itemBasicInfo.has("itemId")) {
                        option.put("itemId", itemBasicInfo.get("itemId").asLong());
                    }
                }
                
                // 提取发货方式（从 deliveryUnificationBadgeArea.badge.badgeList[0].badgeId 读取）
                String deliveryType = extractDeliveryType(item);
                if (deliveryType != null && !deliveryType.isEmpty()) {
                    option.put("deliveryType", deliveryType);
                    report.append("     发货方式: ").append(deliveryType)
                          .append(" (").append(getDeliveryTypeDescription(deliveryType)).append(")\n");
                }
                
                // 提取价格
                if (item.has("priceInfo")) {
                    JsonNode priceInfo = item.get("priceInfo");
                    if (priceInfo.has("finalPrice")) {
                        String finalPrice = priceInfo.get("finalPrice").asText();
                        option.put("finalPrice", finalPrice);
                        report.append("     价格: ").append(finalPrice).append("\n");
                    }
                }
                
                allOptions.add(option);
            }
        }
    }
    
    /**
     * 从选项表视图中提取选项
     */
    private static void extractOptionsFromTableView(JsonNode module,
                                                    List<Map<String, Object>> allOptions,
                                                    StringBuilder report) {
        if (module.has("optionList") && module.get("optionList").isArray()) {
            JsonNode optionList = module.get("optionList");
            report.append("\n选项表中有 ").append(optionList.size()).append(" 个选项组合:\n");
            
            for (int i = 0; i < optionList.size(); i++) {
                JsonNode option = optionList.get(i);
                Map<String, Object> optionMap = new LinkedHashMap<>();
                
                if (option.has("hoverSelectionText")) {
                    String selectionText = option.get("hoverSelectionText").asText();
                    optionMap.put("itemName", selectionText);
                    report.append("  ").append(i + 1).append(". ").append(selectionText).append("\n");
                }
                
                // 提取发货方式（从 deliveryUnificationBadgeArea.badge.badgeList[0].badgeId 读取）
                String deliveryType = extractDeliveryTypeFromOption(option);
                if (deliveryType != null && !deliveryType.isEmpty()) {
                    optionMap.put("deliveryType", deliveryType);
                    report.append("     发货方式: ").append(deliveryType)
                          .append(" (").append(getDeliveryTypeDescription(deliveryType)).append(")\n");
                }
                
                if (option.has("finalPrice")) {
                    String finalPrice = option.get("finalPrice").asText();
                    optionMap.put("finalPrice", finalPrice);
                    report.append("     价格: ").append(finalPrice).append("\n");
                }
                
                if (option.has("vendorItemId")) {
                    optionMap.put("vendorItemId", option.get("vendorItemId").asLong());
                }
                
                allOptions.add(optionMap);
            }
        }
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
     * 从选项表视图的选项节点中提取配送方式
     * 从 deliveryUnificationBadgeArea.badge.badgeList[0].badgeId 读取
     *
     * @param option 选项表视图中的选项节点
     * @return 配送方式，如果不存在则返回空字符串
     */
    private static String extractDeliveryTypeFromOption(JsonNode option) {
        if (!option.has("deliveryUnificationBadgeArea")) {
            return "";
        }
        
        JsonNode badgeArea = option.get("deliveryUnificationBadgeArea");
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

