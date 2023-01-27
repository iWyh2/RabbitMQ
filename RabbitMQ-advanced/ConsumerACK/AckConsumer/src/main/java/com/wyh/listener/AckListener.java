package com.wyh.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;


@Component
public class AckListener implements ChannelAwareMessageListener {
    /**
     * Consumer Ack
     * 1.设置手动确认（spring-rabbitmq-consumer.xml中的rabbit:listener-container设置acknowledge="manual"
     * 2.让监听器实现ChannelAwareMessageListener接口
     * 3.如果消息成功处理，调用basicAck确认签收
     * 4.如果消息处理失败，调用basicNack拒绝签收，并让其重新发送
     */
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            System.out.println("This is a manual confirmation message");
            System.out.println("The business is being processed...");
            //int i = 1/0;
            //如果处理成功，手动签收，移除缓存
            /*
             * basicAck方法参数
             * 1.deliveryTag：当前收到的消息的递送过来的Tag值 - long
             * 2.multiple：是否签收多条消息 - boolean -true为签收全部
             */
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
        } catch (Exception e) {
            /*
             * basicNack方法参数
             * 1.deliveryTag：当前收到的消息的递送过来的Tag值 - long
             * 2.multiple：是否签收多条消息 - boolean -true为签收全部
             * 3.requeue：是否重回队列 - boolean -true为消息重新回到queue，且broker会重新发送该消息到消费端
             */
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),true,true);
        }
    }

}
