package org.study.mq.rocketmq.dt2.service;

import org.study.mq.rocketmq.dt2.model.Point;

/**
 * @author unisk1123
 * @Description
 * @create 2021-08-10 8:03 下午
 */
public interface PointService {

    int savePoint(Point point);
}
