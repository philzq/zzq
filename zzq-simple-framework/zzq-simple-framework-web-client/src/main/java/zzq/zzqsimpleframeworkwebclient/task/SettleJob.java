package zzq.zzqsimpleframeworkwebclient.task;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzq.zzqsimpleframeworkwebclient.test.TestHttpClientService;

import java.util.Date;

/**
 * @author Travis
 * @date 2022/11/24 10:42
 */
@Component
public class SettleJob {
    private static Logger logger = LoggerFactory.getLogger(SettleJob.class);

    @Autowired
    private TestHttpClientService testHttpClientService;

    @XxlJob(value = "zzqTest1JobHandler")
    public void verboseJobHandler() {
        //测试全链路跟踪
        //job生成全局requestId ---> web --->kafka
        testHttpClientService.getOK();
        XxlJobHelper.log("Just execute a zzqTest1JobHandler--{}", new Date());
    }

    @XxlJob(value = "zzqTest2JobHandler")
    public void timestampJobHandler() {
        XxlJobHelper.log("print current time--{}", new Date());
        throw new RuntimeException("test job");
    }
}
