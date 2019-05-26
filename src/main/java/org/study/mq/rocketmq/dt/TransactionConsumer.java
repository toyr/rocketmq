package org.study.mq.rocketmq.dt;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.util.locale.provider.TimeZoneNameUtility;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author unisk1123
 * @Description
 * @create 2019/5/26
 */
public class TransactionConsumer {

    private static Logger logger = LoggerFactory.getLogger(TransactionConsumer.class);

    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("transaction_comsumer_group");
        consumer.setNamesrvAddr("localhost:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("TopicTransaction", "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            private Random random = new Random();
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                for (MessageExt msg : list) {
                    logger.info("消息消费者接收到消息：" + msg);
                    logger.info("接收到的消息标签是：" + new String(msg.getTags()));
                    logger.info("接收到的消息内容是：" + new String(msg.getBody()));

                    try {
                        // 模拟业务处理
                        TimeUnit.SECONDS.sleep(random.nextInt(5));
                    } catch (Exception e) {
                        e.printStackTrace();
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }

                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
    }
}
