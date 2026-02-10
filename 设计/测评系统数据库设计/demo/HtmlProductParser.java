package com.baotongit.webdriver.utils;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

/**
 * HTML产品解析器
 * 使用正则表达式从HTML中提取完整的产品选项和发货方式信息
 *
 * @author baotongit
 */
public class HtmlProductParser {
    
    /**
     * 产品选项信息
     */
    public static class ProductOption {
        private String itemName;
        private String vendorItemId;
        private String itemId;
        private String deliveryType;
        private String price;
        private List<String> badges;
        
        public ProductOption() {
            this.badges = new ArrayList<>();
        }
        
        // Getters and Setters
        public String getItemName() { return itemName; }
        public void setItemName(String itemName) { this.itemName = itemName; }
        
        public String getVendorItemId() { return vendorItemId; }
        public void setVendorItemId(String vendorItemId) { this.vendorItemId = vendorItemId; }
        
        public String getItemId() { return itemId; }
        public void setItemId(String itemId) { this.itemId = itemId; }
        
        public String getDeliveryType() { return deliveryType; }
        public void setDeliveryType(String deliveryType) { this.deliveryType = deliveryType; }
        
        public String getPrice() { return price; }
        public void setPrice(String price) { this.price = price; }
        
        public List<String> getBadges() { return badges; }
        public void setBadges(List<String> badges) { this.badges = badges; }
        
        @Override
        public String toString() {
            return String.format("ProductOption{itemName='%s', vendorItemId='%s', deliveryType='%s', price='%s', badges=%s}",
                    itemName, vendorItemId, deliveryType, price, badges);
        }
    }

    /**
     * 对应 option_list / PRODUCT_DETAIL_OPTION_LIST 中的 items 元素（你贴出的 JSON 数组结构）。
     * 这里只建我们需要的字段，其他字段 Jackson 会自动忽略。
     */
    public static class OptionListItem {
        private DeliveryInfo deliveryInfo;
        private ItemBasicInfo itemBasicInfo;
        private PriceInfo priceInfo;

        public DeliveryInfo getDeliveryInfo() { return deliveryInfo; }
        public void setDeliveryInfo(DeliveryInfo deliveryInfo) { this.deliveryInfo = deliveryInfo; }

        public ItemBasicInfo getItemBasicInfo() { return itemBasicInfo; }
        public void setItemBasicInfo(ItemBasicInfo itemBasicInfo) { this.itemBasicInfo = itemBasicInfo; }

        public PriceInfo getPriceInfo() { return priceInfo; }
        public void setPriceInfo(PriceInfo priceInfo) { this.priceInfo = priceInfo; }
    }

    public static class ItemBasicInfo {
        private long itemId;
        private long vendorItemId;
        private String itemName;

        public long getItemId() { return itemId; }
        public void setItemId(long itemId) { this.itemId = itemId; }

        public long getVendorItemId() { return vendorItemId; }
        public void setVendorItemId(long vendorItemId) { this.vendorItemId = vendorItemId; }

        public String getItemName() { return itemName; }
        public void setItemName(String itemName) { this.itemName = itemName; }
    }

    public static class PriceInfo {
        private String finalPrice;

        public String getFinalPrice() { return finalPrice; }
        public void setFinalPrice(String finalPrice) { this.finalPrice = finalPrice; }
    }

    public static class DeliveryInfo {
        private DeliveryUnificationBadgeArea deliveryUnificationBadgeArea;

        public DeliveryUnificationBadgeArea getDeliveryUnificationBadgeArea() {
            return deliveryUnificationBadgeArea;
        }

        public void setDeliveryUnificationBadgeArea(DeliveryUnificationBadgeArea deliveryUnificationBadgeArea) {
            this.deliveryUnificationBadgeArea = deliveryUnificationBadgeArea;
        }
    }

    public static class DeliveryUnificationBadgeArea {
        private Badge badge;

        public Badge getBadge() { return badge; }
        public void setBadge(Badge badge) { this.badge = badge; }
    }

    public static class Badge {
        private List<BadgeEntry> badgeList;

        public List<BadgeEntry> getBadgeList() { return badgeList; }
        public void setBadgeList(List<BadgeEntry> badgeList) { this.badgeList = badgeList; }
    }

    public static class BadgeEntry {
        private String badgeId;

        public String getBadgeId() { return badgeId; }
        public void setBadgeId(String badgeId) { this.badgeId = badgeId; }
    }
    
