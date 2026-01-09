package com.ecommerce.analyzer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 网页爬取服务 - 从产品页面提取图片URL
 */
@Slf4j
public class WebCrawlerService {
    
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
    private static final int CONNECT_TIMEOUT = 30000; // 30秒
    private static final int READ_TIMEOUT = 30000; // 30秒
    
    /**
     * 产品页面信息
     */
    @Data
    public static class ProductPageInfo {
        private String productUrl;
        private String productTitle;
        private String productId;
        private List<String> mainImageUrls;
        private List<String> detailImageUrls;
        private List<String> allImageUrls;
    }
    
    /**
     * 从产品URL获取页面信息（带重试机制）
     */
    public ProductPageInfo fetchProductPage(String productUrl) throws IOException {
        log.info("正在获取产品页面: {}", productUrl);
        
        Document doc = fetchDocumentWithRetry(productUrl);
        
        ProductPageInfo info = new ProductPageInfo();
        info.setProductUrl(productUrl);
        String productId = extractProductId(productUrl);
        info.setProductId(productId);
        info.setProductTitle(extractProductTitle(doc));
        
        // 保存HTML到downloads产品目录下
        saveHtmlToFile(doc, productId);
        
        // 提取主图
        info.setMainImageUrls(extractMainImages(doc));
        log.info("找到 {} 张主图", info.getMainImageUrls().size());
        
        // 提取详情页图片
        info.setDetailImageUrls(extractDetailImages(doc));
        log.info("找到 {} 张详情页图片", info.getDetailImageUrls().size());
        
        // 提取所有图片（去重）
        Set<String> allImages = new HashSet<>();
        allImages.addAll(info.getMainImageUrls());
        allImages.addAll(info.getDetailImageUrls());
        info.setAllImageUrls(new ArrayList<>(allImages));
        
        return info;
    }
    
    /**
     * 保存HTML内容到downloads产品目录下
     */
    private void saveHtmlToFile(Document doc, String productId) {
        try {
            // 创建产品目录
            String downloadDir = "downloads" + File.separator + productId;
            File dir = new File(downloadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            // 保存HTML文件
            String htmlFilePath = downloadDir + File.separator + "page.html";
            File htmlFile = new File(htmlFilePath);
            
            // 使用Jsoup的html()方法获取完整的HTML内容
            String htmlContent = doc.html();
            
            // 保存到文件
            try (FileWriter writer = new FileWriter(htmlFile, StandardCharsets.UTF_8)) {
                writer.write(htmlContent);
            }
            
            log.info("HTML已保存到: {}", htmlFilePath);
        } catch (Exception e) {
            log.warn("保存HTML文件失败: {}", e.getMessage());
        }
    }
    
    /**
     * 获取Document，带重试机制
     * 重试20次，每次间隔0.5-2秒随机
     */
    private Document fetchDocumentWithRetry(String productUrl) throws IOException {
        int maxRetries = 20;
        int minRetryInterval = 500; // 0.5秒，单位：毫秒
        int maxRetryInterval = 2000; // 2秒，单位：毫秒
        IOException lastException = null;
        java.util.Random random = new java.util.Random();
        
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                log.info("尝试连接产品页面 (第 {}/{} 次)...", attempt, maxRetries);
                
                // 直接使用Jsoup获取页面
                Document doc = Jsoup.connect(productUrl)
                        .userAgent(USER_AGENT)
                        .timeout(CONNECT_TIMEOUT)
                        .followRedirects(true)
                        .get();
                
                log.info("成功获取产品页面！");
                return doc;
                
            } catch (IOException e) {
                lastException = e;
                log.warn("第 {} 次连接失败: {}", attempt, e.getMessage());
                
                // 如果不是最后一次尝试，等待后重试
                if (attempt < maxRetries) {
                    // 生成0.5-2秒之间的随机等待时间
                    int retryInterval = minRetryInterval + random.nextInt(maxRetryInterval - minRetryInterval + 1);
                    double retryIntervalSeconds = retryInterval / 1000.0;
                    log.info("等待 {} 秒后重试...", String.format("%.2f", retryIntervalSeconds));
                    try {
                        Thread.sleep(retryInterval);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new IOException("重试过程被中断", ie);
                    }
                } else {
                    log.error("达到最大重试次数 ({} 次)，连接失败", maxRetries);
                }
            }
        }
        
