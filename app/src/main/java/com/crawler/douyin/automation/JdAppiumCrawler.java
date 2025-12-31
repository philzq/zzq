package com.crawler.douyin.automation;

import com.crawler.douyin.config.AppiumConfig;
import com.crawler.douyin.model.ProductInfo;
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
 * 使用Appium实现的京东APP自动化操作类
 * 通过模拟用户行为（滑动、点击、搜索等）来操作京东APP并获取商品信息
 * 
 * @author crawler
 */
public class JdAppiumCrawler {
    private static final Logger logger = LoggerFactory.getLogger(JdAppiumCrawler.class);
    
    private AppiumDriver driver;
    private final AppiumConfig config;
    private WebDriverWait wait;
    private final Random random = new Random();
    
    /**
     * 京东APP的元素定位器
     * 注意：元素ID可能需要根据实际APP版本调整，建议使用Appium Inspector查看
     */
    // 商品相关元素（使用XPath和文本定位，更通用）
    private static final By PRODUCT_TITLE = By.xpath("//android.widget.TextView[contains(@resource-id, 'title') or contains(@resource-id, 'name')]"); // 商品标题
    private static final By PRODUCT_PRICE = By.xpath("//android.widget.TextView[contains(@resource-id, 'price')]"); // 商品价格
    private static final By PRODUCT_SHOP = By.xpath("//android.widget.TextView[contains(@resource-id, 'shop') or contains(@resource-id, 'store')]"); // 店铺名称
    private static final By PRODUCT_COMMENT = By.xpath("//android.widget.TextView[contains(@text, '评价') or contains(@text, '条评价')]"); // 评价数
    private static final By PRODUCT_SALES = By.xpath("//android.widget.TextView[contains(@text, '销量') or contains(@text, '人付款')]"); // 销量
    
    // 搜索相关元素
    private static final By SEARCH_BOX = By.xpath("//android.widget.EditText[contains(@resource-id, 'search') or @hint='搜索商品']"); // 搜索框
    private static final By SEARCH_BUTTON = By.xpath("//android.widget.Button[@text='搜索'] or //android.widget.TextView[@text='搜索']"); // 搜索按钮
    
    // 底部导航栏
    private static final By HOME_TAB = By.xpath("//android.widget.TextView[@text='首页']"); // 首页
    private static final By CATEGORY_TAB = By.xpath("//android.widget.TextView[@text='分类']"); // 分类
    private static final By SHOPPING_CART_TAB = By.xpath("//android.widget.TextView[@text='购物车']"); // 购物车
    private static final By MINE_TAB = By.xpath("//android.widget.TextView[@text='我的']"); // 我的
    
