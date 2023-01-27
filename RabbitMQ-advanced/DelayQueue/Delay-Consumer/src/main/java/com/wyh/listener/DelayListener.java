package com.wyh.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * 监听延迟队列
 */
@Component
public class DelayListener implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        System.out.println("30 minutes ago...");//模拟用户三十分钟未完成支付
        System.out.println("The message form OrderSystem: " + new String(message.getBody()));
        System.out.println("Then we are checking the order's state...");
        System.out.println("Oh,no. we find that this order was not paid");
        System.out.println("So,we will cancel the order, and then we will rollback transaction");
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
    }
}
