package org.study.mq.rocketmq.dt2.message;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author unisk1123
 * @Description
 * @create 2021-08-10 7:56 下午
 */
public class TransactionSpringConsumer {
    private Logger logger = LoggerFactory.getLogger(TransactionSpringConsumer.class);
    private String consumerGroupName;
    private String nameServerAddress;
    private String topic;
    private DefaultMQPushConsumer consumer;
    private MessageListenerConcurrently messageListener;

    public TransactionSpringConsumer(String consumerGroupName, String nameServerAddress, String topic, MessageListenerConcurrently messageListener) {
        this.consumerGroupName = consumerGroupName;
        this.nameServerAddress = nameServerAddress;
        this.topic = topic;
        this.messageListener = messageListener;
    }

    public void init() throws Exception {
        logger.info("开始启动消息消费服务");
        // 创建一个消息消费者，并设置一个消息消费者组
        consumer = new DefaultMQPushConsumer(consumerGroupName);
        consumer.setNamesrvAddr(nameServerAddress);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe(topic, "*");
        consumer.registerMessageListener(messageListener);
        consumer.start();
    }

    public void destroy() {
        logger.info("开始关闭消息消费者服务");
        consumer.shutdown();
        logger.info("消息消费者服务已关闭");
    }

    public DefaultMQPushConsumer getConsumer() {
        return consumer;
    }
}
