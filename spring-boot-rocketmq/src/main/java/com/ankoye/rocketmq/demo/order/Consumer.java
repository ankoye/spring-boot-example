package com.ankoye.rocketmq.demo.order;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 顺序消息 - 消费者
 * 一个队列采用一个线程消费 - MessageListenerOrderly
 */
public class Consumer {
    public static void main(String[] args) throws Exception {
        // 1.创建消费者Consumer，制定消费者组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group1");
        // 2.指定Nameserver地址
        consumer.setNamesrvAddr("rocketmq-server1:9876;rocketmq-server2:9876");
        // 3.订阅主题Topic和Tag
        consumer.subscribe("OrderTopic", "*");

        // 4.注册消息监听器  MessageListenerOrderly 消息侦听器有序 (一个队列的消息会使用一个线程消费)
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                for (MessageExt msg : msgs) {
                    // 可以看到每个queue有唯一的consume线程来消费, 订单对每个queue(分区)有序
                    System.out.println("线程名称-" + Thread.currentThread().getName() + "：" + new String(msg.getBody()));
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });

        // 5.启动消费者
        consumer.start();

        System.out.println("消费者启动");
    }
}
