package com.wyh.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

import java.io.IOException;

public class DeadLetterQueueListener implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            System.out.println("Dead Letter Queue's message: "+new String(message.getBody()));
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
            Thread.sleep(1000);
        } catch (IOException e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),true,true);
        }
    }
}
