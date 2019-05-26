package org.study.mq.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.Date;
import java.util.List;

/**
 * @author unisk1123
 * @Description 消息消费者
 * @create 2019/5/23
 */
public class Consumer {

    public static void main(String[] args) throws Exception {
        // 创建一个消息消费者，并设置一个消息消费者组
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("t_consumer_group");

        // 指定NameServer地址
        consumer.setNamesrvAddr("localhost:9876");

        // 设置 Consumer 第一次启动时是从队列头部还是队列尾部开始消费的
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        // 订阅指定Topic 下的所有消息
        consumer.subscribe("topic_example_java", "*");

        // 注册消息监听器
        consumer.registerMessageListener((List<MessageExt> list, ConsumeConcurrentlyContext context) -> {
            // 默认 list 里只有一条消息， 可以通过设置参数来批量接受消息
            if (list != null) {
                for (MessageExt ext : list) {
                    try {
                        System.out.println(new Date() + new java.lang.String(ext.getBody(), "UTF-8"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        // 消费者对象在使用之前必须要调用 start 方法初始化
        consumer.start();
        System.out.println("消费消费者已启动");
    }
}
