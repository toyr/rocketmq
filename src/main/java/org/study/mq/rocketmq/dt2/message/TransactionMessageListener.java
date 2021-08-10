package org.study.mq.rocketmq.dt2.message;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.study.mq.rocketmq.dt2.model.Point;
import org.study.mq.rocketmq.dt2.model.UserPointMessage;
import org.study.mq.rocketmq.dt2.service.PointService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author unisk1123
 * @Description
 * @create 2021-08-10 8:02 下午
 */
public class TransactionMessageListener implements MessageListenerConcurrently {

    private Logger logger = LoggerFactory.getLogger(TransactionMessageListener.class);

    @Resource
    private PointService pointService;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {

        try {
            for (MessageExt message : list) {
                logger.info("消息消费者接受到消息：" + message);
                logger.info("接收到的消息内容是：" + new String(message.getBody()));
                // 从消息体中获取积分消息对象
                UserPointMessage pointMessage = JSON.parseObject(message.getBody(), UserPointMessage.class);
                if (pointMessage != null) {
                    Point point = new Point();
                    point.setUserId(pointMessage.getUserId());
                    point.setAmount(pointMessage.getAmount());
                    pointService.savePoint(point);
                }
            }
        } catch (Exception e) {
            logger.error("消费消息时报错", e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
