package org.study.mq.rocketmq.order;

/**
 * @author unisk1123
 * @Description
 * @create 2019/5/25
 */
public class OrderMessage {

    private int id;
    private String status;
    private int sendOrder;
    private String content;

    public int getId() {
        return id;
    }

    public OrderMessage setId(int id) {
        this.id = id;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public OrderMessage setStatus(String status) {
        this.status = status;
        return this;
    }

    public int getSendOrder() {
        return sendOrder;
    }

    public OrderMessage setSendOrder(int sendOrder) {
        this.sendOrder = sendOrder;
        return this;
    }

    public String getContent() {
        return content;
    }

    public OrderMessage setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public String toString() {
        return "OrderMessage{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", sendOrder=" + sendOrder +
                ", content='" + content + '\'' +
                '}';
    }
}
