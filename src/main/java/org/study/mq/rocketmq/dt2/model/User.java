package org.study.mq.rocketmq.dt2.model;

/**
 * @author unisk1123
 * @Description
 * @create 2021-08-10 7:22 下午
 */
public class User {
    private String id; // 主键
    private String userName; //用户名

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
