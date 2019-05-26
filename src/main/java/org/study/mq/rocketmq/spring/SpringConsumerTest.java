package org.study.mq.rocketmq.spring;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author unisk1123
 * @Description
 * @create 2019/5/24
 */
public class SpringConsumerTest {

    private ApplicationContext context;

    @Before
    public void setup() {
        context = new ClassPathXmlApplicationContext("classpath:application.xml");
    }

    @Test
    public void sendMessage() throws Exception {
        SpringConsumer consumer = context.getBean(SpringConsumer.class);

        Thread.sleep(10000);

        consumer.destory();
    }
}
