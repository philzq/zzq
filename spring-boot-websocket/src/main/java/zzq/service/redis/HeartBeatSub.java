package zzq.service.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import zzq.server.SubscribeWebSocket;


/**
 * 〈功能简述〉<br>
 * 〈心跳检测〉
 *
 * @create 2018/12/27
 */
@Component
public class HeartBeatSub implements RedisBaseSub {

	private static final Logger logger = LoggerFactory.getLogger(HeartBeatSub.class);

	@Override
	public void receiveMessage(String message) {
		try {
			for (SubscribeWebSocket subscribeWebSocket : SubscribeWebSocket.clients) {
				long nowTime = System.currentTimeMillis();
				long heartBeatTime = subscribeWebSocket.heartBeatTime;
				long subTime = (nowTime - heartBeatTime) / 1000 / 60;
				long beginTime = subscribeWebSocket.beginTime;
				long subAllTime = (nowTime - beginTime) / 1000 / 60 / 60;
				if (subTime > 5 || subAllTime > 2) {// 五分钟没进行心跳检测或者连接时长超过两小时的连接将关闭
					subscribeWebSocket.onClose();
				}
			}
		} catch (Exception e) {
			logger.error("Heartbeat jump error , {}" , e);
		}
	}
}
