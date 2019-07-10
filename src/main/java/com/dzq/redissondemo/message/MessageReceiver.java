package com.dzq.redissondemo.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 消息处理
 *
 * @Author dzq
 * @Date 2019/7/10 16:26
 **/
@Component
@Slf4j
public class MessageReceiver {

    /**
     * 接收消息
     * @Author dzq
     * @Date  2019/7/10 16:27
     **/
    public void receiveMessage(String msg) {
        log.info("收到一条消息：{}", msg);
    }
}
