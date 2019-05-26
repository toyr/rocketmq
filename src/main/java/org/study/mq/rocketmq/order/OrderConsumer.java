package org.study.mq.rocketmq.order;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author unisk1123
 * @Description
 * @create 2019/5/25
 */
public class OrderConsumer {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private String consumerGroupName;
    private String nameServerAddr;
    private String topicName;
    private DefaultMQPushConsumer consumer;
    private MessageListenerOrderly messageListener;

    public OrderConsumer(String consumerGroupName, String nameServerAddr, String topicName, MessageListenerOrderly messageListener) {
        this.consumerGroupName = consumerGroupName;
        this.nameServerAddr = nameServerAddr;
        this.topicName = topicName;
        this.messageListener = messageListener;
    }

    public void init() throws Exception {
        logger.info("开始启动消费者服务。。。");
        consumer = new DefaultMQPushConsumer(consumerGroupName);
        consumer.setNamesrvAddr(nameServerAddr);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe(topicName, "*");
        consumer.setMessageListener(messageListener);
        consumer.start();
        logger.info("消费者服务启动成功。。。");
    }

    public void destory() {
        logger.info("开始关闭消费者服务");
        consumer.shutdown();
        logger.info("消费者服务已关闭");
    }

    public DefaultMQPushConsumer getConsumer() {
        return consumer;
    }
}
