package org.study.mq.rocketmq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author unisk1123
 * @Description
 * @create 2019/5/26
 */
public class BatchMessageProducer {

    public static void main(String[] args) throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("t_ptoducer_group");
        producer.start();
        String topic = "topic_example_batch";
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(topic, "TagA", "100001", "Hello World a".getBytes()));
        messages.add(new Message(topic, "TagB", "100001", "Hello World b".getBytes()));
        messages.add(new Message(topic, "TagC", "100001", "Hello World c".getBytes()));

        try {
            ListSplitter splitter = new ListSplitter(messages);
            while (splitter.hasNext()) {
                try {
                    List<Message> listItem = splitter.next();
                    producer.send(listItem);
                } catch (RemotingException e) {
                    e.printStackTrace();
                } catch (MQBrokerException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        producer.shutdown();
    }

    static class ListSplitter implements Iterator<List<Message>> {

        private final int SIZE_LIMIT = 1000 * 1000;
        private final List<Message> messages;
        private int currIndex;

        public ListSplitter(List<Message> messages) {
            this.messages = messages;
        }

        @Override
        public boolean hasNext() {
            return currIndex < messages.size();
        }

        @Override
        public List<Message> next() {
            int nextIndex = currIndex;
            int totalSize = 0;
            for (; nextIndex < messages.size(); nextIndex++) {
                Message message = messages.get(nextIndex);
                int tmpSize = message.getTopic().length() + message.getBody().length;
                Map<String, String> properties = message.getProperties();
                for (Map.Entry<String, String> entry :
                        properties.entrySet()) {
                    tmpSize += entry.getKey().length() + entry.getValue().length();
                }
                tmpSize = tmpSize + 20;
                if (tmpSize > SIZE_LIMIT) {
                    // 如果大小超过批量消息的1MB限制，则跳出循环
                    // 否则，就继续添加消息对象到集合中一次发送出去
                    if (nextIndex - currIndex == 0) {
                        nextIndex++;
                    }
                    break;
                }
                if (tmpSize + totalSize > SIZE_LIMIT) {
                    break;
                } else {
                    totalSize += tmpSize;
                }
            }

            List<Message> subList = this.messages.subList(currIndex, nextIndex);
            currIndex = nextIndex;
            return subList;
        }


    }
}
