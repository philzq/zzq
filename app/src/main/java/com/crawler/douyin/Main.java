package com.crawler.douyin;

import com.crawler.douyin.automation.ToutiaoAppiumCrawler;
import com.crawler.douyin.config.AppiumConfig;
import com.crawler.douyin.model.ArticleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 今日头条APP自动化爬虫Demo主程序入口
 * 通过Appium模拟用户行为（滑动、点击等）来爬取今日头条文章
 * 
 * @author crawler
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("今日头条APP自动化爬虫Demo启动...");

        AppiumConfig config = new AppiumConfig();
        
        // 配置Appium连接参数
        // 注意：需要根据实际情况修改以下参数
        // config.setDeviceUdid("设备UDID");  // 可通过adb devices查看
        config.setPlatformVersion("11.0");  // Android版本
        config.setDeviceName("Android Device");
        
        ToutiaoAppiumCrawler crawler = new ToutiaoAppiumCrawler(config);

        try {
            // 初始化Appium驱动（会启动今日头条APP）
            logger.info("正在初始化Appium连接...");
            crawler.initDriver();
            logger.info("Appium连接成功，今日头条APP已启动");

            // 等待APP完全加载
            Thread.sleep(5000);

            // 示例1：获取当前可见的文章列表
            logger.info("=== 示例1：获取当前可见的文章列表 ===");
            List<ArticleInfo> currentArticles = crawler.getCurrentArticles();
            logger.info("当前可见文章数: {}", currentArticles.size());
            for (ArticleInfo article : currentArticles) {
                logger.info("文章标题: {}", article.getTitle());
                logger.info("  作者: {}", article.getAuthor());
                logger.info("  阅读数: {}", article.getReadCount());
                logger.info("  评论数: {}", article.getCommentCount());
                logger.info("---");
            }
            
            Thread.sleep(2000);

            // 示例2：滑动并爬取多个文章
            logger.info("=== 示例2：滑动并爬取10篇文章 ===");
            List<ArticleInfo> articleList = crawler.crawlArticles(10);
            
            if (articleList != null && !articleList.isEmpty()) {
                logger.info("成功爬取 {} 篇文章", articleList.size());
                for (int i = 0; i < articleList.size(); i++) {
                    ArticleInfo article = articleList.get(i);
                    logger.info("文章 {}:", i + 1);
                    logger.info("  标题: {}", article.getTitle());
                    logger.info("  作者: {}", article.getAuthor());
                    logger.info("  阅读数: {}", article.getReadCount());
                    logger.info("  评论数: {}", article.getCommentCount());
                    logger.info("---");
                }
            } else {
                logger.warn("未爬取到数据");
            }

            // 示例3：点击文章进入详情页（可选）
            // logger.info("=== 示例3：点击文章查看详情 ===");
            // if (!articleList.isEmpty()) {
            //     crawler.clickArticle(articleList.get(0).getTitle());
            //     ArticleInfo detail = crawler.getArticleDetail();
            //     logger.info("文章详情: {}", detail);
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

