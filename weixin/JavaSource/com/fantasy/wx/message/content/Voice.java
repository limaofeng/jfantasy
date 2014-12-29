package com.fantasy.wx.message.content;

/**
 * 语音消息
 */
public class Voice {

    /**
     * 语音识别结果，UTF8编码
     */
    private String recognition;
    /**
     * 音频媒体信息
     */
    private Media media;

    public Voice(Media media, String recognition) {
        this.media = media;
        this.recognition = recognition;
    }

    public String getRecognition() {
        return recognition;
    }

    public void setRecognition(String recognition) {
        this.recognition = recognition;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }
}
