package com.ecommerce.analyzer.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 产品URL处理服务 - 生成HTML、下载主图和详情页图片
 */
@Slf4j
@Data
public class ProductUrlAnalyzer {
    
    private final WebCrawlerService webCrawlerService;
    private final ImageDownloadService imageDownloadService;
    
    public ProductUrlAnalyzer() {
        this.webCrawlerService = new WebCrawlerService();
        this.imageDownloadService = new ImageDownloadService();
    }
    
    /**
     * 从产品URL获取页面、生成HTML、下载主图和详情页图片
     */
    public ProductDownloadResult downloadFromUrl(String productUrl) throws IOException {
        log.info("=== 开始处理产品URL: {} ===", productUrl);
        
        ProductDownloadResult result = new ProductDownloadResult();
        result.setProductUrl(productUrl);
        
        try {
            // 1. 获取产品页面信息（自动生成HTML）
            log.info("步骤 1/3: 获取产品页面信息并生成HTML...");
            WebCrawlerService.ProductPageInfo pageInfo = webCrawlerService.fetchProductPage(productUrl);
            result.setProductId(pageInfo.getProductId());
            result.setProductTitle(pageInfo.getProductTitle());
            result.setMainImageUrls(pageInfo.getMainImageUrls());
            result.setDetailImageUrls(pageInfo.getDetailImageUrls());
            result.setHtmlPath("downloads" + File.separator + pageInfo.getProductId() + File.separator + "page.html");
            
            // 2. 下载主图
            log.info("步骤 2/3: 下载主图...");
            List<String> mainImagePaths = new ArrayList<>();
            if (!pageInfo.getMainImageUrls().isEmpty()) {
                mainImagePaths = imageDownloadService.downloadImages(
                    pageInfo.getMainImageUrls(), 
                    pageInfo.getProductId(), 
                    "main"
                );
            }
            result.setMainImagePaths(mainImagePaths);
            
            // 3. 下载详情页图片
            log.info("步骤 3/3: 下载详情页图片...");
            List<String> detailImagePaths = new ArrayList<>();
            if (!pageInfo.getDetailImageUrls().isEmpty()) {
                detailImagePaths = imageDownloadService.downloadImages(
                    pageInfo.getDetailImageUrls(), 
                    pageInfo.getProductId(), 
                    "detail"
                );
            }
            result.setDetailImagePaths(detailImagePaths);
            
            log.info("=== 处理完成 ===");
            return result;
            
        } catch (Exception e) {
            log.error("处理产品URL失败: {}", e.getMessage(), e);
            throw new IOException("处理产品URL失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 产品下载结果
     */
    @Data
    public static class ProductDownloadResult {
        private String productUrl;
        private String productId;
        private String productTitle;
        private String htmlPath;
        private List<String> mainImageUrls;
        private List<String> detailImageUrls;
        private List<String> mainImagePaths;
        private List<String> detailImagePaths;
    }
    
    /**
     * 清理资源
     */
    public void cleanup() {
        if (imageDownloadService != null) {
            imageDownloadService.close();
        }
    }
}

