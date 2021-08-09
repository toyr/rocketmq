package org.study.mq.rocketmq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;

/**
 * @author unisk1123
 * @Description
 * @create 2021-08-09 8:27 下午
 */
public class ProducerTest {

    public static void main(String[] args) throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("niwei_producer_GROUP");
        // 消息发送失败重试次数
        producer.setRetryTimesWhenSendFailed(3);
        // 消息没有存储成功，是否发送到另外一个Broker中
        producer.setRetryAnotherBrokerWhenNotStoreOK(true);
        // 指定NameServer地址
        producer.setNamesrvAddr("localhost:9876");
        // 初始化 TransactionSpringProducer, 整个应用生命周期内只需要初始化一次
        producer.start();

    }
}
