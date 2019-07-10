package com.dzq.redissondemo.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 测试消息发送
 * @Author dzq
 * @Date  2019/7/10 16:44
 **/
@Component
@EnableScheduling
public class MessageSender {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 间隔两秒，发送一次消息
     * @Author dzq
     * @Date  2019/7/10 16:33
     **/
    @Scheduled(fixedRate = 2000)
    public void sendMessage() {
        redisTemplate.convertAndSend("test", String.valueOf(Math.random()));
    }

}
