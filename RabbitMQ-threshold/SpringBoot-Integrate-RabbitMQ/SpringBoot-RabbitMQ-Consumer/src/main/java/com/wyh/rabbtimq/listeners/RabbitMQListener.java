package com.wyh.rabbtimq.listeners;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitMQListener {
/*
    @RabbitListener(queues = {"bootTopicErrorQueue"})
    public void TopicErrorQueueListener(Message message) {
        System.out.println("TopicErrorQueue's message: "+new String(message.getBody()));
    }

    @RabbitListener(queues = {"bootTopicInfoQueue"})
    public void TopicInfoQueueListener(Message message) {
        System.out.println("TopicInfoQueue's message: "+new String(message.getBody()));
    }

    @RabbitListener(queues = {"Confirm-queue"}/*,ackMode = "MANUAL"* /)
    public void AckListener(Message message, Channel channel) throws IOException {
        try {
            System.out.println("This is a manual confirmation message");
            System.out.println("The business is being processed...");
            System.out.println("Confirm-queue's message: "+new String(message.getBody()));
            //int i = 1/0;
            //如果处理成功，手动签收，移除缓存
            /*
             * basicAck方法参数
             * 1.deliveryTag：当前收到的消息的递送过来的Tag值 - long
             * 2.multiple：是否签收多条消息 - boolean -true为签收全部
             * /
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
        } catch (Exception e) {
            /*
             * basicNack方法参数
             * 1.deliveryTag：当前收到的消息的递送过来的Tag值 - long
             * 2.multiple：是否签收多条消息 - boolean -true为签收全部
             * 3.requeue：是否重回队列 - boolean -true为消息重新回到queue，且broker会重新发送该消息到消费端
             * /
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),true,true);
        }
    }

    /**
     * 监听与死信交换机绑定的普通队列
     * /
    @RabbitListener(queues = {"NormalQueue"})
    public void NormalListener(Message message, Channel channel) throws IOException {
        try {
            Random random = new Random(System.currentTimeMillis());
            int i = random.nextInt(2);
            if (i == 1) {
                System.out.println("One message was received: "+new String(message.getBody()));
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
            } else {
                System.out.println("One message was rejected");
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),true,true);
            }
            Thread.sleep(1000);
        } catch (Exception e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),true,true);
        }
    }
*/

    /**
     * 监听延迟队列 - 要监听死信队列才会达到延迟队列的效果
     */
    @RabbitListener(queues = {"ServiceDeadLetterQueue"})
    public void ServiceListener(Message message,Channel channel) throws IOException {
        System.out.println("30 minutes ago...");
        System.out.println("The message form OrderSystem: " + new String(message.getBody()));
        System.out.println("Then we are checking the order's state...");
        System.out.println("Oh,no. we find that this order was not paid");
        System.out.println("So,we will cancel the order, and then we will rollback transaction");
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
    }
}
