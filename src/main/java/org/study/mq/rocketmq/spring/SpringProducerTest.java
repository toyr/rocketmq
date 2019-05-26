package org.study.mq.rocketmq.spring;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author unisk1123
 * @Description
 * @create 2019/5/24
 */
public class SpringProducerTest {

    private ApplicationContext context;

    @Before
    public void setup() {
        context = new ClassPathXmlApplicationContext("classpath:application.xml");
    }

    @Test
    public void sendMessage() throws Exception {
        SpringProducer producer = context.getBean(SpringProducer.class);

        for (int i = 0; i < 20; i++) {
            Message msg = new Message("spring-rocketMQ-topic", null, ("Spring RockerMQ demo " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult send = producer.getProducer().send(msg);
            System.out.printf("%s%n", send);
        }
    }
}