    public JdAppiumCrawler(AppiumConfig config) {
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
            // 如果设置了Activity就使用，否则让Appium自动检测
            if (config.getAppActivity() != null && !config.getAppActivity().isEmpty()) {
                options.setAppActivity(config.getAppActivity());
            }
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
     * 向下滑动（查看更多商品）
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
     * 搜索商品
     * 
     * @param keyword 搜索关键词
     */
    public void searchProduct(String keyword) {
        try {
            logger.info("搜索商品: {}", keyword);
            
            // 查找搜索框并点击
            WebElement searchBox = driver.findElement(SEARCH_BOX);
            searchBox.click();
            randomDelay(1000, 2000);
            
            // 输入搜索关键词
            searchBox.clear();
            searchBox.sendKeys(keyword);
            randomDelay(500, 1000);
            
            // 点击搜索按钮
            try {
                List<WebElement> searchButtons = driver.findElements(SEARCH_BUTTON);
                if (!searchButtons.isEmpty()) {
                    searchButtons.get(0).click();
                } else {
                    // 如果找不到搜索按钮，尝试点击屏幕上的"搜索"文本
                    WebElement searchText = driver.findElement(By.xpath("//android.widget.TextView[@text='搜索']"));
                    searchText.click();
                }
            } catch (NoSuchElementException e) {
                logger.warn("未找到搜索按钮，等待用户手动操作或尝试其他方式");
                // 等待一段时间，可能需要手动点击搜索按钮
                randomDelay(2000, 3000);
            }
            
            randomDelay(2000, 3000);
            logger.info("搜索完成");
            
        } catch (Exception e) {
            logger.error("搜索商品失败", e);
        }
    }
    
    /**
     * 获取当前可见的商品信息列表
     * 
     * @return 商品信息列表
     */
    public List<ProductInfo> getCurrentProducts() {
        List<ProductInfo> productList = new ArrayList<>();
        
        try {
            // 等待页面加载
            randomDelay(2000, 3000);
            
            // 尝试查找所有商品标题元素
            List<WebElement> titleElements = driver.findElements(PRODUCT_TITLE);
            
            logger.debug("找到 {} 个可能的商品标题元素", titleElements.size());
            
            for (int i = 0; i < Math.min(titleElements.size(), 10); i++) { // 最多处理10个
                try {
                    WebElement titleElement = titleElements.get(i);
                    String title = titleElement.getText();
                    
                    if (title != null && !title.isEmpty() && title.length() > 3) {
                        ProductInfo product = new ProductInfo();
                        product.setTitle(title);
                        product.setProductId("product_" + System.currentTimeMillis() + "_" + i);
                        
                        // 尝试获取价格
                        try {
                            List<WebElement> priceElements = driver.findElements(PRODUCT_PRICE);
                            if (priceElements.size() > i) {
                                product.setPrice(priceElements.get(i).getText());
                            }
                        } catch (Exception e) {
                            logger.debug("未找到价格信息");
                        }
                        
                        // 尝试获取店铺名称
                        try {
                            List<WebElement> shopElements = driver.findElements(PRODUCT_SHOP);
                            if (shopElements.size() > i) {
                                product.setShopName(shopElements.get(i).getText());
                            }
                        } catch (Exception e) {
                            logger.debug("未找到店铺信息");
                        }
                        
                        // 尝试获取评价数
                        try {
                            List<WebElement> commentElements = driver.findElements(PRODUCT_COMMENT);
                            if (commentElements.size() > i) {
                                String commentText = commentElements.get(i).getText();
                                product.setCommentCount(parseCommentCount(commentText));
                            }
                        } catch (Exception e) {
                            logger.debug("未找到评价数");
                        }
                        
                        productList.add(product);
                        logger.debug("提取商品: {}", title);
                    }
                } catch (Exception e) {
                    logger.debug("处理第 {} 个元素失败", i, e);
                }
            }
            
        } catch (Exception e) {
            logger.error("获取商品列表失败", e);
        }
        
        return productList;
    }
    
    /**
     * 点击商品进入详情页
     * 
     * @param productTitle 商品标题
     */
    public void clickProduct(String productTitle) {
        try {
            WebElement productElement = driver.findElement(By.xpath("//android.widget.TextView[@text='" + productTitle + "']"));
            productElement.click();
            logger.debug("点击商品: {}", productTitle);
            randomDelay(2000, 3000);
        } catch (NoSuchElementException e) {
            logger.warn("未找到商品: {}", productTitle);
        }
    }
    
    /**
     * 获取商品详情页信息
     * 
     * @return 商品信息
     */
    public ProductInfo getProductDetail() {
        ProductInfo product = new ProductInfo();
        
        try {
            randomDelay(2000, 3000);
            
            // 获取标题
            try {
                List<WebElement> titleElements = driver.findElements(PRODUCT_TITLE);
                if (!titleElements.isEmpty()) {
                    product.setTitle(titleElements.get(0).getText());
                }
            } catch (Exception e) {
                logger.warn("未找到标题");
            }
            
            // 获取价格
            try {
                List<WebElement> priceElements = driver.findElements(PRODUCT_PRICE);
                if (!priceElements.isEmpty()) {
                    product.setPrice(priceElements.get(0).getText());
                }
            } catch (Exception e) {
                logger.warn("未找到价格");
            }
            
            // 获取店铺名称
            try {
                List<WebElement> shopElements = driver.findElements(PRODUCT_SHOP);
                if (!shopElements.isEmpty()) {
                    product.setShopName(shopElements.get(0).getText());
                }
            } catch (Exception e) {
                logger.warn("未找到店铺");
            }
            
            product.setProductId("detail_" + System.currentTimeMillis());
            
        } catch (Exception e) {
            logger.error("获取商品详情失败", e);
        }
        
        return product;
    }
    
    /**
     * 滑动并爬取多个商品
     * 
     * @param count 要爬取的商品数量
     * @return 商品信息列表
     */
    public List<ProductInfo> crawlProducts(int count) {
        logger.info("开始爬取 {} 个商品", count);
        List<ProductInfo> productList = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            try {
                logger.info("正在爬取第 {} 个商品", i + 1);
                
                // 获取当前可见的商品
                List<ProductInfo> currentProducts = getCurrentProducts();
                
                if (!currentProducts.isEmpty()) {
                    // 取第一个商品
                    ProductInfo product = currentProducts.get(0);
                    productList.add(product);
                    logger.info("获取商品: {}", product.getTitle());
                } else {
                    logger.warn("当前页面未找到商品");
                }
                
                // 滑动到下一个（最后一个不滑动）
                if (i < count - 1) {
                    swipeDown();
                }
                
            } catch (Exception e) {
                logger.error("爬取第 {} 个商品失败", i + 1, e);
            }
        }
        
        logger.info("爬取完成，共获取 {} 个商品", productList.size());
        return productList;
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
     * 点击底部导航栏的"分类"标签
     */
    public void clickCategoryTab() {
        try {
            WebElement categoryTab = driver.findElement(CATEGORY_TAB);
            categoryTab.click();
            logger.debug("点击分类标签");
            randomDelay(2000, 3000);
        } catch (NoSuchElementException e) {
            logger.warn("未找到分类标签");
        }
    }
    
    /**
     * 解析评价数文本（如：1.2万条评价 -> 12000）
     * 
     * @param commentText 评价数文本
     * @return 评价数
     */
    private Long parseCommentCount(String commentText) {
        if (commentText == null || commentText.isEmpty()) {
            return 0L;
        }
        
        try {
            // 移除空格和特殊字符
            commentText = commentText.trim().toLowerCase();
            
            if (commentText.contains("万")) {
                // 万
                double value = Double.parseDouble(commentText.replaceAll("[^0-9.]", "").replace("万", "").trim());
                return (long) (value * 10000);
            } else if (commentText.contains("k") || commentText.contains("千")) {
                // 千
                double value = Double.parseDouble(commentText.replaceAll("[^0-9.]", "").replace("k", "").replace("千", "").trim());
                return (long) (value * 1000);
            } else {
                // 提取数字
                String numbers = commentText.replaceAll("[^0-9]", "");
                if (!numbers.isEmpty()) {
                    return Long.parseLong(numbers);
                }
            }
        } catch (Exception e) {
            logger.warn("解析评价数失败: {}", commentText);
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

