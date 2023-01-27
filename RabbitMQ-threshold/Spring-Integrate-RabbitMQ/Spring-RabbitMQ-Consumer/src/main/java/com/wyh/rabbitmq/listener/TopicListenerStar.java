package com.wyh.rabbitmq.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class TopicListenerStar implements MessageListener {
    /**
     * 接收到消息之后的回调方法
     */
    @Override
    public void onMessage(Message message) {
        System.out.println("消息: " + new String(message.getBody()));
    }
}
