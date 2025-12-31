package com.crawler.douyin.config;

import lombok.Data;

/**
 * Appium配置类
 * 用于配置Appium连接参数
 * 
 * @author crawler
 */
@Data
public class AppiumConfig {
    /**
     * Appium服务器地址
     */
    private String serverUrl = "http://127.0.0.1:4723/wd/hub";
    
    /**
     * 设备UDID（可通过adb devices查看）
     */
    private String deviceUdid = "EMH0221A22001952";
    
    /**
     * 设备平台版本（Android版本，如：11.0）
     */
    private String platformVersion = "11.0";
    
    /**
     * 设备名称
     */
    private String deviceName = "Android Device";
    
    /**
     * APP包名（京东APP包名）
     */
    private String appPackage = "com.jingdong.app.mall";
    
    /**
     * APP启动Activity
     */
    private String appActivity = ".activity.MainFrameActivity";
    
    /**
     * 命令超时时间（秒）
     */
    private int newCommandTimeout = 300;
    
    /**
     * 是否不清除APP数据
     */
    private boolean noReset = true;
    
    /**
     * 是否跳过安装
     */
    private boolean skipServerInstallation = false;
    
    /**
     * 自动化引擎（uiautomator2用于Android）
     */
    private String automationName = "UiAutomator2";
    
    /**
     * ADB命令执行超时时间（毫秒）
     */
    private long adbExecTimeout = 120000;
}

