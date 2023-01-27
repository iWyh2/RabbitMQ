package com.wyh.simpleMode;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


public class Producer {
    public static void main(String[] args) throws Exception {
        //简单模式 - 无需设置Exchange交换机
        //1. 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //2. 设置参数
        connectionFactory.setHost("192.168.88.92");//ip       默认值：localhost
        connectionFactory.setPort(5672);           //端口     默认值：5672
        connectionFactory.setVirtualHost("/wyh");  //虚拟机  默认值：/
        connectionFactory.setUsername("wyh");      //用户名 默认值：guest
        connectionFactory.setPassword("wyh");      //密码  默认值：guest
        //3. 创建Connection
        Connection connection = connectionFactory.newConnection();
        //4. 创建Channel
        Channel channel = connection.createChannel();
        //5. 创建Queue
        /*
         * queueDeclare方法参数：
         * 1. queue：队列名称 - String
         * 2. durable 是否持久化，MQ重启之后还会存在 - boolean
         * 3. exclusive： - boolean
         *              ①是否独占，只能有一个消费者监听这队列
         *              ②当Connection关闭时，是否删除队列
         * 4. autoDelete：是否自动删除，当没有Consumer时，会自动删除掉 - boolean
         * 5. arguments：参数信息，例如如何删除队列信息 - Map
         */
        //如果没有名为HelloWorld的队列，那么会自动创建，有则直接使用
        channel.queueDeclare("HelloWorld",true,false,false,null);
        //6. 发送消息到Queue
        /*
         * basicPublish方法参数：
         * 1. exchange：交换机名称，简单模式下使用默认交换机("")
         * 2. routingKey：路由名称，简单模式下需要和队列名称一致才可以找到对应队列
         * 3. props：配置信息
         * 4. body：要发送的消息数据 - byte[]
         */
        channel.basicPublish("","HelloWorld",null,"WangYihuan Will Change The World!".getBytes());
        //7. 释放资源
        channel.close();
        connection.close();
    }
}
