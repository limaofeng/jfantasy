package org.jfantasy.aliyun;

public class AliyunSettings {

    private String topicId;
    private String producerId;
    private String consumerId;
    private String accessKey;
    private String secretKey;

    public AliyunSettings() {
    }

    public AliyunSettings(AliyunSettings aliyunSettings) {
        this.accessKey = aliyunSettings.getAccessKey();
        this.secretKey = aliyunSettings.getSecretKey();
        this.topicId = aliyunSettings.getTopicId();
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getProducerId() {
        return producerId;
    }

    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }
}
