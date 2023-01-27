package com.wyh.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.util.Random;


@Component
public class DLXlistener implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            System.out.println("Normal Queue's message: "+new String(message.getBody()));
            Random random = new Random();
            int i = random.nextInt(2);
            if (i == 1) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
                System.out.println("The message was received");
            } else {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),true,false);
                System.out.println("The message was rejected");
            }
            Thread.sleep(1000);
        } catch (Exception e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),true,false);
        }

    }
}
