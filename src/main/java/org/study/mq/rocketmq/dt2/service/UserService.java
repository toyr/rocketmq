package org.study.mq.rocketmq.dt2.service;

import org.study.mq.rocketmq.dt2.model.User;

/**
 * @author unisk1123
 * @Description
 * @create 2021-08-10 7:46 下午
 */
public interface UserService {
    void saveUser(String userid, String userName);

    User getById(String userId);
}
