package org.study.mq.rocketmq.order;

import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

/**
 * @author unisk1123
 * @Description
 * @create 2019/5/25
 */
public class OrderMessageListener implements MessageListenerOrderly {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
        // 默认 list 中只有一条消息，可以通过设置参数来批量接受消息
        if (list != null) {
            try {
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                logger.info(LocalDateTime.now().format(timeFormatter) + "接收到消息");
                // 模拟业务处理消息时间
                Thread.sleep(new Random().nextInt(1000));
                for (MessageExt ext : list) {
                    try {
                        logger.info(LocalDateTime.now().format(timeFormatter) + "消息内容：" + new String(ext.getBody(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ConsumeOrderlyStatus.SUCCESS;
    }
}
