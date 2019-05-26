package org.study.mq.rocketmq.order;

import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.List;

/**
 * @author unisk1123
 * @Description
 * @create 2019/5/25
 */
public class OrderMessageQueueSelector implements MessageQueueSelector {
    @Override
    public MessageQueue select(List<MessageQueue> list, Message message, Object o) {

        Integer id = (Integer) o;
        return list.get(id % list.size());
    }
}
