package com.dzq.redissondemo.controller;

import org.redisson.api.RLock;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 测试
 *
 * @Author dzq
 * @Date 2019/7/5 11:56
 * @Param
 * @return
 **/
@RestController
public class TestController {

    @Autowired
    private RedissonClient redissonClient;

    @GetMapping("/test")
    public Object test() {

        // 获取可重入锁
        RLock rLock = redissonClient.getLock("test");

        try {
            // 上锁 常见上锁方法
            /*boolean res = rLock.tryLock();*/
            /*rLock.lock();*/

            // 加锁以后10秒钟自动解锁， 无需手动调用unlock()解锁
            /*rLock.lock(10, TimeUnit.SECONDS);*/

            // 尝试加锁，如果没有获取到，最多等待100秒， 获取到锁后10秒自动释放
            boolean res = rLock.tryLock(100, 10, TimeUnit.SECONDS);

            System.out.println(Thread.currentThread().getName() + "isLock=" + res);

            if (res) {
                // 获取到锁
                System.out.println(Thread.currentThread().getName() + "获取到锁");
                Thread.sleep(10000);
                return "success";
            } else {
                System.out.println(Thread.currentThread().getName() + "没有拿到锁");
                return "faild";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放锁
            System.out.println(Thread.currentThread().getName() + "释放锁");
            rLock.unlock();
        }
        return null;
    }

    @GetMapping("/test2")
    public Object test2() {

        // 获取公平锁
        // 它保证了当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。
        // 所有请求线程会在一个队列中排队，当某个线程出现宕机时，Redisson会等待5秒后继续下一个线程，也就是说如果前面有5个线程都处于等待状态，
        // 那么后面的线程会等待至少25秒。
        RLock rLock = redissonClient.getFairLock("test2");

        try {

            // 最常见的使用方法
            rLock.lock();

            // 加锁以后10秒钟自动解锁， 无需手动调用unlock()解锁
            /*rLock.lock(10, TimeUnit.SECONDS);*/

            // 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
            // boolean res = rLock.tryLock(100, 10, TimeUnit.SECONDS);

            // 获取到锁
            System.out.println(Thread.currentThread().getName() + "获取到锁");
            Thread.sleep(10000);
            return "success";

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放锁
            System.out.println(Thread.currentThread().getName() + "释放锁");
            rLock.unlock();
        }
        return null;
    }

    @GetMapping
    public Object test3(@RequestParam(name = "message") String message) {
        return publish(message);
    }

    /**
     * 发布消息
     * @Author dzq
     * @Date  2019/7/5 17:21
     * @Param
     * @return
     **/
    public long publish(String message) {
        RTopic rTopic = redissonClient.getTopic("pubTest");
        return rTopic.publish(message);
    }

    @Bean
    public void subscribe() {

        RTopic rTopic = redissonClient.getTopic("pubTest");

        // 添加监听器 lambda表达式
        rTopic.addListener(String.class,
                (charSequence, s) -> {
                    System.out.println("接收到消息，主题：" + charSequence + "内容：" + s);
                    System.out.println(Thread.currentThread().getName());
                });
    }
}
