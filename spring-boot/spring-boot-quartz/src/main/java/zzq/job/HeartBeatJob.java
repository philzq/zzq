package zzq.job;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Date;

/**
 * 〈功能简述〉<br>
 * 〈心跳检测定时任务〉
 *
 * @author zhouzhiqiang
 * @create 2018-12-28
 */
public class HeartBeatJob implements BaseJob {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void execute(JobExecutionContext context) {
        stringRedisTemplate.convertAndSend("heartBeatSub", "心跳蹦蹦蹦:" + new Date().getTime());
    }
}
