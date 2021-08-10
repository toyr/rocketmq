package org.study.mq.rocketmq.dt2.message;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.study.mq.rocketmq.dt2.model.UserPointMessage;
import org.study.mq.rocketmq.dt2.service.UserService;

import javax.annotation.Resource;

/**
 * @author unisk1123
 * @Description
 * @create 2021-08-10 7:39 下午
 */
public class UserLocalTransactionListener implements TransactionListener {
    private Logger logger = LoggerFactory.getLogger(UserLocalTransactionListener.class);
    @Resource
    private UserService userService;

    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        logger.info("本地事务执行");
        logger.info("消息标签是：" + new String(message.getTags()));
        logger.info("消息内容是：" + new String(message.getBody()));
        // 从消息体重获取积分消息对象
        UserPointMessage userPointMessage = JSON.parseObject(message.getBody(), UserPointMessage.class);
        // 保存用户记录并提交本地事务
        userService.saveUser(userPointMessage.getUserId(), userPointMessage.getUserName());

        return LocalTransactionState.COMMIT_MESSAGE;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        logger.info("消息服务器调用消息会查接口");
        logger.info("消息标签是：" + new String(messageExt.getTags()));
        logger.info("消息内容是：" + new String(messageExt.getBody()));
        // 从消息体中获取积分消息对象
        UserPointMessage userPointMessage = JSON.parseObject(messageExt.getBody(), UserPointMessage.class);
        if (userPointMessage != null) {
            String userId = userPointMessage.getUserId();
            if (userService.getById(userId) != null) {
                logger.info("本地插入用户表成功！");
                // 表示本地事务执行成功
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        }
        return LocalTransactionState.ROLLBACK_MESSAGE;
    }
}
