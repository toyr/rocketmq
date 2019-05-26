package org.study.mq.rocketmq.spring;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author unisk1123
 * @Description
 * @create 2019/5/24
 */
public class SpringProducer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String producerGroupName;
    private String nameServerAddr;
    private DefaultMQProducer producer;

    public SpringProducer(String producerGroupName, String nameServerAddr) {
        this.producerGroupName = producerGroupName;
        this.nameServerAddr = nameServerAddr;
    }

    public void init() throws Exception {
        logger.info("开始启动消息生产者服务---");
        // 创建一个消息生产者，并设置一个消息生产者组
        producer = new DefaultMQProducer(producerGroupName);
        // 指定 NameServerAddr
        producer.setNamesrvAddr(nameServerAddr);
        // 初始化 springProducer，在整个应用生命周期内只需要初始化一次
        producer.start();
        logger.info("消息生产者服务启动成功。");
    }

    public void destrory() {
        logger.info("开始关闭消息生产者服务");
        producer.shutdown();
        logger.info("消息生产者服务已关闭");
    }

    public DefaultMQProducer getProducer() {
        return producer;
    }
}
