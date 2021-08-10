package org.study.mq.rocketmq.dt;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.experimental.theories.FromDataPoints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author unisk1123
 * @Description
 * @create 2019/5/26
 */
public class TransactionProducer {

    private static final Logger logger = LoggerFactory.getLogger(TransactionProducer.class);

    public static void main(String[] args) throws MQClientException, InterruptedException {
        TransactionMQProducer producer = new TransactionMQProducer("transaction_producer_group");

        producer.setNamesrvAddr("localhost:9876");

        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), (Runnable r) -> {
            Thread thread = new Thread(r);
            thread.setName("client-transaction-msg-check-thread");
            return thread;
        });

        // 设置本地事务执行的线程池
        producer.setExecutorService(executorService);
        producer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                // 本地事务处理逻辑
                logger.info("本地事务执行。。。");
                logger.info("消息标签是 " + new String(message.getTags()));
                logger.info("消息内容是 " + new String(message.getBody()));
                String tag = message.getTags();
                if (tag.equals("Transaction1")) {
                    // 消息的标签，如果是Transaction1,则返回事务失败标记
                    logger.error("模拟本地事务执行失败");

                    // 表示本地事务执行失败，当事务执行失败时需要返回ROLLBACK消息
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
                logger.info("模拟本地事务执行成功");
                // 表示本地事务执行成功
                return LocalTransactionState.COMMIT_MESSAGE;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                logger.info("服务器调用消息回查接口");
                logger.info("消息标签是 " + new String(messageExt.getTags()));
                logger.info("消息内容是 " + new String(messageExt.getBody()));

                return LocalTransactionState.COMMIT_MESSAGE;
            }
        });

        producer.start();

        // 为了演示事务成功和失败的效果，发送两条消息，根据消息的tag 分别演示事务成功和事务失败
        for (int i = 0; i < 2; i++) {
            Message message = new Message("TopicTransaction", "Transaction" + i,
                    ("Hello RocketMq distribution transaction" + i).getBytes());
            SendResult sendResult = producer.sendMessageInTransaction(message, null);
            logger.info(String.valueOf(sendResult));
            logger.info("");
            TimeUnit.MILLISECONDS.sleep(10);
        }

        for (int i = 0; i < 100; i++) {
            Thread.sleep(1000);
        }

        producer.shutdown();

    }
}
