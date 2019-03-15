package com.channelsoft.common.rocketmq;

/**
 * @author sicwen
 * @date 2019/03/12
 */
public class TopicInfo {
    private String topicName;
    private int wirteQueueSize;
    private int readQueueSize;
    private int perm;

    public TopicInfo() {
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public int getWirteQueueSize() {
        return wirteQueueSize;
    }

    public void setWirteQueueSize(int wirteQueueSize) {
        this.wirteQueueSize = wirteQueueSize;
    }

    public int getReadQueueSize() {
        return readQueueSize;
    }

    public void setReadQueueSize(int readQueueSize) {
        this.readQueueSize = readQueueSize;
    }

    public int getPerm() {
        return perm;
    }

    public void setPerm(int perm) {
        this.perm = perm;
    }

    @Override
    public String toString() {
        return "TopicInfo{" +
                "topicName='" + topicName + '\'' +
                ", wirteQueueSize=" + wirteQueueSize +
                ", readQueueSize=" + readQueueSize +
                ", perm=" + perm +
                '}';
    }
}
