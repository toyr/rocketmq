package org.study.mq.rocketmq.order;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.function.Predicate;

/**
 * @author unisk1123
 * @Description
 * @create 2019/5/25
 */
public class OrderProducerTest {

    private ApplicationContext context;

    @Before
    public void setup() {
        context = new ClassPathXmlApplicationContext("classpath:spring-producer.xml");
    }

    @Test
    public void sendMessage() throws Exception {
        OrderProducer producer = context.getBean(OrderProducer.class);

        OrderMessageQueueSelector messageQueueSelector = context.getBean(OrderMessageQueueSelector.class);

        String topicName = "topic_example_order";

        String[] statusName = {"已创建", "已付款", "已配送", "已取消", "已完成"};

        // 模拟订单
        for (int orderId = 0; orderId < 10; orderId++) {
            for (int j = 0; j < statusName.length; j++) {
                String messageContent = new OrderMessage().setId(orderId).setStatus(statusName[j])
                        .setSendOrder(j).setContent("hello orderly rocketMQ message!").toString();
                Message sendMessage = new Message(topicName, statusName[j], orderId + "#" + statusName[j],
                        messageContent.getBytes(RemotingHelper.DEFAULT_CHARSET));

                // 发送消息并返回消息
                SendResult sendResult = producer.getProducer().send(sendMessage, messageQueueSelector, orderId);
                System.out.printf("%s %n", sendResult);

            }
        }
    }
}
