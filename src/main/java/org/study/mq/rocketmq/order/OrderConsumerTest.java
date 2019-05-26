package org.study.mq.rocketmq.order;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author unisk1123
 * @Description
 * @create 2019/5/25
 */
public class OrderConsumerTest {

    private ApplicationContext context;

    @Before
    public void setup() {
        context = new ClassPathXmlApplicationContext("classpath:spring-consumer.xml");
    }

    @Test
    public void consumer() throws  Exception{
        OrderConsumer comsumer = context.getBean(OrderConsumer.class);
        Thread.sleep(200 * 1000);
        comsumer.destory();
    }
}
