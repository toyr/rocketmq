package org.study.mq.rocketmq.dt2.message;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author unisk1123
 * @Description
 * @create 2021-08-10 7:30 下午
 */
public class TransactionSpringProducer {

    private Logger logger = LoggerFactory.getLogger(TransactionSpringProducer.class);
    private String producerGroupName;
    private String nameServerAddress;
    private int corePoolSize = 1;
    private int maximumPoolSize = 5;
    private long keepAliveTime = 100;
    private TransactionMQProducer producer;
    private TransactionListener transactionListener;

    public TransactionSpringProducer(String producerGroupName, String nameServerAddress,
                                     int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                     TransactionListener transactionListener) {
        this.producerGroupName = producerGroupName;
        this.nameServerAddress = nameServerAddress;
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.transactionListener = transactionListener;
    }

    public void init() throws Exception {
        logger.info("开始启动消息生产者服务");
        // 创建一个消息生产者，并设置一个消息生产者组
        producer = new TransactionMQProducer(producerGroupName);
        producer.setNamesrvAddr(nameServerAddress);

        ExecutorService executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2000), (Runnable r) -> {
            Thread thread = new Thread(r);
            thread.setName("client-transaction-msg-check-thread");
            return thread;
        });
        // 设置本地事务执行的线程池
        producer.setExecutorService(executorService);
        producer.setTransactionListener(transactionListener);
        producer.start();

        logger.info("消费生产者服务启动成功");
    }

    public void destory() {
        logger.info("开始关闭消息生产者服务");
        producer.shutdown();
        logger.info("消息生产者服务已关闭");
    }

    public DefaultMQProducer getProducer() {
        return producer;
    }
}
