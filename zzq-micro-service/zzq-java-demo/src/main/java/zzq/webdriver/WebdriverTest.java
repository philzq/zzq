package zzq.webdriver;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.val;
import org.apache.commons.lang3.ObjectUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class WebdriverTest {

    public static void main(String[] args) throws Exception{
        Map<String,Object> startParam = new HashMap<String,Object>();
        startParam.put("containerCode", "1354389183");
        String body = HttpUtil.createPost("http://127.0.0.1:6873/api/v1/browser/start").body(JSONUtil.toJsonStr(startParam)).execute().body();
        System.out.println(body);
        StartRS startRS = JSONUtil.toBean(body, StartRS.class);
        if (startRS.getCode() != 0
                || ObjectUtils.isEmpty(startRS.getData().getWebdriver())
                || ObjectUtils.isEmpty(startRS.getData().getDebuggingPort())
        ) {
            return;
        }
        //更新环境代理
        /*Map<String,Object> updateProxyParam = new HashMap<String,Object>();
        startParam.put("containerCode", "1354389183");
        String updateProxyBody = HttpUtil.createPost("http://127.0.0.1:6873/api/v1/env/proxy/update").body(JSONUtil.toJsonStr(updateProxyParam)).execute().body();
        System.out.println(updateProxyBody);*/
        //浏览页面
        String webdriver = startRS.getData().getWebdriver();
        String debuggingPort = startRS.getData().getDebuggingPort();
        // 获取webdriver
        System.setProperty("webdriver.chrome.driver", webdriver); // 填写打开环境返回的webdriver参数
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("debuggerAddress", "127.0.0.1:" + debuggingPort); // 填写打开环境的debuggingPort参数
        ChromeDriver driver = new ChromeDriver(chromeOptions);
        driver.get("https://www.coupang.com/");
        WebElement elementByXPath = driver.findElementByCssSelector("#wa-search-form-tablet > div.header-searchForm.fw-relative.fw-flex-1 > input.headerSearchKeyword.coupang-search.fw-h-full.fw-w-full.fw-bg-white.fw-indent-\\[10px\\].fw-text-\\[14px\\].fw-outline-none.rlux\\:fw-text-bluegray-1000.rlux\\:placeholder\\:fw-text-bluegray-800.is-speech");
        elementByXPath.sendKeys("드래곤볼 제일복권");
        WebElement submit = driver.findElementByCssSelector("#wa-search-form-tablet > button");
        submit.click();
        WebElement product1 = driver.findElementByCssSelector("#product-list > li:nth-child(1)");
        clickAndSwitchToNewWindow(driver,product1,100);
        //商品组图详情页浏览
        List<WebElement> elementsByCssSelector = driver.findElementsByCssSelector("body > div:nth-child(5) > div > div.twc-flex.twc-max-w-full > main > div.prod-atf.twc-block.md\\:twc-flex.twc-relative > div.product-image.twc-flex-1.md\\:twc-flex > div.twc-w-\\[70px\\].twc-relative > ul > li");
        for (WebElement element : elementsByCssSelector) {
            element.click();
            Thread.sleep(500);
        }
        //分阶段平滑滚动
        smoothScrollToBottomPhased(driver,5000,3);
    }

    /**
     * 点击元素并切换到新窗口
     * @param element 要点击的元素
     * @param timeoutSeconds 超时时间（秒）
     * @return 新窗口的句柄，如果失败返回null
     */
    public static String clickAndSwitchToNewWindow(ChromeDriver driver,WebElement element, long timeoutSeconds){
        try {
            // 记录点击前的窗口
            int windowCountBefore = driver.getWindowHandles().size();
            System.out.println("点击前窗口数: " + windowCountBefore);

            // 点击元素
            element.click();

            // 等待新窗口出现
            WebDriverWait customWait = new WebDriverWait(driver, timeoutSeconds);
            customWait.until(webDriver -> {
                int currentCount = webDriver.getWindowHandles().size();
                System.out.println("等待新窗口，当前窗口数: " + currentCount);
                return currentCount > windowCountBefore;
            });

            // 获取新窗口句柄
            String newWindow = getNewWindowHandle(driver);
            if (newWindow != null) {
                driver.switchTo().window(newWindow);
                System.out.println("成功切换到新窗口: " + newWindow);
                return newWindow;
            }

        } catch (Exception e) {
            System.out.println("切换到新窗口失败: " + e.getMessage());
        }
        return null;
    }

    /**
     * 获取新打开的窗口句柄
     */
    public static String getNewWindowHandle(ChromeDriver driver) {
        Set<String> allWindows = driver.getWindowHandles();
        List<String> windowList = new ArrayList<>(allWindows);

        // 获取最后一个窗口（通常是新打开的）
        if (windowList.size() > 1) {
            return windowList.get(windowList.size() - 1);
        }
        return null;
    }

    /**
     * 方法3: 分阶段平滑滚动
     * @param driver WebDriver实例
     * @param totalDurationMs 总时长（毫秒）
     * @param stages 分几阶段
     */
    public static void smoothScrollToBottomPhased(WebDriver driver, int totalDurationMs, int stages) {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        long pageHeight = (Long) js.executeScript("return document.body.scrollHeight");
        long stageDistance = pageHeight / stages;
        long stageDuration = totalDurationMs / stages;

        System.out.println("分" + stages + "阶段滚动，每阶段" + stageDuration + "ms，总高" + pageHeight);

        for (int i = 1; i <= stages; i++) {
            long targetPosition = stageDistance * i;
            if (i == stages) {
                targetPosition = pageHeight; // 最后阶段确保到底
            }

            String script = String.format(
                    "window.scrollTo({" +
                            "  top: %d," +
                            "  behavior: 'smooth'," +
                            "  duration: %d" +
                            "})",
                    targetPosition, stageDuration
            );

            js.executeScript(script);
            System.out.println("阶段 " + i + "/" + stages + ": 滚动到 " + targetPosition);

            try {
                Thread.sleep(stageDuration + 300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}


