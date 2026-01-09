package com.ecommerce.analyzer;

import com.ecommerce.analyzer.service.ProductUrlAnalyzer;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * 产品图片下载应用程序主入口
 * 功能：生成HTML、下载主图和详情页图片
 * 
 * 使用方法：
 * 1. 通过命令行参数传递URL：
 *    java -jar product-image-analyzer.jar https://www.coupang.com/vp/products/9086902162
 *    java -jar product-image-analyzer.jar --url=https://www.coupang.com/vp/products/9086902162
 *    java -jar product-image-analyzer.jar -u https://www.coupang.com/vp/products/9086902162
 * 
 * 2. 如果不传参数，将使用代码中的默认URL
 */
@Slf4j
public class ProductImageAnalyzerApplication {
    
    /**
     * 默认产品URL - 当命令行未提供URL时使用
     */
    private static final String DEFAULT_PRODUCT_URL = "https://www.coupang.com/vp/products/9086902162?itemId=26699966136&vendorItemId=93680643000&sourceType=srp_product_ads&clickEventId=3b2cf4e0-eaa9-11f0-95db-92d6bafac94d&korePlacement=15&koreSubPlacement=5&clickEventId=3b2cf4e0-eaa9-11f0-95db-92d6bafac94d&korePlacement=15&koreSubPlacement=5&traceId=mk6l29hh";
    
    public static void main(String[] args) {
        log.info("=== 产品图片下载工具 ===");
        log.info("功能：生成HTML、下载主图和详情页图片");
        
        // 从命令行参数解析产品URL
        String productUrl = parseProductUrl(args);
        
        if (productUrl == null || productUrl.trim().isEmpty()) {
            System.err.println("错误：未提供产品URL");
            System.err.println("\n使用方法：");
            System.err.println("  java -jar product-image-analyzer.jar <产品URL>");
            System.err.println("  java -jar product-image-analyzer.jar --url=<产品URL>");
            System.err.println("  java -jar product-image-analyzer.jar -u <产品URL>");
            System.err.println("\n示例：");
            System.err.println("  java -jar product-image-analyzer.jar https://www.coupang.com/vp/products/9086902162");
            System.exit(1);
        }
        
        log.info("产品URL：{}", productUrl);
        
        ProductUrlAnalyzer urlAnalyzer = new ProductUrlAnalyzer();
        
        try {
            // 处理产品URL
            processProductUrl(productUrl, urlAnalyzer);
        } catch (Exception e) {
            log.error("程序执行失败: {}", e.getMessage(), e);
            System.err.println("处理失败: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } finally {
            urlAnalyzer.cleanup();
        }
    }
    
    /**
     * 从命令行参数解析产品URL
     * 支持以下格式：
     * 1. 直接URL作为第一个参数：java -jar app.jar https://...
     * 2. --url=URL 格式：java -jar app.jar --url=https://...
     * 3. -u URL 格式：java -jar app.jar -u https://...
     * 
     * @param args 命令行参数
     * @return 产品URL，如果未找到有效URL则返回null
     */
    private static String parseProductUrl(String[] args) {
        if (args == null || args.length == 0) {
            // 如果没有参数，返回默认URL（用于测试）
            log.info("未提供命令行参数，使用默认URL");
            return DEFAULT_PRODUCT_URL;
        }
        
        // 遍历参数，查找URL
        for (int i = 0; i < args.length; i++) {
            String arg = args[i].trim();
            
            // 格式1: --url=URL 或 -u=URL
            if (arg.startsWith("--url=")) {
                String url = arg.substring(6); // 跳过 "--url="
                if (!url.isEmpty() && (url.startsWith("http://") || url.startsWith("https://"))) {
                    return url;
                }
            } else if (arg.startsWith("-u=")) {
                String url = arg.substring(3); // 跳过 "-u="
                if (!url.isEmpty() && (url.startsWith("http://") || url.startsWith("https://"))) {
                    return url;
                }
            }
            // 格式2: -u URL 或 --url URL (下一个参数是URL)
            else if (arg.equals("-u") || arg.equals("--url")) {
                if (i + 1 < args.length) {
                    String url = args[i + 1].trim();
                    if (url.startsWith("http://") || url.startsWith("https://")) {
                        return url;
                    }
                }
            }
            // 格式3: 直接是URL (以http://或https://开头)
            else if (arg.startsWith("http://") || arg.startsWith("https://")) {
                return arg;
            }
        }
        
        // 如果第一个参数看起来像URL，直接返回
        if (args.length > 0) {
            String firstArg = args[0].trim();
            if (firstArg.startsWith("http://") || firstArg.startsWith("https://")) {
                return firstArg;
            }
        }
        
        // 未找到有效URL，返回null（让调用者处理错误提示）
        return null;
    }
    
    /**
     * 处理产品URL：生成HTML、下载主图和详情页图片
     */
    private static void processProductUrl(String productUrl, ProductUrlAnalyzer urlAnalyzer) {
        try {
            System.out.println("\n=== 开始处理产品URL ===");
            System.out.println("产品链接: " + productUrl);
            System.out.println("请稍候，正在获取页面信息并下载图片...\n");
            
            ProductUrlAnalyzer.ProductDownloadResult result = urlAnalyzer.downloadFromUrl(productUrl);
            
            // 输出结果
            System.out.println("\n=== 处理结果 ===");
            System.out.println("产品标题: " + result.getProductTitle());
            System.out.println("产品ID: " + result.getProductId());
            System.out.println("HTML路径: " + result.getHtmlPath());
            System.out.println("主图URL数量: " + result.getMainImageUrls().size());
            System.out.println("主图下载成功: " + result.getMainImagePaths().size() + " 张");
            System.out.println("详情页图片URL数量: " + result.getDetailImageUrls().size());
            System.out.println("详情页图片下载成功: " + result.getDetailImagePaths().size() + " 张");
            
            // 显示保存路径
            String downloadDir = "downloads" + File.separator + result.getProductId();
            System.out.println("\n文件保存路径: ");
            System.out.println("- HTML: " + downloadDir + File.separator + "page.html");
            System.out.println("- 主图: " + downloadDir + File.separator + "main" + File.separator);
            System.out.println("- 详情页图片: " + downloadDir + File.separator + "detail" + File.separator);
            
            System.out.println("\n处理完成！");
            
        } catch (Exception e) {
            log.error("处理产品URL失败: {}", e.getMessage(), e);
            System.err.println("处理失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("处理失败", e);
        }
    }
    
}

