package com.ankoye.rocketmq.demo.batch;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 批量发送消息能显著提高传递小消息的性能。
 * 限制是这些批量消息应该有相同的topic，相同的waitStoreMsgOK，而且不能是延时消息。
 * 此外，这一批消息的总大小不应超过4MB。超过需要进行消息分隔
 */
public class Producer {
    public static void main(String[] args) throws Exception {
        // 1.创建消息生产者producer，并制定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        // 2.指定Nameserver地址
        producer.setNamesrvAddr("rocketmq-server1:9876;rocketmq-server2:9876");
        // 3.启动producer
        producer.start();
        // 4.创建消息对象，指定主题Topic、Tag和消息体
        /**
         * 参数一：消息主题Topic
         * 参数二：消息Tag
         * 参数三：消息内容
         */
        List<Message> msgs = new ArrayList<>();

        Message msg1 = new Message("BatchTopic", "Tag1", ("Hello World" + 1).getBytes());
        Message msg2 = new Message("BatchTopic", "Tag1", ("Hello World" + 2).getBytes());
        Message msg3 = new Message("BatchTopic", "Tag1", ("Hello World" + 3).getBytes());
        msgs.add(msg1);
        msgs.add(msg2);
        msgs.add(msg3);

        //5.发送消息
        SendResult result = producer.send(msgs);

        System.out.println("发送结果:" + result);

        //线程睡1秒
        TimeUnit.SECONDS.sleep(1);

        //6.关闭生产者producer
        producer.shutdown();
    }
}
