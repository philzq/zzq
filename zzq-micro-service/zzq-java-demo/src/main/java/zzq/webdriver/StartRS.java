package zzq.webdriver;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class StartRS {

    private String requestId;
    private String msg;
    private int code;
    private DataBean data;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        private String action;
        private String backgroundPluginId;
        private int browserID;
        private String browserPath;
        private String containerCode;
        private int containerId;
        private String debuggingPort;
        private String downloadPath;
        private int duplicate;
        private String err;
        private String ip;
        private boolean isDynamicIp;
        private String launcherPage;
        private String proxyType;
        private String requestId;
        private int runMode;
        private String statusCode;
        private String webdriver;
    }
}
