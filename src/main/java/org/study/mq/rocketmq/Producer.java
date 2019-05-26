package org.study.mq.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @author unisk1123
 * @Description 消息生产者
 * @create 2019/5/23
 */
public class Producer {

    public static void main(String[] args) throws Exception {

        // 创建一个消息生产者，并设置一个消息生产者组
        DefaultMQProducer producer = new DefaultMQProducer("t_producer_group");

        // 指定 NameServer地址
        producer.setNamesrvAddr("localhost:9876");

        // 初始化 Producer，在整个应用声明周期中只需要初始化一次
        producer.start();

        for (int i = 0; i < 100; i++) {
            // 创建一个消息对象，指定其主体、标签和消息内容
            Message msg = new Message("topic_example_java",
                    "TagA",
                    ("Hello Java demo RockerMQ" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));

            // 发送消息并返回结果
            SendResult sendResult = producer.send(msg);

            System.out.printf("%s%n", sendResult);

        }

        // 一旦生产者实例不再被使用，则将其关闭，包括清理资源、关闭网络连接等
        producer.shutdown();
    }
}
