package com.crawler.douyin.automation;

import com.crawler.douyin.config.AppiumConfig;
import com.crawler.douyin.model.VideoInfo;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 使用Appium实现的抖音APP自动化操作类
 * 通过模拟用户行为（滑动、点击等）来操作抖音APP
 * 
 * @author crawler
 */
public class DouyinAppiumCrawler {
    private static final Logger logger = LoggerFactory.getLogger(DouyinAppiumCrawler.class);
    
    private AppiumDriver driver;
    private final AppiumConfig config;
    private WebDriverWait wait;
    private final Random random = new Random();
    
    /**
     * 抖音APP的元素定位器（可能需要根据实际APP版本调整）
     */
    // 视频相关元素
    private static final By VIDEO_CONTAINER = By.id("com.ss.android.ugc.aweme:id/aef"); // 视频容器
    private static final By VIDEO_AUTHOR = By.id("com.ss.android.ugc.aweme:id/title"); // 作者名称
    private static final By VIDEO_DESC = By.id("com.ss.android.ugc.aweme:id/awf"); // 视频描述
    private static final By LIKE_BUTTON = By.id("com.ss.android.ugc.aweme:id/b4c"); // 点赞按钮
    private static final By LIKE_COUNT = By.id("com.ss.android.ugc.aweme:id/b4e"); // 点赞数
    private static final By COMMENT_BUTTON = By.id("com.ss.android.ugc.aweme:id/b4d"); // 评论按钮
    private static final By COMMENT_COUNT = By.id("com.ss.android.ugc.aweme:id/b4f"); // 评论数
    
    // 底部导航栏
    private static final By HOME_TAB = By.xpath("//android.widget.TextView[@text='首页']"); // 首页
    private static final By SEARCH_TAB = By.xpath("//android.widget.TextView[@text='搜索']"); // 搜索
    private static final By USER_TAB = By.xpath("//android.widget.TextView[@text='我']"); // 我的
    
