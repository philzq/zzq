package com.crawler.douyin;

import com.crawler.douyin.automation.JdAppiumCrawler;
import com.crawler.douyin.config.AppiumConfig;
import com.crawler.douyin.model.ProductInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 京东APP自动化爬虫Demo主程序入口
 * 通过Appium模拟用户行为（滑动、点击、搜索等）来操作京东APP并获取商品信息
 * 
 * @author crawler
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("京东APP自动化爬虫Demo启动...");

        AppiumConfig config = new AppiumConfig();
        
        // 配置Appium连接参数
        // 注意：需要根据实际情况修改以下参数
        // config.setDeviceUdid("设备UDID");  // 可通过adb devices查看
        config.setPlatformVersion("11.0");  // Android版本
        config.setDeviceName("Android Device");
        
        JdAppiumCrawler crawler = new JdAppiumCrawler(config);

        try {
            // 初始化Appium驱动（会启动京东APP）
            logger.info("正在初始化Appium连接...");
            crawler.initDriver();
            logger.info("Appium连接成功，京东APP已启动");

            // 等待APP完全加载
            Thread.sleep(5000);

            // 示例1：搜索商品
            logger.info("=== 示例1：搜索商品 ===");
            crawler.searchProduct("手机");
            Thread.sleep(3000);

            // 示例2：获取当前可见的商品列表
            logger.info("=== 示例2：获取当前可见的商品列表 ===");
            List<ProductInfo> currentProducts = crawler.getCurrentProducts();
            logger.info("当前可见商品数: {}", currentProducts.size());
            for (ProductInfo product : currentProducts) {
                logger.info("商品名称: {}", product.getTitle());
                logger.info("  价格: {}", product.getPrice());
                logger.info("  店铺: {}", product.getShopName());
                logger.info("  评价数: {}", product.getCommentCount());
                logger.info("---");
            }
            
            Thread.sleep(2000);

            // 示例3：滑动并爬取多个商品
            logger.info("=== 示例3：滑动并爬取10个商品 ===");
            List<ProductInfo> productList = crawler.crawlProducts(10);
            
            if (productList != null && !productList.isEmpty()) {
                logger.info("成功爬取 {} 个商品", productList.size());
                for (int i = 0; i < productList.size(); i++) {
                    ProductInfo product = productList.get(i);
                    logger.info("商品 {}:", i + 1);
                    logger.info("  名称: {}", product.getTitle());
                    logger.info("  价格: {}", product.getPrice());
                    logger.info("  店铺: {}", product.getShopName());
                    logger.info("  评价数: {}", product.getCommentCount());
                    logger.info("---");
                }
            } else {
                logger.warn("未爬取到数据");
            }

            // 示例4：点击商品查看详情（可选）
            // logger.info("=== 示例4：点击商品查看详情 ===");
            // if (!productList.isEmpty()) {
            //     crawler.clickProduct(productList.get(0).getTitle());
            //     ProductInfo detail = crawler.getProductDetail();
            //     logger.info("商品详情: {}", detail);
            //     crawler.goBack();
            // }

        } catch (Exception e) {
            logger.error("爬虫执行出错", e);
        } finally {
            // 关闭驱动（关闭APP连接）
            logger.info("正在关闭Appium连接...");
            crawler.close();
        }

        logger.info("程序执行完成");
    }
}

