package org.study.mq.rocketmq.batch;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author unisk1123
 * @Description
 * @create 2021-08-09 11:25 下午
 */
public class BatchMessageProducer {
    public static void main(String[] args) throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("niwei_producer_group");
        producer.start();

        List<Message> messages = new ArrayList<>();
        messages.add(new Message("topic", "TagA", "100001", "Hello world a".getBytes()));
        messages.add(new Message("topic", "TagA", "100001", "Hello world b".getBytes()));
        messages.add(new Message("topic", "TagA", "100001", "Hello world c".getBytes()));

        try {
            ListSplitter splitter = new ListSplitter(messages);
            while (splitter.hasNext()) {
                try {
                    List<Message> listItem = splitter.next();
                    producer.send(listItem);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        producer.shutdown();
    }
}