    public DouyinAppiumCrawler(AppiumConfig config) {
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
            // setAutoLaunch方法在Appium 8.x中已移除，默认会自动启动APP
            // options.setAutoLaunch(config.isAutoLaunch());
            options.setNewCommandTimeout(Duration.ofSeconds(config.getNewCommandTimeout()));
            options.setNoReset(config.isNoReset());
            options.setSkipServerInstallation(config.isSkipServerInstallation());
            options.setAutomationName(config.getAutomationName());
            
            driver = new AndroidDriver(new java.net.URL(config.getServerUrl()), options);
            
            // 初始化等待对象
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            
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
     * 向上滑动视频（切换到下一个视频）
     */
    public void swipeUp() {
        try {
            Dimension size = driver.manage().window().getSize();
            int startX = size.width / 2;
            int startY = (int) (size.height * 0.8);
            int endY = (int) (size.height * 0.2);
            
            // 添加随机偏移，模拟人类操作
            startX += random.nextInt(100) - 50;
            
            logger.debug("向上滑动: ({}, {}) -> ({}, {})", startX, startY, startX, endY);
            
            // 使用PointerInput进行滑动（Appium 8.x推荐方式）
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
            
            randomDelay(1000, 2000);
        } catch (Exception e) {
            logger.error("滑动失败", e);
        }
    }
    
    /**
     * 向下滑动视频（切换到上一个视频）
     */
    public void swipeDown() {
        try {
            Dimension size = driver.manage().window().getSize();
            int startX = size.width / 2;
            int startY = (int) (size.height * 0.2);
            int endY = (int) (size.height * 0.8);
            
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
            
            randomDelay(1000, 2000);
        } catch (Exception e) {
            logger.error("滑动失败", e);
        }
    }
    
    /**
     * 获取当前视频信息
     * 
     * @return 视频信息
     */
    public VideoInfo getCurrentVideoInfo() {
        VideoInfo videoInfo = new VideoInfo();
        
        try {
            // 等待视频加载
            randomDelay(1500, 2500);
            
            // 获取作者名称
            try {
                WebElement authorElement = driver.findElement(VIDEO_AUTHOR);
                videoInfo.setAuthor(authorElement.getText());
                logger.debug("作者: {}", videoInfo.getAuthor());
            } catch (NoSuchElementException e) {
                logger.warn("未找到作者元素");
            }
            
            // 获取视频描述
            try {
                WebElement descElement = driver.findElement(VIDEO_DESC);
                videoInfo.setTitle(descElement.getText());
                logger.debug("描述: {}", videoInfo.getTitle());
            } catch (NoSuchElementException e) {
                logger.warn("未找到描述元素");
            }
            
            // 获取点赞数
            try {
                WebElement likeCountElement = driver.findElement(LIKE_COUNT);
                String likeText = likeCountElement.getText();
                videoInfo.setLikeCount(parseCount(likeText));
                logger.debug("点赞数: {}", videoInfo.getLikeCount());
            } catch (NoSuchElementException e) {
                logger.warn("未找到点赞数元素");
            }
            
            // 获取评论数
            try {
                WebElement commentCountElement = driver.findElement(COMMENT_COUNT);
                String commentText = commentCountElement.getText();
                videoInfo.setCommentCount(parseCount(commentText));
                logger.debug("评论数: {}", videoInfo.getCommentCount());
            } catch (NoSuchElementException e) {
                logger.warn("未找到评论数元素");
            }
            
            // 生成视频ID（使用时间戳作为临时ID）
            videoInfo.setVideoId("appium_" + System.currentTimeMillis());
            
        } catch (Exception e) {
            logger.error("获取视频信息失败", e);
        }
        
        return videoInfo;
    }
    
    /**
     * 解析数量文本（如：1.2w -> 12000）
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
            
            if (countText.contains("w")) {
                // 万
                double value = Double.parseDouble(countText.replace("w", "").trim());
                return (long) (value * 10000);
            } else if (countText.contains("k")) {
                // 千
                double value = Double.parseDouble(countText.replace("k", "").trim());
                return (long) (value * 1000);
            } else {
                return Long.parseLong(countText.replaceAll("[^0-9]", ""));
            }
        } catch (Exception e) {
            logger.warn("解析数量失败: {}", countText);
            return 0L;
        }
    }
    
    /**
     * 滑动并获取多个视频信息
     * 
     * @param count 要获取的视频数量
     * @return 视频信息列表
     */
    public List<VideoInfo> crawlVideos(int count) {
        logger.info("开始爬取 {} 个视频", count);
        List<VideoInfo> videoList = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            try {
                logger.info("正在爬取第 {} 个视频", i + 1);
                
                // 获取当前视频信息
                VideoInfo videoInfo = getCurrentVideoInfo();
                videoList.add(videoInfo);
                
                // 滑动到下一个视频（最后一个不滑动）
                if (i < count - 1) {
                    swipeUp();
                }
                
            } catch (Exception e) {
                logger.error("爬取第 {} 个视频失败", i + 1, e);
            }
        }
        
        logger.info("爬取完成，共获取 {} 个视频", videoList.size());
        return videoList;
    }
    
    /**
     * 点击点赞按钮
     */
    public void likeVideo() {
        try {
            WebElement likeButton = driver.findElement(LIKE_BUTTON);
            likeButton.click();
            logger.debug("点击点赞按钮");
            randomDelay(500, 1000);
        } catch (NoSuchElementException e) {
            logger.warn("未找到点赞按钮");
        }
    }
    
    /**
     * 点击评论按钮
     */
    public void openComments() {
        try {
            WebElement commentButton = driver.findElement(COMMENT_BUTTON);
            commentButton.click();
            logger.debug("点击评论按钮");
            randomDelay(1000, 2000);
        } catch (NoSuchElementException e) {
            logger.warn("未找到评论按钮");
        }
    }
    
    /**
     * 返回上一页
     */
    public void goBack() {
        try {
            driver.navigate().back();
            logger.debug("返回上一页");
            randomDelay(500, 1000);
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
            randomDelay(1000, 2000);
        } catch (NoSuchElementException e) {
            logger.warn("未找到首页标签");
        }
    }
    
    /**
     * 点击底部导航栏的"搜索"标签
     */
    public void clickSearchTab() {
        try {
            WebElement searchTab = driver.findElement(SEARCH_TAB);
            searchTab.click();
            logger.debug("点击搜索标签");
            randomDelay(1000, 2000);
        } catch (NoSuchElementException e) {
            logger.warn("未找到搜索标签");
        }
    }
    
    /**
     * 点击底部导航栏的"我"标签
     */
    public void clickUserTab() {
        try {
            WebElement userTab = driver.findElement(USER_TAB);
            userTab.click();
            logger.debug("点击用户标签");
            randomDelay(1000, 2000);
        } catch (NoSuchElementException e) {
            logger.warn("未找到用户标签");
        }
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

