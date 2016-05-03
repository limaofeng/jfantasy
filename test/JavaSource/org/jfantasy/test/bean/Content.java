package org.jfantasy.test.bean;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * 内容表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-11-4 下午05:47:36
 */
public class Content {

    private static final long serialVersionUID = -7570871629827875364L;

    private Long id;
    /**
     * 正文内容
     */
    private String text;

    public Content() {
    }

    public Content(String text) {
        this.setText(text);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text == null ? "" : this.text;
    }

    public static class ContentSerialize extends JsonSerializer<Content> {

        @Override
        public void serialize(Content content, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            if (content == null) {
                jgen.writeString("");
            } else {
                jgen.writeString(content.toString());
            }
        }

    }

}
