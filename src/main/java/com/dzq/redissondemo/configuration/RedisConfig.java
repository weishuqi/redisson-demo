package com.dzq.redissondemo.configuration;

import com.dzq.redissondemo.message.MessageReceiver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

    /**
     * redis消息监听容器
     * 可以添加多个不同主题的监听器只需要把消息监听器和相应的消息订阅处理器绑定，该消息监听器
     *      通过反射技术调用消息订阅处理器的相关方法进行一些业务处理
     * @Author dzq
     * @Date  2019/7/10 16:23
     **/
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory factory,
                                                   MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);

        // 订阅test主题
        container.addMessageListener(listenerAdapter, new PatternTopic("test"));
        // 可以添加多个监听

        return container;
    }

    /**
     * 消息监听适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法
     * @Author dzq
     * @Date  2019/7/10 16:31
     **/
    @Bean
    public MessageListenerAdapter messageListenerAdapter(MessageReceiver messageReceiver) {
        // 传入一个自定义的消息接收类， 反射调用该类接收消息的方法
        return new MessageListenerAdapter(messageReceiver, "receiveMessage");
    }


    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        return new StringRedisTemplate(factory);
    }
}
