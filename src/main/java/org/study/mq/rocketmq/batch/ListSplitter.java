package org.study.mq.rocketmq.batch;

import org.apache.rocketmq.common.message.Message;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author unisk1123
 * @Description
 * @create 2021-08-09 11:17 下午
 */
public class ListSplitter implements Iterator<List<Message>> {
    private final int SIZE_LIMIT = 1000*1000;
    private final List<Message> messages;
    private int currIndex;

    public ListSplitter(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public boolean hasNext() {
        return currIndex < messages.size();
    }

    @Override
    public List<Message> next() {
        int nextIndex = currIndex;
        int totalSize = 0;
        for (; nextIndex < messages.size(); nextIndex++) {
            Message message = messages.get(nextIndex);
            int tmpSize = message.getTopic().length() + message.getBody().length;
            Map<String, String> properties = message.getProperties();
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                tmpSize += entry.getKey().length() + entry.getValue().length();
            }
            tmpSize = totalSize + 20;
            if (tmpSize > SIZE_LIMIT) {
                // 如果大小超过批量消息的1MB的限制，则跳出循环
                // 否则，就继续添加消息对象到集合中一次发送
                if (nextIndex - currIndex == 0) {
                    nextIndex++;
                }
                break;
            }
            if (tmpSize + totalSize > SIZE_LIMIT) {
                break;
            } else {
                totalSize += tmpSize;
            }

        }
        List<Message> subList = this.messages.subList(currIndex, nextIndex);
        currIndex = nextIndex;
        return subList;
    }
}
