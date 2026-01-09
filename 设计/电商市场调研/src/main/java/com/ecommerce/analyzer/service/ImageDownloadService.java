package com.ecommerce.analyzer.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 图片下载服务
 */
@Slf4j
public class ImageDownloadService {
    
    private static final String DOWNLOAD_DIR = "downloads";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
    private static final int CONNECT_TIMEOUT = 30000;
    private static final int READ_TIMEOUT = 30000;
    
    private final CloseableHttpClient httpClient;
    
    public ImageDownloadService() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(READ_TIMEOUT)
                .setConnectionRequestTimeout(CONNECT_TIMEOUT)
                .build();
        
        this.httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
    }
    
    /**
     * 下载图片到本地
     * 按照产品ID、主图、详情页图分开保存
     * 保存路径：downloads/{productId}/main/ 或 downloads/{productId}/detail/
     */
    public String downloadImage(String imageUrl, String productId, String imageType) throws IOException {
        // 创建下载目录，按照产品ID和图片类型分开保存
        // downloads/{productId}/main/ 或 downloads/{productId}/detail/
        String downloadPath = DOWNLOAD_DIR + File.separator + productId + File.separator + imageType;
        File dir = new File(downloadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        // 生成文件名（路径已区分类型，文件名不需要类型前缀）
        String fileName = generateFileName(imageUrl, imageType);
        String filePath = downloadPath + File.separator + fileName;
        
        log.info("正在下载图片: {} -> {}", imageUrl, filePath);
        
        try {
            // 使用HttpClient下载
            HttpGet request = new HttpGet(imageUrl);
            request.setHeader("User-Agent", USER_AGENT);
            request.setHeader("Referer", "https://www.coupang.com/");
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    try (InputStream inputStream = entity.getContent();
                         FileOutputStream outputStream = new FileOutputStream(filePath)) {
                        
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }
                    
                    log.info("图片下载成功: {}", filePath);
                    return filePath;
                }
            }
        } catch (Exception e) {
            log.warn("使用HttpClient下载失败，尝试备用方法: {}", e.getMessage());
            // 备用方法：使用FileUtils
            try {
                FileUtils.copyURLToFile(new URL(imageUrl), new File(filePath), CONNECT_TIMEOUT, READ_TIMEOUT);
                log.info("图片下载成功（备用方法）: {}", filePath);
                return filePath;
            } catch (Exception e2) {
                log.error("图片下载失败: {}", imageUrl, e2);
                throw new IOException("下载图片失败: " + imageUrl, e2);
            }
        }
        
        throw new IOException("下载图片失败: " + imageUrl);
    }
    
    /**
     * 批量下载图片
     * 按照产品ID、主图、详情页图分开保存
     */
    public List<String> downloadImages(List<String> imageUrls, String productId, String imageType) {
        List<String> downloadedPaths = new ArrayList<>();
        
        for (int i = 0; i < imageUrls.size(); i++) {
            String imageUrl = imageUrls.get(i);
            try {
                // imageType 参数：main 或 detail
                String filePath = downloadImage(imageUrl, productId, imageType);
                downloadedPaths.add(filePath);
                
                // 添加延迟，避免请求过快
                Thread.sleep(500);
            } catch (Exception e) {
                log.error("下载图片失败: {}", imageUrl, e);
                // 继续下载其他图片
            }
        }
        
        log.info("批量下载完成: {} / {} 张{}图片下载成功，保存在 downloads/{}/{}", 
                downloadedPaths.size(), imageUrls.size(), imageType, productId, imageType);
        return downloadedPaths;
    }
    
    /**
     * 生成文件名
     * 由于图片已按路径分类（downloads/{productId}/main/ 或 detail/），文件名不再需要类型前缀
     */
    private String generateFileName(String imageUrl, String imageType) {
        try {
            // 尝试从URL中提取文件名
            String urlPath = new URL(imageUrl).getPath();
            String fileName = urlPath.substring(urlPath.lastIndexOf('/') + 1);
            
            // 移除查询参数
            if (fileName.contains("?")) {
                fileName = fileName.substring(0, fileName.indexOf('?'));
            }
            
            // 如果没有扩展名，添加.jpg
            if (!fileName.contains(".")) {
                // 生成唯一文件名，使用时间戳和随机数
                fileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8) + ".jpg";
            }
            
            // 如果文件名为空或无效，生成随机文件名
            if (fileName.isEmpty() || fileName.length() < 5) {
                fileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8) + ".jpg";
            }
            
            // 清理文件名（移除特殊字符，保留扩展名）
            String extension = "";
            int lastDot = fileName.lastIndexOf('.');
            if (lastDot > 0) {
                extension = fileName.substring(lastDot);
                fileName = fileName.substring(0, lastDot);
            }
            fileName = fileName.replaceAll("[^a-zA-Z0-9_-]", "_") + extension;
            
            return fileName;
        } catch (Exception e) {
            // 如果解析URL失败，生成随机文件名
            return System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8) + ".jpg";
        }
    }
    
    /**
     * 清理下载的图片
     */
    public void cleanup(String productId) {
        try {
            String downloadPath = DOWNLOAD_DIR + File.separator + productId;
            File dir = new File(downloadPath);
            if (dir.exists() && dir.isDirectory()) {
                FileUtils.deleteDirectory(dir);
                log.info("已清理下载文件: {}", downloadPath);
            }
        } catch (IOException e) {
            log.warn("清理下载文件失败: {}", e.getMessage());
        }
    }
    
    /**
     * 关闭HttpClient
     */
    public void close() {
        try {
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (IOException e) {
            log.warn("关闭HttpClient失败: {}", e.getMessage());
        }
    }
}

