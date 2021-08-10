package org.study.mq.rocketmq.dt2.service;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.mq.rocketmq.dt2.dao.UserDao;
import org.study.mq.rocketmq.dt2.message.TransactionSpringProducer;
import org.study.mq.rocketmq.dt2.model.User;
import org.study.mq.rocketmq.dt2.model.UserPointMessage;

import javax.annotation.Resource;

/**
 * @author unisk1123
 * @Description
 * @create 2021-08-10 8:09 下午
 */
@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private TransactionSpringProducer producer;

    @Resource
    private UserDao userDao;

    @Transactional(rollbackFor = Exception.class)
    public void newUserAndPoint(String userName, Integer amount) throws Exception {
        String userId = userDao.getId();
        UserPointMessage userPointMessage = new UserPointMessage();
        userPointMessage.setUserId(userId);
        userPointMessage.setAmount(amount);
        this.sendMessage(userPointMessage);
    }

    /**
     * 给消费者发送消息
     *
     * @param userPointMessage
     */
    private void sendMessage(UserPointMessage userPointMessage) throws Exception {
        Message message = new Message();
        message.setTopic("distributed_transaction_spring_topic");
        message.setTags("newUserAndPoint");
        message.setKeys(userPointMessage.getUserId());
        message.setBody(JSON.toJSONString(userPointMessage).getBytes());
        //发送消息，并封装本地事务处理逻辑
        SendResult sendResult = producer.getProducer().sendMessageInTransaction(message, "");
        logger.info("消息发送结果：" + sendResult);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(String userid, String userName) {
        userDao.insert(userid, userName);
    }

    @Override
    public User getById(String userId) {
        return userDao.getById(userId);
    }
}