        // 所有重试都失败，抛出最后一次的异常
        throw new IOException("连接失败，已重试 " + maxRetries + " 次", lastException);
    }
    
    /**
     * 从URL提取产品ID
     */
    private String extractProductId(String url) {
        // Coupang URL格式: .../products/8729129197?...
        Pattern pattern = Pattern.compile("products/(\\d+)");
        java.util.regex.Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return String.valueOf(url.hashCode());
    }
    
    /**
     * 提取产品标题
     */
    private String extractProductTitle(Document doc) {
        // Coupang页面标题通常在 <title> 或特定的meta标签中
        String title = doc.select("meta[property=og:title]").attr("content");
        if (title.isEmpty()) {
            title = doc.select("title").text();
        }
        if (title.isEmpty()) {
            title = doc.select("h1").first() != null ? doc.select("h1").first().text() : "未知产品";
        }
        return title.trim();
    }
    
    /**
     * 提取主图URL（Coupang特定逻辑）
     * 优先从 JSON-LD 结构化数据中提取
     */
    private List<String> extractMainImages(Document doc) {
        List<String> mainImages = new ArrayList<>();
        
        // 优先从 JSON-LD 结构化数据中提取主图
        // script type="application/ld+json" 中包含 Product 数据
        try {
            Elements scriptElements = doc.select("script[type=application/ld+json]");
            ObjectMapper objectMapper = new ObjectMapper();
            
            for (Element script : scriptElements) {
                String jsonText = script.html();
                if (jsonText == null || jsonText.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    JsonNode jsonNode = objectMapper.readTree(jsonText);
                    
                    // 检查是否为 Product 类型
                    JsonNode typeNode = jsonNode.get("@type");
                    if (typeNode != null && "Product".equals(typeNode.asText())) {
                        // 提取 image 数组
                        JsonNode imageNode = jsonNode.get("image");
                        if (imageNode != null) {
                            if (imageNode.isArray()) {
                                log.info("从 JSON-LD 找到 {} 张主图", imageNode.size());
                                for (JsonNode imgNode : imageNode) {
                                    if (imgNode.isTextual()) {
                                        String imageUrl = imgNode.asText();
                                        if (imageUrl != null && !imageUrl.isEmpty()) {
                                            String fullUrl = convertToFullUrl(imageUrl);
                                            mainImages.add(fullUrl);
                                            log.debug("从 JSON-LD 提取主图: {}", fullUrl);
                                        }
                                    }
                                }
                            } else if (imageNode.isTextual()) {
                                // 单个图片URL（字符串）
                                String imageUrl = imageNode.asText();
                                if (imageUrl != null && !imageUrl.isEmpty()) {
                                    String fullUrl = convertToFullUrl(imageUrl);
                                    mainImages.add(fullUrl);
                                    log.debug("从 JSON-LD 提取主图: {}", fullUrl);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    log.debug("解析 JSON-LD 失败: {}", e.getMessage());
                    // 继续尝试下一个 script 标签
                }
            }
            
            if (!mainImages.isEmpty()) {
                log.info("成功从 JSON-LD 提取 {} 张主图", mainImages.size());
            }
        } catch (Exception e) {
            log.warn("从 JSON-LD 提取主图失败: {}", e.getMessage());
        }
        
        // 如果 JSON-LD 没有找到图片，使用备用方法
        if (mainImages.isEmpty()) {
            log.info("使用备用方法提取主图");
            
            // 1. 使用指定的CSS选择器提取主图
            try {
                String ulSelector = "body > div:nth-child(5) > div > div.twc-flex.twc-max-w-full > main > div.prod-atf.twc-block.md\\:twc-flex.twc-relative > div.product-image.twc-relative.twc-flex-1.md\\:twc-flex > div.twc-w-\\[70px\\].twc-relative > ul";
                Elements ulElements = doc.select(ulSelector);
                
                if (!ulElements.isEmpty()) {
                    log.info("使用指定选择器找到主图容器");
                    for (Element ul : ulElements) {
                        Elements liElements = ul.select("li");
                        for (Element li : liElements) {
                            Elements imgElements = li.select("img");
                            for (Element img : imgElements) {
                                String src = getImageSrc(img);
                                if (src != null && !src.isEmpty()) {
                                    String fullUrl = convertToFullUrl(src);
                                    mainImages.add(fullUrl);
                                    log.debug("从指定选择器提取主图: {}", fullUrl);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("使用指定选择器提取主图失败: {}", e.getMessage());
            }
            
            // 2. og:image meta标签
            if (mainImages.isEmpty()) {
                String ogImage = doc.select("meta[property=og:image]").attr("content");
                if (!ogImage.isEmpty()) {
                    mainImages.add(convertToFullUrl(ogImage));
                }
            }
            
            // 3. 主图容器中的图片
            if (mainImages.isEmpty()) {
                Elements prodImages = doc.select(".prod-image, .product-image, .main-image");
                for (Element img : prodImages.select("img")) {
                    String src = getImageSrc(img);
                    if (src != null && !src.isEmpty()) {
                        mainImages.add(convertToFullUrl(src));
                    }
                }
            }
        }
        
        // 去重并限制数量（最多10张主图）
        return deduplicateUrls(mainImages).stream().limit(10).collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * 提取详情页图片URL
     */
    private List<String> extractDetailImages(Document doc) {
        List<String> detailImages = new ArrayList<>();
        
        // 优先使用 getElementsByClass("type-IMAGE_NO_SPACE") 提取详情页图片
        try {
            // 使用 getElementsByClass 查找所有 class="type-IMAGE_NO_SPACE" 的元素
            Elements baseDivs = doc.getElementsByClass("type-IMAGE_NO_SPACE");
            
            if (!baseDivs.isEmpty()) {
                log.info("使用 getElementsByClass 找到详情页容器，找到 {} 个元素", baseDivs.size());
                
                // 遍历所有找到的元素
                for (Element element : baseDivs) {
                    // 查询该元素下的img标签
                    Elements imgElements = element.select("img");
                    log.debug("元素下找到 {} 个img标签", imgElements.size());
                    
                    // 遍历所有img标签，获取src属性
                    for (Element img : imgElements) {
                        String src = getImageSrc(img);
                        if (src != null && !src.isEmpty()) {
                            // 过滤掉小图标、logo等
                            if (isDetailImage(img)) {
                                String fullUrl = convertToFullUrl(src);
                                detailImages.add(fullUrl);
                                log.debug("从 type-IMAGE_NO_SPACE 提取详情页图片: {}", fullUrl);
                            }
                        }
                    }
                }
            } else {
                log.warn("使用 getElementsByClass 未找到详情页容器，尝试备用方法");
            }
        } catch (Exception e) {
            log.warn("使用 getElementsByClass 提取详情页图片失败: {}", e.getMessage());
        }
        
        // 如果指定选择器没有找到图片，使用备用方法
        if (detailImages.isEmpty()) {
            log.info("使用备用方法提取详情页图片");
            
            // 1. 详情页容器
            Elements detailContainers = doc.select(".prod-description, .prod-detail, .product-detail, .detail-section");
            
            for (Element container : detailContainers) {
                Elements images = container.select("img");
                for (Element img : images) {
                    String src = getImageSrc(img);
                    if (src != null && !src.isEmpty()) {
                        // 过滤掉小图标、logo等
                        if (isDetailImage(img)) {
                            detailImages.add(convertToFullUrl(src));
                        }
                    }
                }
            }
            
            // 2. 如果上面没找到，尝试查找所有大尺寸图片
            if (detailImages.isEmpty()) {
                Elements allImages = doc.select("img[width][height]");
                for (Element img : allImages) {
                    int width = parseDimension(img.attr("width"));
                    int height = parseDimension(img.attr("height"));
                    // 过滤大尺寸图片（可能是详情图）
                    if (width > 400 && height > 400) {
                        String src = getImageSrc(img);
                        if (src != null && !src.isEmpty()) {
                            detailImages.add(convertToFullUrl(src));
                        }
                    }
                }
            }
            
            // 3. 尝试从lazy-load属性中提取
            Elements lazyImages = doc.select("[data-lazy-src], [data-src], [data-original]");
            for (Element img : lazyImages) {
                String src = img.attr("data-lazy-src");
                if (src.isEmpty()) {
                    src = img.attr("data-src");
                }
                if (src.isEmpty()) {
                    src = img.attr("data-original");
                }
                if (!src.isEmpty() && isDetailImage(img)) {
                    detailImages.add(convertToFullUrl(src));
                }
            }
        }
        
        // 去重
        return deduplicateUrls(detailImages);
    }
    
    /**
     * 判断是否为详情页图片（过滤掉小图标、logo等）
     */
    private boolean isDetailImage(Element img) {
        String src = getImageSrc(img);
        if (src == null || src.isEmpty()) {
            return false;
        }
        
        // 过滤掉常见的小图片
        String lowerSrc = src.toLowerCase();
        if (lowerSrc.contains("icon") || lowerSrc.contains("logo") || 
            lowerSrc.contains("badge") || lowerSrc.contains("button")) {
            return false;
        }
        
        // 检查尺寸属性
        int width = parseDimension(img.attr("width"));
        int height = parseDimension(img.attr("height"));
        
        // 如果尺寸很小，可能是图标
        if (width > 0 && width < 100 && height > 0 && height < 100) {
            return false;
        }
        
        return true;
    }
    
    /**
     * 获取图片src属性
     */
    private String getImageSrc(Element img) {
        String src = img.attr("src");
        if (src.isEmpty()) {
            src = img.attr("data-src");
        }
        if (src.isEmpty()) {
            src = img.attr("data-lazy-src");
        }
        if (src.isEmpty()) {
            src = img.attr("data-original");
        }
        return src;
    }
    
    /**
     * 转换为完整URL
     */
    private String convertToFullUrl(String url) {
        if (url == null || url.isEmpty()) {
            return url;
        }
        
        // 如果已经是完整URL，直接返回
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        }
        
        // 如果是 // 开头，添加 https:
        if (url.startsWith("//")) {
            return "https:" + url;
        }
        
        // 如果是 / 开头，添加域名
        if (url.startsWith("/")) {
            return "https://www.coupang.com" + url;
        }
        
        // 其他情况返回原样
        return url;
    }
    
    /**
     * 解析尺寸字符串为整数
     */
    private int parseDimension(String dim) {
        try {
            if (dim == null || dim.isEmpty()) {
                return 0;
            }
            // 移除单位（px等）
            dim = dim.replaceAll("[^0-9]", "");
            return Integer.parseInt(dim);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    /**
     * 从JSON数据中提取图片URL（如果页面中包含产品数据的JSON）
     */
    private void extractImagesFromJson(String pageText, List<String> images) {
        // 查找包含图片URL的JSON结构
        Pattern imagePattern = Pattern.compile("(https?://[^\"'\\s]+\\.(jpg|jpeg|png|gif|webp))", Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher matcher = imagePattern.matcher(pageText);
        
        while (matcher.find()) {
            String imageUrl = matcher.group(1);
            if (imageUrl.contains("product") || imageUrl.contains("prod")) {
                images.add(imageUrl);
            }
        }
    }
    
    /**
     * URL去重
     */
    private List<String> deduplicateUrls(List<String> urls) {
        Set<String> uniqueUrls = new HashSet<>();
        List<String> result = new ArrayList<>();
        
        for (String url : urls) {
            // 规范化URL（移除查询参数中的特定参数）
            String normalizedUrl = normalizeUrl(url);
            if (!uniqueUrls.contains(normalizedUrl) && normalizedUrl != null && !normalizedUrl.isEmpty()) {
                uniqueUrls.add(normalizedUrl);
                result.add(url); // 保留原始URL
            }
        }
        
        return result;
    }
    
    /**
     * 规范化URL（用于去重比较）
     */
    private String normalizeUrl(String url) {
        if (url == null) {
            return null;
        }
        // 移除常见的尺寸参数，保留URL核心部分用于比较
        return url.split("\\?")[0];
    }
}

