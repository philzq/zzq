package com.crawler.douyin.automation;

import com.crawler.douyin.config.AppiumConfig;
import com.crawler.douyin.model.ArticleInfo;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 使用Appium实现的今日头条APP自动化操作类
 * 通过模拟用户行为（滑动、点击等）来爬取今日头条文章
 * 
 * @author crawler
 */
public class ToutiaoAppiumCrawler {
    private static final Logger logger = LoggerFactory.getLogger(ToutiaoAppiumCrawler.class);
    
    private AppiumDriver driver;
    private final AppiumConfig config;
    private WebDriverWait wait;
    private final Random random = new Random();
    
    /**
     * 今日头条APP的元素定位器
     * 注意：元素ID可能需要根据实际APP版本调整，建议使用Appium Inspector查看
     */
    // 文章列表相关元素（使用XPath和文本定位，更通用）
    private static final By ARTICLE_TITLE = By.xpath("//android.widget.TextView[contains(@resource-id, 'title') or contains(@text, '')]"); // 文章标题
    private static final By ARTICLE_AUTHOR = By.xpath("//android.widget.TextView[contains(@resource-id, 'author') or contains(@resource-id, 'source')]"); // 作者
    private static final By ARTICLE_SUMMARY = By.xpath("//android.widget.TextView[contains(@resource-id, 'summary') or contains(@resource-id, 'abstract')]"); // 文章摘要
    private static final By ARTICLE_READ_COUNT = By.xpath("//android.widget.TextView[contains(@resource-id, 'read') or contains(@text, '阅读')]"); // 阅读数
    private static final By ARTICLE_COMMENT_COUNT = By.xpath("//android.widget.TextView[contains(@resource-id, 'comment') or contains(@text, '评论')]"); // 评论数
    private static final By ARTICLE_LIKE_COUNT = By.xpath("//android.widget.TextView[contains(@resource-id, 'like') or contains(@text, '赞')]"); // 点赞数
    
    // 底部导航栏
    private static final By HOME_TAB = By.xpath("//android.widget.TextView[@text='首页' or @text='推荐']"); // 首页/推荐
    private static final By VIDEO_TAB = By.xpath("//android.widget.TextView[@text='视频']"); // 视频
    private static final By MICRO_TAB = By.xpath("//android.widget.TextView[@text='微头条']"); // 微头条
    private static final By USER_TAB = By.xpath("//android.widget.TextView[@text='我的']"); // 我的
    
    public ToutiaoAppiumCrawler(AppiumConfig config) {
        this.config = config;
    }
    
    /**
     * 初始化Appium驱动
     */
    public void initDriver() {
        try {
            logger.info("正在连接Appium服务器: {}", config.getServerUrl());
            
            UiAutomator2Options options = new UiAutomator2Options();
            options.setPlatformName("Android");
            options.setPlatformVersion(config.getPlatformVersion());
            options.setDeviceName(config.getDeviceName());
            
            if (config.getDeviceUdid() != null && !config.getDeviceUdid().isEmpty()) {
                options.setUdid(config.getDeviceUdid());
            }
            
            options.setAppPackage(config.getAppPackage());
            options.setAppActivity(config.getAppActivity());
            options.setNewCommandTimeout(Duration.ofSeconds(config.getNewCommandTimeout()));
            options.setNoReset(config.isNoReset());
            options.setSkipServerInstallation(config.isSkipServerInstallation());
            options.setAutomationName(config.getAutomationName());
            
            driver = new AndroidDriver(new java.net.URL(config.getServerUrl()), options);
            
            // 初始化等待对象
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            
            logger.info("Appium驱动初始化成功");
            
            // 等待APP启动
            randomDelay(3000, 5000);
            
        } catch (Exception e) {
            logger.error("初始化Appium驱动失败", e);
            throw new RuntimeException("初始化Appium驱动失败", e);
        }
    }
    
    /**
     * 关闭驱动
     */
    public void close() {
        if (driver != null) {
            try {
                driver.quit();
                logger.info("Appium驱动已关闭");
            } catch (Exception e) {
                logger.error("关闭驱动失败", e);
            }
        }
    }
    
