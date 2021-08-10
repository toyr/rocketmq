package org.study.mq.rocketmq.dt2.dao;

import org.study.mq.rocketmq.dt2.model.User;

/**
 * @author unisk1123
 * @Description
 * @create 2021-08-10 7:27 下午
 */
public interface UserDao{

    public String getId();

    public String insert(String id, String userName);

    public User getById(String id);
}
