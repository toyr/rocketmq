package org.study.mq.rocketmq.dt2.model;

/**
 * @author unisk1123
 * @Description
 * @create 2021-08-10 7:24 下午
 */
public class Point {
    private String id;
    private String userId;
    private Integer amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
