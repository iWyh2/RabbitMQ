package com.wyh.routing;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


public class Producer {
    public static void main(String[] args) throws Exception {
        //路由模式 - 需要设置Exchange交换机
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
        //5. 创建Exchange - 交换机
        /*
         * exchangeDeclare方法参数：
         * 1. exchange：交换机名称 - String
         * 2. type：交换机类型 - BuiltinExchangeType枚举
         *                  ① DIRECT：定向
         *                  ② FANOUT：广播
         *                  ③ TOPIC： 通配符
         *                  ④ HEADERS：参数匹配
         * 3. durable 是否持久化 - boolean
         * 4. autoDelete：是否自动删除 - boolean
         * 5. internal：是否内部使用 - boolean - 一般为false
         * 6. arguments：参数 - Map
         */
        channel.exchangeDeclare("wyh-direct", BuiltinExchangeType.DIRECT,true,false,false,null);
        //6. 创建多个Queue
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
        //如果没有名为wyh-direct-queue-error / wyh-direct-queue-info的队列，那么会自动创建，有则直接使用
        channel.queueDeclare("wyh-direct-queue-error",true,false,false,null);
        channel.queueDeclare("wyh-direct-queue-info",true,false,false,null);
        //7. 绑定队列和交换机 - Binding
        /*
         * queueBind方法参数：
         * 1. queue：队列名称
         * 2. exchange：交换机名称
         * 3. routingKey：路由键，绑定规则
         *              - 当交换机为Fanout广播类型时，不需要指定routingKey，设置为("")
         */
        channel.queueBind("wyh-direct-queue-error","wyh-direct","error");
        channel.queueBind("wyh-direct-queue-error","wyh-direct","all");//绑定一样的routingKey，都能获取相同的消息

        channel.queueBind("wyh-direct-queue-info","wyh-direct","info");
        channel.queueBind("wyh-direct-queue-info","wyh-direct","warning");
        channel.queueBind("wyh-direct-queue-info","wyh-direct","exception");
        channel.queueBind("wyh-direct-queue-info","wyh-direct","all");//绑定一样的routingKey，都能获取相同的消息
        //8. 发送消息到Exchange
        /*
         * basicPublish方法参数：
         * 1. exchange：交换机名称，简单模式下使用默认交换机("")
         * 2. routingKey：路由名称，简单模式下需要和队列名称一致才可以找到对应队列
         * 3. props：配置信息
         * 4. body：要发送的消息数据 - byte[]
         */
        channel.basicPublish("wyh-direct","info",null,"WangYihuan Will Change The World!-info".getBytes());
        channel.basicPublish("wyh-direct","warning",null,"WangYihuan Will Change The World!-warning".getBytes());
        channel.basicPublish("wyh-direct","exception",null,"WangYihuan Will Change The World!-exception".getBytes());
        channel.basicPublish("wyh-direct","error",null,"WangYihuan Will Failed?-NO".getBytes());
        channel.basicPublish("wyh-direct","all",null,"WangYihuan is very cool!".getBytes());
        //9. 释放资源
        channel.close();
        connection.close();
    }
}