    /**
     * 向下滑动（查看更多文章）
     */
    public void swipeDown() {
        try {
            Dimension size = driver.manage().window().getSize();
            int startX = size.width / 2;
            int startY = (int) (size.height * 0.7);
            int endY = (int) (size.height * 0.3);
            
            // 添加随机偏移，模拟人类操作
            startX += random.nextInt(100) - 50;
            
            logger.debug("向下滑动");
            
            // 使用PointerInput进行滑动
            org.openqa.selenium.interactions.PointerInput finger = 
                new org.openqa.selenium.interactions.PointerInput(
                    org.openqa.selenium.interactions.PointerInput.Kind.TOUCH, "finger");
            org.openqa.selenium.interactions.Sequence swipe = new org.openqa.selenium.interactions.Sequence(finger, 1);
            swipe.addAction(finger.createPointerMove(
                Duration.ofMillis(0),
                org.openqa.selenium.interactions.PointerInput.Origin.viewport(), startX, startY));
            swipe.addAction(finger.createPointerDown(org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
            swipe.addAction(finger.createPointerMove(
                Duration.ofMillis(500),
                org.openqa.selenium.interactions.PointerInput.Origin.viewport(), startX, endY));
            swipe.addAction(finger.createPointerUp(org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(java.util.Collections.singletonList(swipe));
            
            randomDelay(1500, 2500);
        } catch (Exception e) {
            logger.error("滑动失败", e);
        }
    }
    
    /**
     * 向上滑动（返回上一页）
     */
    public void swipeUp() {
        try {
            Dimension size = driver.manage().window().getSize();
            int startX = size.width / 2;
            int startY = (int) (size.height * 0.3);
            int endY = (int) (size.height * 0.7);
            
            startX += random.nextInt(100) - 50;
            
            logger.debug("向上滑动");
            
            org.openqa.selenium.interactions.PointerInput finger = 
                new org.openqa.selenium.interactions.PointerInput(
                    org.openqa.selenium.interactions.PointerInput.Kind.TOUCH, "finger");
            org.openqa.selenium.interactions.Sequence swipe = new org.openqa.selenium.interactions.Sequence(finger, 1);
            swipe.addAction(finger.createPointerMove(
                Duration.ofMillis(0),
                org.openqa.selenium.interactions.PointerInput.Origin.viewport(), startX, startY));
            swipe.addAction(finger.createPointerDown(org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
            swipe.addAction(finger.createPointerMove(
                Duration.ofMillis(500),
                org.openqa.selenium.interactions.PointerInput.Origin.viewport(), startX, endY));
            swipe.addAction(finger.createPointerUp(org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(java.util.Collections.singletonList(swipe));
            
            randomDelay(1500, 2500);
        } catch (Exception e) {
            logger.error("滑动失败", e);
        }
    }
    
    /**
     * 获取当前可见的文章信息列表
     * 
     * @return 文章信息列表
     */
    public List<ArticleInfo> getCurrentArticles() {
        List<ArticleInfo> articleList = new ArrayList<>();
        
        try {
            // 等待页面加载
            randomDelay(2000, 3000);
            
            // 尝试查找所有文章标题元素
            List<WebElement> titleElements = driver.findElements(ARTICLE_TITLE);
            
            logger.debug("找到 {} 个可能的文章标题元素", titleElements.size());
            
            for (int i = 0; i < Math.min(titleElements.size(), 5); i++) { // 最多处理5个
                try {
                    WebElement titleElement = titleElements.get(i);
                    String title = titleElement.getText();
                    
                    if (title != null && !title.isEmpty() && title.length() > 5) {
                        ArticleInfo article = new ArticleInfo();
                        article.setTitle(title);
                        article.setArticleId("article_" + System.currentTimeMillis() + "_" + i);
                        
                        // 尝试获取作者
                        try {
                            WebElement authorElement = titleElement.findElement(By.xpath("./following-sibling::*//android.widget.TextView[contains(@resource-id, 'author') or contains(@resource-id, 'source')]"));
                            article.setAuthor(authorElement.getText());
                        } catch (Exception e) {
                            logger.debug("未找到作者信息");
                        }
                        
                        // 尝试获取阅读数
                        try {
                            WebElement readElement = titleElement.findElement(By.xpath("./following-sibling::*//android.widget.TextView[contains(@text, '阅读') or contains(@text, '万')]"));
                            String readText = readElement.getText();
                            article.setReadCount(parseCount(readText));
                        } catch (Exception e) {
                            logger.debug("未找到阅读数");
                        }
                        
                        // 尝试获取评论数
                        try {
                            WebElement commentElement = titleElement.findElement(By.xpath("./following-sibling::*//android.widget.TextView[contains(@text, '评论')]"));
                            String commentText = commentElement.getText();
                            article.setCommentCount(parseCount(commentText));
                        } catch (Exception e) {
                            logger.debug("未找到评论数");
                        }
                        
                        articleList.add(article);
                        logger.debug("提取文章: {}", title);
                    }
                } catch (Exception e) {
                    logger.debug("处理第 {} 个元素失败", i, e);
                }
            }
            
        } catch (Exception e) {
            logger.error("获取文章列表失败", e);
        }
        
        return articleList;
    }
    
    /**
     * 点击文章进入详情页
     * 
     * @param articleTitle 文章标题
     */
    public void clickArticle(String articleTitle) {
        try {
            WebElement articleElement = driver.findElement(By.xpath("//android.widget.TextView[@text='" + articleTitle + "']"));
            articleElement.click();
            logger.debug("点击文章: {}", articleTitle);
            randomDelay(2000, 3000);
        } catch (NoSuchElementException e) {
            logger.warn("未找到文章: {}", articleTitle);
        }
    }
    
    /**
     * 获取文章详情页信息
     * 
     * @return 文章信息
     */
    public ArticleInfo getArticleDetail() {
        ArticleInfo article = new ArticleInfo();
        
        try {
            randomDelay(2000, 3000);
            
            // 获取标题
            try {
                List<WebElement> titleElements = driver.findElements(ARTICLE_TITLE);
                if (!titleElements.isEmpty()) {
                    article.setTitle(titleElements.get(0).getText());
                }
            } catch (Exception e) {
                logger.warn("未找到标题");
            }
            
            // 获取作者
            try {
                List<WebElement> authorElements = driver.findElements(ARTICLE_AUTHOR);
                if (!authorElements.isEmpty()) {
                    article.setAuthor(authorElements.get(0).getText());
                }
            } catch (Exception e) {
                logger.warn("未找到作者");
            }
            
            // 获取阅读数
            try {
                List<WebElement> readElements = driver.findElements(ARTICLE_READ_COUNT);
                if (!readElements.isEmpty()) {
                    article.setReadCount(parseCount(readElements.get(0).getText()));
                }
            } catch (Exception e) {
                logger.warn("未找到阅读数");
            }
            
            // 获取评论数
            try {
                List<WebElement> commentElements = driver.findElements(ARTICLE_COMMENT_COUNT);
                if (!commentElements.isEmpty()) {
                    article.setCommentCount(parseCount(commentElements.get(0).getText()));
                }
            } catch (Exception e) {
                logger.warn("未找到评论数");
            }
            
            article.setArticleId("detail_" + System.currentTimeMillis());
            
        } catch (Exception e) {
            logger.error("获取文章详情失败", e);
        }
        
        return article;
    }
    
    /**
     * 滑动并爬取多个文章
     * 
     * @param count 要爬取的文章数量
     * @return 文章信息列表
     */
    public List<ArticleInfo> crawlArticles(int count) {
        logger.info("开始爬取 {} 篇文章", count);
        List<ArticleInfo> articleList = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            try {
                logger.info("正在爬取第 {} 篇文章", i + 1);
                
                // 获取当前可见的文章
                List<ArticleInfo> currentArticles = getCurrentArticles();
                
                if (!currentArticles.isEmpty()) {
                    // 取第一个文章
                    ArticleInfo article = currentArticles.get(0);
                    articleList.add(article);
                    logger.info("获取文章: {}", article.getTitle());
                } else {
                    logger.warn("当前页面未找到文章");
                }
                
                // 滑动到下一个（最后一个不滑动）
                if (i < count - 1) {
                    swipeDown();
                }
                
            } catch (Exception e) {
                logger.error("爬取第 {} 篇文章失败", i + 1, e);
            }
        }
        
        logger.info("爬取完成，共获取 {} 篇文章", articleList.size());
        return articleList;
    }
    
    /**
     * 返回上一页
     */
    public void goBack() {
        try {
            driver.navigate().back();
            logger.debug("返回上一页");
            randomDelay(1000, 2000);
        } catch (Exception e) {
            logger.error("返回失败", e);
        }
    }
    
    /**
     * 点击底部导航栏的"首页"标签
     */
    public void clickHomeTab() {
        try {
            WebElement homeTab = driver.findElement(HOME_TAB);
            homeTab.click();
            logger.debug("点击首页标签");
            randomDelay(2000, 3000);
        } catch (NoSuchElementException e) {
            logger.warn("未找到首页标签");
        }
    }
    
    /**
     * 解析数量文本（如：1.2万 -> 12000）
     * 
     * @param countText 数量文本
     * @return 数量
     */
    private Long parseCount(String countText) {
        if (countText == null || countText.isEmpty()) {
            return 0L;
        }
        
        try {
            // 移除空格和特殊字符
            countText = countText.trim().toLowerCase();
            
            if (countText.contains("万")) {
                // 万
                double value = Double.parseDouble(countText.replace("万", "").trim());
                return (long) (value * 10000);
            } else if (countText.contains("k") || countText.contains("千")) {
                // 千
                double value = Double.parseDouble(countText.replace("k", "").replace("千", "").trim());
                return (long) (value * 1000);
            } else {
                // 提取数字
                String numbers = countText.replaceAll("[^0-9.]", "");
                if (!numbers.isEmpty()) {
                    return Long.parseLong(numbers.split("\\.")[0]);
                }
            }
        } catch (Exception e) {
            logger.warn("解析数量失败: {}", countText);
        }
        
        return 0L;
    }
    
    /**
     * 等待指定时间（随机延迟，模拟人类操作）
     * 
     * @param min 最小延迟（毫秒）
     * @param max 最大延迟（毫秒）
     */
    private void randomDelay(int min, int max) {
        try {
            int delay = random.nextInt(max - min + 1) + min;
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 获取驱动实例（用于高级操作）
     * 
     * @return AppiumDriver实例
     */
    public AppiumDriver getDriver() {
        return driver;
    }
}

