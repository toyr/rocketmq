package org.study.mq.rocketmq.dt2.dao;

import org.study.mq.rocketmq.dt2.model.Point;

/**
 * @author unisk1123
 * @Description
 * @create 2021-08-10 7:29 下午
 */
public interface PointDao {
    public String insert(Point point);

    public Point getByUserId(String userId);
}
