package com.fantasy.cms.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.system.util.SettingUtil;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import javax.persistence.*;
import java.io.IOException;

/**
 * 内容表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-11-4 下午05:47:36
 */
@Entity
@Table(name = "CMS_CONTENT")
public class Content extends BaseBusEntity {

    private static final long serialVersionUID = -7570871629827875364L;

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = true, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @org.hibernate.annotations.GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 正文内容
     */
    @Lob
    @Column(name = "CONTENT")
    private String content;

    public Content() {
    }

    public Content(String text) {
        this.setContent(text);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String text) {
        this.content = text;
    }

    @Override
    public String toString() {
        return this.content == null ? "" : this.content;
    }

    public static class ContentSerialize extends JsonSerializer<Content> {

        @Override
        public void serialize(Content content, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            try {
                if (content == null) {
                    jgen.writeString("");
                } else {
                    jgen.writeString(SettingUtil.toHtml(content.toString()));
                }
            } catch (IOException e) {
                jgen.writeString("");
            }
        }

    }

}
