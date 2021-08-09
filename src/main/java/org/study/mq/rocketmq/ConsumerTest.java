package org.study.mq.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author unisk1123
 * @Description
 * @create 2021-08-09 8:35 下午
 */
public class ConsumerTest {

    public static void main(String[] args) throws MQClientException {
        // 创建一个消息消费者，并设置一个消息消费者组
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("niwei_consumer_group");
        // 指定NameServer地址
        consumer.setNamesrvAddr("localhost:9876");
        // 设置消费者第一次启动后是从对列头部还是队列尾部开始消费的
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 订阅指定Topic下的所有信息
        consumer.registerMessageListener((List<MessageExt> list, ConsumeConcurrentlyContext context) -> {
            for (MessageExt messageExt : list) {
                String messageBody = new String(messageExt.getBody());
                if (messageExt.getReconsumeTimes() == 3) {
                    // 如果重试了3次还是失败，则不再重试
                    // 把重试次数达到3次的消息记录下来
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                } else {
                    try {
                        // 执行业务
                    } catch (Exception e) {
                        // 业务方法在执行时如果返回的是可以在消费的异常，则触发重试
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        // 在使用消费者对象之前必须调用start初始化
        consumer.start();
        System.out.println("消息消费者已启动");
    }


    public void test() throws UnsupportedEncodingException, MQBrokerException, RemotingException, InterruptedException, MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("niwei_consumer_group");
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("niwei_consumer_group");
        // 设置广播模式
        consumer.setMessageModel(MessageModel.BROADCASTING);
        // 创建一个消息队形，指定其主题、标签和消息内容
        Message msg = new Message("topic", "TagA", ("Hello Java").getBytes(RemotingHelper.DEFAULT_CHARSET));
        // 延时消息，延迟级别为3，表示延迟10s后发送
        // 1~N: 1s、5s、10s、30s、1~10m、10m、20m、30m、1h、2h
        msg.setDelayTimeLevel(3);
        // 订单ID
        String orderId = "1234124";
        msg.setKeys(orderId);
        // 发送消息并返回结果
        SendResult sendResult = producer.send(msg);

        consumer.registerMessageListener((List<MessageExt> list, ConsumeConcurrentlyContext context) -> {
            // 默认list中只有一条消息，可以通过设置参数来批量接受消息
            for (MessageExt message : list) {

                try {
                    // 根据业务唯一标识的 key 做幂等处理
                    String orderIdd = message.getKeys();
                    System.out.println(new Date() + new String(message.getBody(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        List<Message> messages = new ArrayList<>();
        messages.add(new Message("topic", "TagA", "100001", "Hello world a".getBytes()));
        messages.add(new Message("topic", "TagA", "100001", "Hello world b".getBytes()));
        messages.add(new Message("topic", "TagA", "100001", "Hello world c".getBytes()));

        try {
            producer.send(messages);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