    /**
     * 解析HTML文件
     */
    public static List<ProductOption> parseHtmlFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        String html = new String(Files.readAllBytes(path), "UTF-8");
        return parseHtml(html);
    }
    
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    /**
     * 从 JavaScript 数据解析选项（优先）
     * 数据来源：script type="application/ld+json" 中的 Product；self.__next_f.push 中的 urlQuery。
     */
    public static List<ProductOption> parseHtmlFromJavaScriptData(String html) {
        List<ProductOption> options = new ArrayList<>();
        Map<String, ProductOption> byVendorItemId = new LinkedHashMap<>();

        // 1. 从 application/ld+json 的 Product 中解析
        Pattern scriptPattern = Pattern.compile(
            "<script[^>]*type\\s*=\\s*[\"']application/ld\\+json[\"'][^>]*>\\s*(\\{[^<]+?})\\s*</script>",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
        );
        Matcher scriptMatcher = scriptPattern.matcher(html);
        while (scriptMatcher.find()) {
            String jsonStr = scriptMatcher.group(1).trim();
            if (!jsonStr.contains("\"@type\":\"Product\"") && !jsonStr.contains("\"@type\": \"Product\"")) continue;
            try {
                JsonNode root = JSON_MAPPER.readTree(jsonStr);
                if (root.has("@type") && "Product".equals(root.path("@type").asText(null))) {
                    ProductOption opt = new ProductOption();
                    opt.setItemName(root.has("name") ? root.get("name").asText("") : null);
                    String sku = root.has("sku") ? root.get("sku").asText("") : "";
                    if (root.has("offers") && root.get("offers").isObject()) {
                        JsonNode offers = root.get("offers");
                        if (offers.has("price")) opt.setPrice(offers.get("price").asText("") + "원");
                        if (offers.has("url")) {
                            String url = offers.get("url").asText("");
                            Matcher mid = Pattern.compile("itemId=([0-9]+)").matcher(url);
                            if (mid.find()) opt.setItemId(mid.group(1));
                            Matcher mvid = Pattern.compile("vendorItemId=([0-9]+)").matcher(url);
                            if (mvid.find()) opt.setVendorItemId(mvid.group(1));
                        }
                    }
                    if (sku != null && sku.contains("-") && opt.getItemId() == null) {
                        String[] parts = sku.split("-");
                        if (parts.length >= 2) opt.setItemId(parts[1]);
                    }
                    if (opt.getVendorItemId() != null) {
                        byVendorItemId.putIfAbsent(opt.getVendorItemId(), opt);
                    } else {
                        options.add(opt);
                    }
                }
            } catch (Exception ignored) { }
        }
        options.addAll(byVendorItemId.values());

        // 2. 从 __next_f 内嵌的 urlQuery 补充 itemId/vendorItemId（整段 HTML 上固定格式匹配，避免超大 payload 导致 StackOverflow）
        Set<String> seenIds = new LinkedHashSet<>(byVendorItemId.keySet());
        Pattern urlQueryEscaped = Pattern.compile("\\\\\"itemId\\\\\":\\\\\"(\\d+)\\\\\",\\\\\"vendorItemId\\\\\":\\\\\"(\\d+)\\\\\"");
        Pattern urlQueryPlain = Pattern.compile("\"itemId\"\\s*:\\s*\"(\\d+)\"\\s*,\\s*\"vendorItemId\"\\s*:\\s*\"(\\d+)\"");
        for (Pattern p : new Pattern[]{urlQueryEscaped, urlQueryPlain}) {
            Matcher matcher = p.matcher(html);
            while (matcher.find()) {
                String itemId = matcher.group(1);
                String vendorItemId = matcher.group(2);
                if (seenIds.add(vendorItemId)) {
                    ProductOption opt = new ProductOption();
                    opt.setItemId(itemId);
                    opt.setVendorItemId(vendorItemId);
                    options.add(opt);
                }
            }
        }

        return options;
    }

    /**
     * 使用正则定位 \"optionList\" 数组，提取数组字符串后解析为选项数据。
     * 数据来源：HTML/JS 中 \"optionList\":[{...},{...}] 或 "optionList":[{...}]。
     *
     * @param html 完整 HTML 内容
     * @return 解析出的选项列表，未找到或解析失败返回空列表
     */
    public static List<ProductOption> parseHtmlFromOptionList(String html) {
        String arrayStr = extractOptionListArrayString(html);
        if (arrayStr == null || arrayStr.isEmpty()) {
            return new ArrayList<>();
        }
        return parseOptionListArray(arrayStr);
    }

    /**
     * 用正则找到 \"optionList\":[ 或 "optionList":[ 的起始位置，再按括号匹配提取整个数组字符串（含 [ ]）。
     * 提取出的字符串中 \" 需反转义为 " 后才可作为 JSON 解析。
     */
    public static String extractOptionListArrayString(String html) {
        return extractJsonArrayStringByKey(html, "optionList");
    }

    /**
     * 提取 tabList 数组 JSON 字符串（含 [ ]，必要时已做 \\\" -> \" 反转义）。
     */
    public static String extractTabListArrayString(String html) {
        return extractJsonArrayStringByKey(html, "tabList");
    }

    /**
     * 从 tabList 中找到 attributeId 对应的 items 数组，返回 items 的 JSON 数组字符串（含 [ ]）。
     *
     * 你给的定位特征类似：\"tabList\":[{\"attributeId\":\"2439\",\"items\": [...] }]
     */
    public static String extractItemsArrayStringFromTabList(String html, String attributeId) {
        String tabListJson = extractTabListArrayString(html);
        if (tabListJson == null || tabListJson.isEmpty()) return null;
        try {
            JsonNode tabList = JSON_MAPPER.readTree(tabListJson);
            if (tabList == null || !tabList.isArray()) return null;
            for (JsonNode tab : tabList) {
                if (!tab.has("attributeId")) continue;
                if (!attributeId.equals(tab.get("attributeId").asText())) continue;
                JsonNode items = tab.get("items");
                if (items != null && items.isArray()) {
                    return items.toString();
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 解析 tabList[attributeId].items 数组为选项数据（通常是 valueName 列表）。\n
     * 注意：tabList.items 一般不直接带价格/发货方式；价格/发货方式通常在 optionList 或 items(详细选项列表) 中。
     */
    public static List<ProductOption> parseHtmlFromTabListItems(String html, String attributeId) {
        String itemsJson = extractItemsArrayStringFromTabList(html, attributeId);
        if (itemsJson == null || itemsJson.isEmpty()) {
            return new ArrayList<>();
        }
        List<ProductOption> options = new ArrayList<>();
        try {
            JsonNode items = JSON_MAPPER.readTree(itemsJson);
            if (items == null || !items.isArray()) return options;
            for (JsonNode item : items) {
                ProductOption po = new ProductOption();
                if (item.has("valueName")) {
                    po.setItemName(item.get("valueName").asText(""));
                }
                options.add(po);
            }
        } catch (Exception ignored) {
        }
        return options;
    }

    /**
     * 不关注 attributeId：解析 tabList 下所有 tab 的 items（合并后返回）。
     * 适用于你说的“attributeId 中间用 .*? 跳过”的场景。
     */
    public static List<ProductOption> parseHtmlFromTabListItems(String html) {
        String tabListJson = extractTabListArrayString(html);
        if (tabListJson == null || tabListJson.isEmpty()) {
            return new ArrayList<>();
        }
        List<ProductOption> options = new ArrayList<>();
        Set<String> seen = new LinkedHashSet<>();
        try {
            JsonNode tabList = JSON_MAPPER.readTree(tabListJson);
            if (tabList == null || !tabList.isArray()) return options;
            for (JsonNode tab : tabList) {
                JsonNode items = tab.get("items");
                if (items == null || !items.isArray()) continue;
                for (JsonNode item : items) {
                    if (!item.has("valueName")) continue;
                    String name = item.get("valueName").asText("");
                    if (name == null || name.isEmpty()) continue;
                    if (seen.add(name)) {
                        ProductOption po = new ProductOption();
                        po.setItemName(name);
                        options.add(po);
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return options;
    }

    /**
     * 不关注 tabList：直接从整段 HTML/JS 中提取 "items":[...]（或 \\\"items\\\":[...])。
     *
     * 约定：按出现顺序提取所有 "items" 数组后，只使用 arrays[1]（第二个），
     *      即 PRODUCT_DETAIL_OPTION_LIST / option_list 那一块的 items。
     *
     * 解析策略：直接将 arrays[1] 反序列化为 List<OptionListItem>，再映射到 ProductOption。
     */
    public static List<ProductOption> parseHtmlFromItemsArrays(String html) {
        List<String> arrays = extractAllJsonArrayStringsByKey(html, "items");
        // 只处理第二个 "items"（arrays[1]）
        if (arrays.size() <= 1) {
            return new ArrayList<>();
        }

        String arrayJson = arrays.get(1);
        if (arrayJson == null || arrayJson.isEmpty()) {
            return new ArrayList<>();
        }

        List<ProductOption> options = new ArrayList<>();
        try {
            // 使用 fastjson2 直接反序列化为实体列表
            List<OptionListItem> items = JSON.parseArray(arrayJson, OptionListItem.class);
            if (items == null || items.isEmpty()) {
                return options;
            }

            for (OptionListItem item : items) {
                if (item.getItemBasicInfo() == null) continue;

                ItemBasicInfo basic = item.getItemBasicInfo();
                ProductOption po = new ProductOption();
                po.setItemName(basic.getItemName());
                po.setVendorItemId(String.valueOf(basic.getVendorItemId()));
                po.setItemId(String.valueOf(basic.getItemId()));

                if (item.getPriceInfo() != null) {
                    po.setPrice(item.getPriceInfo().getFinalPrice());
                }

                if (item.getDeliveryInfo() != null &&
                    item.getDeliveryInfo().getDeliveryUnificationBadgeArea() != null &&
                    item.getDeliveryInfo().getDeliveryUnificationBadgeArea().getBadge() != null &&
                    item.getDeliveryInfo().getDeliveryUnificationBadgeArea().getBadge().getBadgeList() != null &&
                    !item.getDeliveryInfo().getDeliveryUnificationBadgeArea().getBadge().getBadgeList().isEmpty()) {

                    BadgeEntry first = item.getDeliveryInfo()
                            .getDeliveryUnificationBadgeArea()
                            .getBadge()
                            .getBadgeList()
                            .get(0);
                    if (first != null && first.getBadgeId() != null) {
                        po.setDeliveryType(first.getBadgeId());
                    }
                }

                options.add(po);
            }
        } catch (Exception ignored) {
        }

        return options;
    }

    /**
     * 通用：用正则定位 key 的数组起始，再按括号匹配提取整个数组字符串（含 [ ]）。
     * 支持两种形式：\\\"key\\\": [ ... ]（嵌入 JS 字符串，整体转义）与 \"key\": [ ... ]（正常 JSON）。
     *
     * @return 可直接 JSON 解析的数组字符串（已做必要反转义），未找到返回 null
     */
    private static String extractJsonArrayStringByKey(String html, String key) {
        Pattern startPattern = Pattern.compile("(?:\\\\\\\"" + Pattern.quote(key) + "\\\\\\\"|\\\"" + Pattern.quote(key) + "\\\")\\s*:\\s*\\[");
        Matcher m = startPattern.matcher(html);
        /*int count = 0;
        while (m.find()) {
            count++;
            // 这里可以获取每次匹配的信息
            System.out.println("第" + count + "次匹配位置: " + m.start() + "-" + m.end());
        }
        System.out.println("总共匹配了 " + count + " 个");*/
        if (!m.find()) return null;

        boolean escapedMode = html.charAt(m.start()) == '\\';
        int arrayStart = m.end() - 1;
        int depth = 1;
        boolean inString = false;
        int endIndex = -1;

        if (escapedMode) {
            // JSON 在 JS 字符串里：字符串边界是 \\\"（即反斜杠+引号）
            for (int i = arrayStart + 1; i < html.length(); ) {
                char c = html.charAt(i);
                if (c == '\\' && i + 1 < html.length()) {
                    if (html.charAt(i + 1) == '"') {
                        inString = !inString;
                    }
                    i += 2;
                    continue;
                }
                if (inString) {
                    i++;
                    continue;
                }
                if (c == '[') {
                    depth++;
                    i++;
                    continue;
                }
                if (c == ']') {
                    depth--;
                    if (depth == 0) {
                        endIndex = i;
                        break;
                    }
                    i++;
                    continue;
                }
                i++;
            }
        } else {
            // 正常 JSON：字符串边界是 "，字符串内转义用 \\
            for (int i = arrayStart + 1; i < html.length(); ) {
                char c = html.charAt(i);
                if (inString) {
                    if (c == '\\' && i + 1 < html.length()) {
                        i += 2;
                        continue;
                    }
                    if (c == '"') {
                        inString = false;
                        i++;
                        continue;
                    }
                    i++;
                    continue;
                }
                if (c == '"') {
                    inString = true;
                    i++;
                    continue;
                }
                if (c == '[') {
                    depth++;
                    i++;
                    continue;
                }
                if (c == ']') {
                    depth--;
                    if (depth == 0) {
                        endIndex = i;
                        break;
                    }
                    i++;
                    continue;
                }
                i++;
            }
        }

        if (endIndex < 0) return null;
        String raw = html.substring(arrayStart, endIndex + 1);
        return escapedMode ? raw.replace("\\\"", "\"") : raw;
    }

    /**
     * 提取整段 HTML/JS 中所有指定 key 的数组字符串（含 [ ]）。
     * 这是为 key="items" 这种多次出现的场景准备的。
     */
    private static List<String> extractAllJsonArrayStringsByKey(String html, String key) {
        List<String> result = new ArrayList<>();
        Pattern startPattern = Pattern.compile("(?:\\\\\\\"" + Pattern.quote(key) + "\\\\\\\"|\\\"" + Pattern.quote(key) + "\\\")\\s*:\\s*\\[");
        Matcher m = startPattern.matcher(html);

        int searchFrom = 0;
        while (searchFrom < html.length() && m.find(searchFrom)) {
            boolean escapedMode = html.charAt(m.start()) == '\\';
            int arrayStart = m.end() - 1;
            int depth = 1;
            boolean inString = false;
            int endIndex = -1;

            if (escapedMode) {
                for (int i = arrayStart + 1; i < html.length(); ) {
                    char c = html.charAt(i);
                    if (c == '\\' && i + 1 < html.length()) {
                        if (html.charAt(i + 1) == '"') inString = !inString;
                        i += 2;
                        continue;
                    }
                    if (inString) { i++; continue; }
                    if (c == '[') { depth++; i++; continue; }
                    if (c == ']') {
                        depth--;
                        if (depth == 0) { endIndex = i; break; }
                        i++; continue;
                    }
                    i++;
                }
            } else {
                for (int i = arrayStart + 1; i < html.length(); ) {
                    char c = html.charAt(i);
                    if (inString) {
                        if (c == '\\' && i + 1 < html.length()) { i += 2; continue; }
                        if (c == '"') { inString = false; i++; continue; }
                        i++; continue;
                    }
                    if (c == '"') { inString = true; i++; continue; }
                    if (c == '[') { depth++; i++; continue; }
                    if (c == ']') {
                        depth--;
                        if (depth == 0) { endIndex = i; break; }
                        i++; continue;
                    }
                    i++;
                }
            }

            if (endIndex > 0) {
                String raw = html.substring(arrayStart, endIndex + 1);
                result.add(escapedMode ? raw.replace("\\\"", "\"") : raw);
                searchFrom = endIndex + 1;
            } else {
                // 找不到闭合则避免死循环，跳过当前匹配点
                searchFrom = m.end();
            }
        }
        return result;
    }

    /**
     * 解析 optionList 数组 JSON 字符串为 List&lt;ProductOption&gt;。
     * 字段映射：hoverSelectionText->itemName, finalPrice->price, deliveryType,
     * deliveryUnificationBadgeArea.badge.badgeList[0].badgeId, vendorItemId, itemId。
     */
    public static List<ProductOption> parseOptionListArray(String arrayJson) {
        List<ProductOption> options = new ArrayList<>();
        if (arrayJson == null || arrayJson.trim().isEmpty()) {
            return options;
        }
        try {
            JsonNode arr = JSON_MAPPER.readTree(arrayJson);
            if (arr == null || !arr.isArray()) {
                return options;
            }
            for (int i = 0; i < arr.size(); i++) {
                JsonNode opt = arr.get(i);
                ProductOption po = new ProductOption();
                if (opt.has("hoverSelectionText")) {
                    po.setItemName(opt.get("hoverSelectionText").asText(""));
                }
                if (opt.has("finalPrice")) {
                    po.setPrice(opt.get("finalPrice").asText(""));
                }
                if (opt.has("deliveryType")) {
                    po.setDeliveryType(opt.get("deliveryType").asText(""));
                } else if (opt.has("deliveryUnificationBadgeArea")) {
                    String badgeId = extractBadgeIdFromOption(opt.get("deliveryUnificationBadgeArea"));
                    if (badgeId != null) po.setDeliveryType(badgeId);
                }
                if (opt.has("vendorItemId")) {
                    po.setVendorItemId(String.valueOf(opt.get("vendorItemId").asLong()));
                }
                if (opt.has("itemId")) {
                    po.setItemId(String.valueOf(opt.get("itemId").asLong()));
                }
                if (opt.has("deliveryUnificationBadgeArea")) {
                    JsonNode badgeArea = opt.get("deliveryUnificationBadgeArea");
                    List<String> badges = new ArrayList<>();
                    collectBadgeIds(badgeArea, badges);
                    po.setBadges(badges);
                }
                options.add(po);
            }
        } catch (Exception e) {
            // 解析失败返回空列表
        }
        return options;
    }

    private static String extractBadgeIdFromOption(JsonNode deliveryUnificationBadgeArea) {
        if (deliveryUnificationBadgeArea == null || !deliveryUnificationBadgeArea.has("badge")) return null;
        JsonNode badge = deliveryUnificationBadgeArea.get("badge");
        if (!badge.has("badgeList") || !badge.get("badgeList").isArray() || badge.get("badgeList").size() == 0) return null;
        JsonNode first = badge.get("badgeList").get(0);
        return first.has("badgeId") ? first.get("badgeId").asText(null) : null;
    }

    private static void collectBadgeIds(JsonNode deliveryUnificationBadgeArea, List<String> badges) {
        if (deliveryUnificationBadgeArea == null || !deliveryUnificationBadgeArea.has("badge")) return;
        JsonNode list = deliveryUnificationBadgeArea.get("badge").get("badgeList");
        if (list == null || !list.isArray()) return;
        for (int j = 0; j < list.size(); j++) {
            if (list.get(j).has("badgeId")) {
                badges.add(list.get(j).get("badgeId").asText(""));
            }
        }
    }

    /**
     * 解析HTML内容
     */
    public static List<ProductOption> parseHtml(String html) {
        List<ProductOption> options = new ArrayList<>();
        
        // 1. 提取所有vendorItemId及其上下文
        Map<String, String> vendorContexts = extractVendorItemContexts(html);
        
        // 2. 为每个vendorItemId创建选项对象
        for (Map.Entry<String, String> entry : vendorContexts.entrySet()) {
            String vendorItemId = entry.getKey();
            String context = entry.getValue();
            
            ProductOption option = new ProductOption();
            option.setVendorItemId(vendorItemId);
            
            // 提取itemId
            Pattern itemIdPattern = Pattern.compile("itemId[=:](\\d+)");
            Matcher itemIdMatcher = itemIdPattern.matcher(context);
            if (itemIdMatcher.find()) {
                option.setItemId(itemIdMatcher.group(1));
            }
            
            // 提取发货方式
            extractDeliveryType(context, option);
            
            // 提取价格
            extractPrice(context, option);
            
            // 提取选项名称
            extractItemName(context, option);
            
            options.add(option);
        }
        
        return options;
    }
    
    /**
     * 提取vendorItemId及其上下文
     */
    private static Map<String, String> extractVendorItemContexts(String html) {
        Map<String, String> contexts = new LinkedHashMap<>();
        
        // 匹配vendorItemId，并提取前后500字符的上下文
        Pattern vendorPattern = Pattern.compile("vendorItemId[=:](\\d+)");
        Matcher matcher = vendorPattern.matcher(html);
        
        while (matcher.find()) {
            String vendorItemId = matcher.group(1);
            if (!contexts.containsKey(vendorItemId)) {
                int start = Math.max(0, matcher.start() - 500);
                int end = Math.min(html.length(), matcher.end() + 500);
                String context = html.substring(start, end);
                contexts.put(vendorItemId, context);
            }
        }
        
        return contexts;
    }
    
    /**
     * 提取发货方式
     */
    private static void extractDeliveryType(String context, ProductOption option) {
        // 查找data-badge-id中的发货方式
        Pattern badgePattern = Pattern.compile("data-badge-id=\"(ROCKET[^\"]*)\"");
        Matcher badgeMatcher = badgePattern.matcher(context);
        
        Set<String> deliveryTypes = new LinkedHashSet<>();
        List<String> badges = new ArrayList<>();
        
        while (badgeMatcher.find()) {
            String badge = badgeMatcher.group(1);
            badges.add(badge);
            
            if ("ROCKET".equals(badge)) {
                deliveryTypes.add("ROCKET");
            } else if ("ROCKET_MERCHANT".equals(badge)) {
                deliveryTypes.add("ROCKET_MERCHANT");
            }
        }
        
        option.setBadges(badges);
        
        // 确定主要发货方式
        if (deliveryTypes.contains("ROCKET")) {
            option.setDeliveryType("ROCKET");
        } else if (deliveryTypes.contains("ROCKET_MERCHANT")) {
            option.setDeliveryType("ROCKET_MERCHANT");
        } else if (!badges.isEmpty()) {
            option.setDeliveryType(badges.get(0));
        }
    }
    
    /**
     * 提取价格
     */
    private static void extractPrice(String context, ProductOption option) {
        // 匹配价格格式：38,170원, 38,170원 등
        Pattern pricePattern = Pattern.compile("(\\d{1,3}(?:,\\d{3})*)\\s*원");
        Matcher priceMatcher = pricePattern.matcher(context);
        
        // 查找第一个价格（通常是最相关的）
        if (priceMatcher.find()) {
            option.setPrice(priceMatcher.group(1) + "원");
        }
    }
    
    /**
     * 提取选项名称
     */
    private static void extractItemName(String context, ProductOption option) {
        // 方法1: 从链接文本提取
        Pattern linkTextPattern = Pattern.compile(
            "href=\"[^\"]*vendorItemId=" + Pattern.quote(option.getVendorItemId()) + "[^\"]*\"[^>]*>([^<]+)</a>",
            Pattern.CASE_INSENSITIVE
        );
        Matcher linkMatcher = linkTextPattern.matcher(context);
        if (linkMatcher.find()) {
            String linkText = linkMatcher.group(1).trim();
            if (!linkText.isEmpty() && linkText.length() < 100) {
                option.setItemName(linkText);
                return;
            }
        }
        
        // 方法2: 从title或alt属性提取
        Pattern titlePattern = Pattern.compile(
            "(?:title|alt)=\"([^\"]*" + Pattern.quote(option.getVendorItemId()) + "[^\"]*)\"",
            Pattern.CASE_INSENSITIVE
        );
        Matcher titleMatcher = titlePattern.matcher(context);
        if (titleMatcher.find()) {
            option.setItemName(titleMatcher.group(1));
            return;
        }
        
        // 方法3: 从附近的文本内容提取（包含常见属性关键词）
        Pattern attributePattern = Pattern.compile(
            "([가-힣]+\\s*[×x]\\s*\\d+[가-힣]*|[가-힣]+\\s*\\d+[가-힣]*)",
            Pattern.CASE_INSENSITIVE
        );
        Matcher attrMatcher = attributePattern.matcher(context);
        if (attrMatcher.find()) {
            option.setItemName(attrMatcher.group(1));
        }
    }
    
    /**
     * 从HTML结构提取选项（改进版）
     * 直接从option-table-list结构中提取
     */
    public static List<ProductOption> parseHtmlFromStructure(String html) {
        List<ProductOption> options = new ArrayList<>();
        
        // 提取颜色选项
        List<String> colors = extractColors(html);
        
        // 提取数量选项及其相关信息
        List<OptionInfo> optionInfos = extractOptionInfos(html);
        
        // 如果找到了颜色和数量，组合它们
        if (!colors.isEmpty() && !optionInfos.isEmpty()) {
            for (String color : colors) {
                for (OptionInfo info : optionInfos) {
                    ProductOption option = new ProductOption();
                    option.setItemName(color + " × " + info.quantity);
                    option.setPrice(info.price);
                    option.setDeliveryType(info.deliveryType);
                    option.setBadges(new ArrayList<>(info.badges));
                    options.add(option);
                }
            }
        } else if (!optionInfos.isEmpty()) {
            // 如果无法组合，至少返回找到的选项信息
            for (OptionInfo info : optionInfos) {
                ProductOption option = new ProductOption();
                option.setItemName(info.quantity);
                option.setPrice(info.price);
                option.setDeliveryType(info.deliveryType);
                option.setBadges(new ArrayList<>(info.badges));
                options.add(option);
            }
        }
        
        return options;
    }
    
    /**
     * 选项信息内部类
     */
    private static class OptionInfo {
        String quantity;
        String price;
        String deliveryType;
        List<String> badges = new ArrayList<>();
    }
    
    /**
     * 提取颜色选项
     */
    private static List<String> extractColors(String html) {
        List<String> colors = new ArrayList<>();
        
        // 从 tab-selector__tab-image-title 中提取颜色
        // 匹配结构：<div class="tab-selector__tab-image-title">颜色名</div>
        Pattern colorPattern = Pattern.compile(
            "class=\"tab-selector__tab-image-title\"[^>]*>\\s*([가-힣]+)\\s*<",
            Pattern.DOTALL
        );
        Matcher colorMatcher = colorPattern.matcher(html);
        
        while (colorMatcher.find()) {
            String color = colorMatcher.group(1).trim();
            if (!color.isEmpty() && !colors.contains(color)) {
                colors.add(color);
            }
        }
        
        // 如果上面的方法没找到，尝试从alt属性提取
        if (colors.isEmpty()) {
            Pattern altColorPattern = Pattern.compile(
                "alt=\"([가-힣]+)\"[^>]*class=\"tab-selector__tab-image\"",
                Pattern.DOTALL
            );
            Matcher altMatcher = altColorPattern.matcher(html);
            while (altMatcher.find()) {
                String color = altMatcher.group(1).trim();
                if (!color.isEmpty() && !colors.contains(color)) {
                    colors.add(color);
                }
            }
        }
        
        return colors;
    }
    
    /**
     * 提取选项信息（数量、价格、发货方式）
     */
    private static List<OptionInfo> extractOptionInfos(String html) {
        List<OptionInfo> optionInfos = new ArrayList<>();
        
        // 匹配每个选项块：option-table-list__option，使用更宽松的匹配
        // 匹配从 option-table-list__option 开始到下一个 option-table-list__option 或 option-table-list__see-all-btn 结束
        Pattern optionBlockPattern = Pattern.compile(
            "<div class=\"option-table-list__option[^\"]*\"[^>]*>(.*?)(?=<div class=\"option-table-list__(?:option|see-all-btn)|\"|$)",
            Pattern.DOTALL | Pattern.CASE_INSENSITIVE
        );
        Matcher blockMatcher = optionBlockPattern.matcher(html);
        
        while (blockMatcher.find()) {
            String optionBlock = blockMatcher.group(1);
            OptionInfo info = new OptionInfo();
            
            // 提取数量 - 匹配 option-table-list__option-name 中的文本
            Pattern quantityPattern = Pattern.compile(
                "class=\"option-table-list__option-name\"[^>]*>\\s*([^<\\n]+?)\\s*<",
            Pattern.DOTALL
            );
            Matcher quantityMatcher = quantityPattern.matcher(optionBlock);
            if (quantityMatcher.find()) {
                info.quantity = quantityMatcher.group(1).trim();
            }
            
            // 提取价格 - 匹配 option-table-list__option-price 中的价格
            Pattern pricePattern = Pattern.compile(
                "class=\"option-table-list__option-price\"[^>]*>\\s*([\\d,]+원)",
                Pattern.DOTALL
            );
            Matcher priceMatcher = pricePattern.matcher(optionBlock);
            if (priceMatcher.find()) {
                info.price = priceMatcher.group(1).trim();
            }
            
            // 提取发货方式 - 在选项块中查找data-badge-id
            Pattern badgePattern = Pattern.compile("data-badge-id=\"(ROCKET[^\"]*)\"");
            Matcher badgeMatcher = badgePattern.matcher(optionBlock);
            while (badgeMatcher.find()) {
                String badge = badgeMatcher.group(1);
                if (!info.badges.contains(badge)) {
                    info.badges.add(badge);
                }
                if ("ROCKET".equals(badge)) {
                    info.deliveryType = "ROCKET";
                } else if ("ROCKET_MERCHANT".equals(badge)) {
                    info.deliveryType = "ROCKET_MERCHANT";
                }
            }
            
            // 只有当找到数量时才添加（确保是有效的选项）
            if (info.quantity != null && !info.quantity.trim().isEmpty()) {
                optionInfos.add(info);
            }
        }
        
        return optionInfos;
    }
    
    /**
     * 生成分析报告
     */
    public static String generateReport(List<ProductOption> options) {
        StringBuilder report = new StringBuilder();
        
        report.append("==========================================\n");
        report.append("HTML产品属性与发货方式分析报告\n");
        report.append("==========================================\n\n");
        
        report.append("【一、产品选项汇总】\n");
        report.append("------------------------------------------\n");
        report.append("共找到 ").append(options.size()).append(" 个选项:\n\n");
        
        for (int i = 0; i < options.size(); i++) {
            ProductOption option = options.get(i);
            report.append(String.format("%d. %s\n", i + 1, option.getItemName() != null ? option.getItemName() : "未知"));
            report.append(String.format("   vendorItemId: %s\n", option.getVendorItemId()));
            if (option.getItemId() != null) {
                report.append(String.format("   itemId: %s\n", option.getItemId()));
            }
            report.append(String.format("   发货方式: %s (%s)\n", 
                    option.getDeliveryType() != null ? option.getDeliveryType() : "未知",
                    getDeliveryTypeDescription(option.getDeliveryType())));
            if (option.getPrice() != null) {
                report.append(String.format("   价格: %s\n", option.getPrice()));
            }
            if (!option.getBadges().isEmpty()) {
                report.append(String.format("   配送徽章: %s\n", String.join(", ", option.getBadges())));
            }
            report.append("\n");
        }
        
        // 按发货方式分组
        report.append("【二、按发货方式分组】\n");
        report.append("------------------------------------------\n");
        
        Map<String, List<ProductOption>> groupedByDelivery = options.stream()
                .filter(opt -> opt.getDeliveryType() != null)
                .collect(Collectors.groupingBy(ProductOption::getDeliveryType));
        
        for (Map.Entry<String, List<ProductOption>> entry : groupedByDelivery.entrySet()) {
            String deliveryType = entry.getKey();
            List<ProductOption> opts = entry.getValue();
            
            report.append(String.format("\n%s (%s): %d 个选项\n",
                    deliveryType, getDeliveryTypeDescription(deliveryType), opts.size()));
            
            for (ProductOption opt : opts) {
                report.append(String.format("  - %s (vendorItemId: %s",
                        opt.getItemName() != null ? opt.getItemName() : "未知",
                        opt.getVendorItemId()));
                if (opt.getPrice() != null) {
                    report.append(", 价格: ").append(opt.getPrice());
                }
                report.append(")\n");
            }
        }
        
        report.append("\n==========================================\n");
        
        return report.toString();
    }
    
    /**
     * 获取发货方式描述
     */
    private static String getDeliveryTypeDescription(String deliveryType) {
        if (deliveryType == null) {
            return "未知";
        }
        switch (deliveryType) {
            case "ROCKET":
                return "火箭配送（쿠팡 직접 배송）";
            case "ROCKET_MERCHANT":
                return "火箭商家配送（판매자 직접 배송）";
            default:
                return "未知配送方式";
        }
    }
    
    /**
     * 主方法 - 用于测试
     */
    public static void main(String[] args) {
        try {
            String filePath = "src/main/resources/7643741908.html";
            String html = new String(Files.readAllBytes(Paths.get(filePath)), "UTF-8");
            
            // 方法1: 从HTML结构提取（推荐）
            System.out.println("=== 方法1: 从HTML结构提取 ===");
            List<ProductOption> optionsFromStructure = parseHtmlFromStructure(html);
            System.out.println("从结构提取到 " + optionsFromStructure.size() + " 个选项\n");
            
            // 方法2: 从vendorItemId提取（备用）
            System.out.println("=== 方法2: 从vendorItemId提取 ===");
            List<ProductOption> optionsFromVendor = parseHtml(html);
            System.out.println("从vendorItemId提取到 " + optionsFromVendor.size() + " 个选项\n");
            
            // 合并结果（优先使用结构提取的结果）
            List<ProductOption> finalOptions = optionsFromStructure.isEmpty() 
                    ? optionsFromVendor 
                    : optionsFromStructure;
            
            // 如果结构提取的结果缺少vendorItemId，尝试从vendorItemId提取的结果中补充
            if (!optionsFromStructure.isEmpty() && !optionsFromVendor.isEmpty()) {
                // 尝试匹配并补充vendorItemId
                for (ProductOption structOpt : optionsFromStructure) {
                    for (ProductOption vendorOpt : optionsFromVendor) {
                        // 根据价格和发货方式匹配
                        if (structOpt.getPrice() != null && vendorOpt.getPrice() != null &&
                            structOpt.getPrice().equals(vendorOpt.getPrice()) &&
                            structOpt.getDeliveryType() != null && vendorOpt.getDeliveryType() != null &&
                            structOpt.getDeliveryType().equals(vendorOpt.getDeliveryType())) {
                            if (structOpt.getVendorItemId() == null && vendorOpt.getVendorItemId() != null) {
                                structOpt.setVendorItemId(vendorOpt.getVendorItemId());
                            }
                            if (structOpt.getItemId() == null && vendorOpt.getItemId() != null) {
                                structOpt.setItemId(vendorOpt.getItemId());
                            }
                            break;
                        }
                    }
                }
            }
            
            String report = generateReport(finalOptions);
            System.out.println(report);
            
            // 保存报告到文件
            Path reportPath = Paths.get("src/main/resources/html_product_analysis_report.txt");
            Files.write(reportPath, report.getBytes("UTF-8"));
            System.out.println("\n报告已保存到: " + reportPath);
            
        } catch (Exception e) {
            System.err.println("解析失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

