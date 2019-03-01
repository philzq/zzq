package zzq.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Author: wangli
 * Date: 2018/11/26
 * Description:
 */
public class CacheUtils {

    private static Logger logger = LoggerFactory.getLogger(CacheUtils.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 将参数中的字符串值设置为键的值，不设置过期时间
     * @param key
     * @param value 必须要实现 Serializable 接口
     */
    public void set(Object key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * 将参数中的字符串值设置为键的值，设置过期时间
     * @param key
     * @param value 必须要实现 Serializable 接口
     * @param timeout
     */
    public void set(Object key, Object value, Long timeout) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * 获取与指定键相关的值
     * @param key
     * @return
     */
    public Object get(String key) {
        Object value = null;
        try {
            if(hasKey(key)){//防止redis缓存穿透
                value = redisTemplate.opsForValue().get(key);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 设置某个键的过期时间
     * @param key 键值
     * @param ttl 过期秒数
     */
    public boolean expire(String key, Long ttl) {
        boolean flag = false;
        try {
            flag = redisTemplate.expire(key, ttl, TimeUnit.SECONDS);
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 获取key过期时间
     * @param key
     */
    public long getExpire(Object key){
        long time = -1;
        try {
            time = redisTemplate.opsForValue().getOperations().getExpire(key);
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 判断某个键是否存在
     * @param key 键值
     */
    public boolean hasKey(String key) {
        boolean flag = false;
        try {
            flag = redisTemplate.hasKey(key);
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 向集合添加元素
     * @param key
     * @param value
     * @return 返回值为设置成功的value数
     */
    public Long sAdd(String key, String... value) {
        Long flag = 0L;
        try {
            flag = redisTemplate.opsForSet().add(key, value);
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 获取集合中的某个元素
     * @param key
     * @return 返回值为redis中键值为key的value的Set集合
     */
    public Set<String> sGetMembers(String key) {
        Set<String> values = null;
        try {
            values = redisTemplate.opsForSet().members(key);
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return values;
    }

    /**
     * 将给定分数的指定成员添加到键中存储的排序集合中
     * @param key
     * @param value
     * @param score
     * @return
     */
    public Boolean zAdd(String key, String value, double score) {
        Boolean flag = false;
        try {
            flag = redisTemplate.opsForZSet().add(key, value, score);
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 返回指定排序集中给定成员的分数
     * @param key
     * @param value
     * @return
     */
    public Double zScore(String key, String value) {
        Double score = 0.0;
        try {
            score = redisTemplate.opsForZSet().score(key, value);
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return score;
    }

    /**
     * 删除指定的键
     * @param key
     * @return
     */
    public Boolean delete(String key) {
        Boolean flag = false;
        try {
            flag = redisTemplate.delete(key);
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除多个键
     * @param keys
     * @return
     */
    public Long delete(Collection<String> keys) {
        Long flag = 0L;
        try {
            flag = redisTemplate.delete(keys);
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 向指定通道发送消息
     * @param channel
     * @param message
     */
    public void sendChannelMess(String channel, String message){
        try {
            stringRedisTemplate.convertAndSend(channel,message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
