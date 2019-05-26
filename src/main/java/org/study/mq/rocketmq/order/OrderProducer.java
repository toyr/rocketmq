package org.study.mq.rocketmq.order;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author unisk1123
 * @Description
 * @create 2019/5/25
 */
public class OrderProducer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String producerGroupName;
    private String nameServerAddr;
    private DefaultMQProducer producer;

    public OrderProducer(String producerGroupName, String nameServerAddr) {
        this.producerGroupName = producerGroupName;
        this.nameServerAddr = nameServerAddr;
    }

    public void init() throws Exception {
        logger.info("开始启动生产者服务。。。");
        producer = new DefaultMQProducer(producerGroupName);
        producer.setNamesrvAddr(nameServerAddr);
        producer.start();
        logger.info("生产者服务启动成功");
    }

    public void destory() {
        logger.info("开始关闭生产者服务。。。");
        producer.shutdown();
        logger.info("生产者服务已关闭。。。");
    }

    public DefaultMQProducer getProducer() {
        return producer;
    }
}
