package org.study.mq.rocketmq.dt2.model;

/**
 * @author unisk1123
 * @Description
 * @create 2021-08-10 7:24 下午
 */
public class UserPointMessage {
    private String userId;
    private String userName;
    private Integer amount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
